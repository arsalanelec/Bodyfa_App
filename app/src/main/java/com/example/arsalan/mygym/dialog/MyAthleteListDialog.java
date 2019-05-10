package com.example.arsalan.mygym.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterAthletesDialog;
import com.example.arsalan.mygym.models.RetUserList;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyAthleteListDialog extends DialogFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TRAINER = "param1";
    private static final String ARG_TITLE = "param2";
    private static final String ARG_BODY = "param3";
    private static final String ARG_PLAN_ID = "param4";


    private List<User> userList;
    private OnFragmentInteractionListener mListener;
    private AdapterAthletesDialog adapter;

    private Trainer mCurrentTrainer;
    private TextView athleteCnt;

    private String mPlanTitle;
    private String mPlanBody;
    private long mPlanId;

    public MyAthleteListDialog() {
        // Required empty public constructor
    }


    public static MyAthleteListDialog newInstance(Trainer trainer, long planId, String title, String body) {
        MyAthleteListDialog fragment = new MyAthleteListDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TRAINER, trainer);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_BODY, body);
        args.putLong(ARG_PLAN_ID, planId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentTrainer = getArguments().getParcelable(ARG_TRAINER);
            mPlanId = getArguments().getLong(ARG_PLAN_ID);
            mPlanTitle = getArguments().getString(ARG_TITLE);
            mPlanBody = getArguments().getString(ARG_BODY);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_athlete_list, container, false);

        athleteCnt = v.findViewById(R.id.txtAthleteCount);
        athleteCnt.setVisibility(View.INVISIBLE);

        RecyclerView rv = v.findViewById(R.id.rvTrainers);
        userList = new ArrayList<>();
        adapter = new AdapterAthletesDialog(userList, new AdapterAthletesDialog.OnItemClickListener() {
            @Override
            public void onItemClick(User user, View view) {
                Intent intent = new Intent();
                intent.putExtra(MyKeys.EXTRA_ATHLETE_ID, user.getId());
                intent.putExtra(MyKeys.EXTRA_PLAN_ID, mPlanId);
                intent.putExtra(MyKeys.EXTRA_PLAN_TITLE, mPlanTitle);
                intent.putExtra(MyKeys.EXTRA_PLAN_BODY, mPlanBody);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
//                mListener.onAthleteSelected(user);
                /*Intent i = new Intent();
                i.setClass(getActivity(), ProfileTrainerActivity.class);
                i.putExtra(EXTRA_PARCLABLE_OBJ, trainer);
                i.putExtra(EXTRA_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(view));

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        view,
                        ViewCompat.getTransitionName(view));

                startVideoRecorderActivity(i, options.toBundle());*/
            }
        });
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        getMyAthleteList(mCurrentTrainer.getId());
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {

        super.onStart();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private void getMyAthleteList(long trainerId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        waitingDialog.setMessage(getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        Call<RetUserList> call = apiService.getMyAthleteList("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), trainerId);
        call.enqueue(new Callback<RetUserList>() {
            @Override
            public void onResponse(Call<RetUserList> call, Response<RetUserList> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("getMyAthleteList", "onResponse: records:" + response.body().getRecordsCount());
                    if (response.body() != null) {
                        athleteCnt.setText(getString(R.string.athlete_count, response.body().getRecordsCount()));
                        athleteCnt.setVisibility(View.VISIBLE);
                        userList.removeAll(userList);
                        userList.addAll(response.body().getRecords());
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Log.d("getMyAthleteList", "onResponse: error:"+response.message());
                }
            }

            @Override
            public void onFailure(Call<RetUserList> call, Throwable t) {
                waitingDialog.dismiss();
                Log.d("getMyAthleteList", "onFailure: error:"+t.getCause());

            }
        });

    }

    public interface OnFragmentInteractionListener {
        void onAthleteSelected(User user);
    }


}

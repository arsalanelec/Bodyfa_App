package com.example.arsalan.mygym.fragments;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arsalan.mygym.activities.MessageRoomActivity;
import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterAthletes;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.viewModels.AthleteListViewModel;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.room.UserDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class MyAthleteListFragment extends Fragment implements Injectable{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TRAINER = "param2";
    private static final String ARG_USER = "param1";


    private List<User> mUserList;
    private OnFragmentInteractionListener mListener;
    private AdapterAthletes mAdapter;

    private Trainer mCurrentTrainer;
    private User mCurrentUser;
    private TextView athleteCnt;

    private AthleteListViewModel athleteListViewModel;

    @Inject
    MyViewModelFactory factory;

    @Inject
    UserDao userDao;

    public MyAthleteListFragment() {
        // Required empty public constructor
    }


    public static MyAthleteListFragment newInstance(User currentUser, Trainer trainer) {
        MyAthleteListFragment fragment = new MyAthleteListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TRAINER, trainer);
        args.putParcelable(ARG_USER, currentUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentTrainer = getArguments().getParcelable(ARG_TRAINER);
            mCurrentUser = getArguments().getParcelable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_athlete_list, container, false);

        athleteCnt = v.findViewById(R.id.txtAthleteCount);
        athleteCnt.setVisibility(View.INVISIBLE);

        RecyclerView rv = v.findViewById(R.id.rvTrainers);
        mUserList = new ArrayList<>();
        mAdapter = new AdapterAthletes(mUserList, new AdapterAthletes.OnItemClickListener() {
            @Override
            public void onItemClick(User user, View view) {
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

            @Override
            public void onSendMessageClicked(User user) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MessageRoomActivity.class);
                intent.putExtra(MyKeys.EXTRA_USER_ID, mCurrentUser.getId());
                intent.putExtra(MyKeys.EXTRA_PARTY_ID, user.getId());
                intent.putExtra(MyKeys.EXTRA_PARTY_NAME, user.getName());
                intent.putExtra(MyKeys.EXTRA_PARTY_THUMB, user.getThumbUrl());

                startActivity(intent);

            }
        });
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));


       // getMyAthleteList(mCurrentTrainer.getId());
        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        athleteListViewModel = ViewModelProviders.of(this, factory).get(AthleteListViewModel.class);
        athleteListViewModel.init("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mCurrentUser.getId());
        athleteListViewModel.getUserList().observe(this, userList -> {
            Log.d(getClass().getSimpleName(), "onActivityCreated observe: mealPlans cnt:" + userList.size());
            mUserList.removeAll(mUserList);
            mUserList.addAll(userList);
            athleteCnt.setText(getString(R.string.athlete_count, userList.size()));
            athleteCnt.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
            // waitingFL.setVisibility(View.GONE);
        });

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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

/*    private void getMyAthleteList(long trainerId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        waitingDialog.setMessage(getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        Call<RetUserList> call = apiService.getMyAthleteList("Bearer " + ((MyApplication2) getActivity().getApplication()).getCurrentToken().getToken(), trainerId);
        call.enqueue(new Callback<RetUserList>() {
            @Override
            public void onResponse(Call<RetUserList> call, Response<RetUserList> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful())
                    Log.d("getNewsWeb", "onResponse: records:" + response.body().getRecordsCount());
                if (response.body() != null && response.body().getRecordsCount() > 0) {
                    athleteCnt.setText(getString(R.string.athlete_count, response.body().getRecordsCount()));
                    athleteCnt.setVisibility(View.VISIBLE);

                    mUserList.removeAll(mUserList);
                    mUserList.addAll(response.body().getRecords());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RetUserList> call, Throwable t) {
                waitingDialog.dismiss();

            }
        });

    }*/


}

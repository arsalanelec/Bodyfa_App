package com.example.arsalan.mygym.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterAthletesDialog;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.TrainerAthlete;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.viewModels.AthleteListViewModel;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class MyAthleteListDialog extends DialogFragment implements Injectable {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TRAINER = "param1";
    private static final String ARG_TITLE = "param2";
    private static final String ARG_BODY = "param3";
    private static final String ARG_PLAN_ID = "param4";


    private final List<TrainerAthlete> mAthleteList=new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private AdapterAthletesDialog mAdapter;

    private Trainer mCurrentTrainer;
    private TextView athleteCnt;

    private String mPlanTitle;
    private String mPlanBody;
    private long mPlanId;
    private AthleteListViewModel acceptedAthleteListViewModel;

    @Inject
    MyViewModelFactory factory;
    private View waitingFL;

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

        RecyclerView rv = v.findViewById(R.id.rv_trainers);
        mAdapter = new AdapterAthletesDialog(mAthleteList, athlete -> {
            Intent intent = new Intent();
            intent.putExtra(MyKeys.EXTRA_ATHLETE_ID, athlete.getId());
            intent.putExtra(MyKeys.EXTRA_PLAN_ID, mPlanId);
            intent.putExtra(MyKeys.EXTRA_PLAN_TITLE, mPlanTitle);
            intent.putExtra(MyKeys.EXTRA_PLAN_BODY, mPlanBody);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            dismiss();
//                mListener.onAthleteSelected(athlete);
            /*Intent i = new Intent();
            i.setClass(getActivity(), ProfileTrainerActivity.class);
            i.putExtra(EXTRA_PARCLABLE_OBJ, trainer);
            i.putExtra(EXTRA_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(view));

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    getActivity(),
                    view,
                    ViewCompat.getTransitionName(view));

            startVideoRecorderActivity(i, options.toBundle());*/
        });
        waitingFL = v.findViewById(R.id.fl_waiting);
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        acceptedAthleteListViewModel = ViewModelProviders.of(this, factory).get(AthleteListViewModel.class);
        acceptedAthleteListViewModel.init( mCurrentTrainer.getId(),true);
        acceptedAthleteListViewModel.getAthleteList().observe(this, userList -> {
            Log.d(getClass().getSimpleName(), "onActivityCreated observe: mealPlans cnt:" + userList.size());
            mAthleteList.clear();
            mAthleteList.addAll(userList);
            athleteCnt.setText(getString(R.string.athlete_count, userList.size()));
            athleteCnt.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
            athleteCnt.setText(getString(R.string.athlete_count, userList.size()));
            athleteCnt.setVisibility(View.VISIBLE);
             waitingFL.setVisibility(View.GONE);
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

    @Override
    public void onStart() {

        super.onStart();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public interface OnFragmentInteractionListener {
        void onAthleteSelected(User user);
    }


}

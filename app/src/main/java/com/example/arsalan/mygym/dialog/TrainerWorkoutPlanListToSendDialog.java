package com.example.arsalan.mygym.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterTrainerWorkoutPlanList;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.WorkoutPlan;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TrainerWorkoutListViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TrainerWorkoutPlanListToSendDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainerWorkoutPlanListToSendDialog extends DialogFragment implements Injectable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_REQUEST_ID = "requestId";
    private static final String ARG_TRAINER_ID="trainerid";
    private static final String ARG_ATHLETE_ID ="athleteid";
    private static final String ARG_ATHLETE_NAME="athletename";
    private static final String ARG_ATHLETE_THUMB="Athletethumb";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TrainerWorkoutListViewModel workoutListViewModel;
    @Inject
    MyViewModelFactory factory;
    private long mTrainerId;
    private int mRequestId;
    private long mAthleteId;
    private String mAthleteName;
    private String mAthleteThumb;

    public TrainerWorkoutPlanListToSendDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TrainerWorkoutPlanListToSendDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static TrainerWorkoutPlanListToSendDialog newInstance(Long param1) {
        TrainerWorkoutPlanListToSendDialog fragment = new TrainerWorkoutPlanListToSendDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<WorkoutPlan> mWorkoutPlanList=new ArrayList<>();
    private AdapterTrainerWorkoutPlanList adapter;

    public static TrainerWorkoutPlanListToSendDialog newInstance(int requestId, long trainerId, long athleteId, String athleteName, String athleteThumbUrl) {
        TrainerWorkoutPlanListToSendDialog fragment = new TrainerWorkoutPlanListToSendDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_ID, requestId);
        args.putLong(ARG_TRAINER_ID, trainerId);
        args.putLong(ARG_ATHLETE_ID, athleteId);
        args.putString(ARG_ATHLETE_NAME, athleteName);
        args.putString(ARG_ATHLETE_THUMB, athleteThumbUrl);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTrainerId = getArguments().getLong(ARG_TRAINER_ID);
            mRequestId = getArguments().getInt(ARG_REQUEST_ID);
            mAthleteId = getArguments().getLong(ARG_ATHLETE_ID);
            mAthleteName = getArguments().getString(ARG_ATHLETE_NAME);
            mAthleteThumb = getArguments().getString(ARG_ATHLETE_THUMB);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mealplan_list, container, false);
        if (mWorkoutPlanList != null) {

            adapter = new AdapterTrainerWorkoutPlanList(getDialog().getContext(), mWorkoutPlanList, new AdapterTrainerWorkoutPlanList.OnItemClickListener() {
                @Override
                public void onItemEditClick(WorkoutPlan workoutPlan, int position) {
                    Intent intent = new Intent();
                    intent.putExtra(MyKeys.EXTRA_PLAN_ID, workoutPlan.getTrainerWorkoutPlanId());
                    intent.putExtra(MyKeys.EXTRA_EDIT_MODE, true);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), MyKeys.RESULT_EDIT, intent);
                }

                @Override
                public void onItemDeleteClick(final WorkoutPlan workoutPlan, final int position) {
                    new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom).setMessage(getString(R.string.ask_remove_plan))
                            .setPositiveButton(getString(R.string.remove), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int ii) {
                                    dialogInterface.dismiss();
                                    Intent intent = new Intent();
                                    intent.putExtra(MyKeys.EXTRA_PLAN_ID, workoutPlan.getTrainerWorkoutPlanId());
                                    getTargetFragment().onActivityResult(getTargetRequestCode(), MyKeys.RESULT_DELETE, intent);
                                    adapter.removeItem(position);
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).show();
                }

                @Override
                public void onItemShowClick(WorkoutPlan workoutPlan, int position) {
                    Intent intent = new Intent();
                    intent.putExtra(MyKeys.EXTRA_PLAN_ID, workoutPlan.getTrainerWorkoutPlanId());
                    intent.putExtra(MyKeys.EXTRA_EDIT_MODE, false);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), MyKeys.RESULT_SHOW, intent);
                }

                @Override
                public void onItemSendClick(WorkoutPlan workoutPlan, int position) {
                    Intent intent = new Intent();
                    intent.putExtra(MyKeys.EXTRA_PLAN_ID, workoutPlan.getTrainerWorkoutPlanId());
                    getTargetFragment().onActivityResult(getTargetRequestCode(), MyKeys.RESULT_SEND, intent);
                }
            });
            ListView planLV = v.findViewById(R.id.listView);
            planLV.setAdapter(adapter);
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        workoutListViewModel = ViewModelProviders.of(this, factory).get(TrainerWorkoutListViewModel.class);
        workoutListViewModel.init("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mTrainerId);
        workoutListViewModel.getWorkoutPlanItemList().observe(this, newWorkoutPlans -> {
            mWorkoutPlanList.removeAll(mWorkoutPlanList);
            mWorkoutPlanList.addAll(newWorkoutPlans);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onStart() {

        super.onStart();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }
}

package com.example.arsalan.mygym.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.models.WorkoutPlan;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterTrainerWorkoutPlanList;

import java.util.ArrayList;


public class TrainerWorkoutPlanListDialog extends DialogFragment {


    private static final String ARG_PLAN = "column-count";

    private ArrayList<WorkoutPlan> mWorkoutPlanList;
    private AdapterTrainerWorkoutPlanList adapter;


    public TrainerWorkoutPlanListDialog() {
    }

    public static TrainerWorkoutPlanListDialog newInstance(ArrayList<WorkoutPlan> workoutPlans) {
        TrainerWorkoutPlanListDialog fragment = new TrainerWorkoutPlanListDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PLAN, workoutPlans);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mWorkoutPlanList = getArguments().getParcelableArrayList(ARG_PLAN);
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
                    new AlertDialog.Builder(getContext()).setMessage(getString(R.string.ask_remove_plan))
                            .setPositiveButton(getString(R.string.remove), (dialogInterface, ii) -> {
                                dialogInterface.dismiss();
                                Intent intent = new Intent();
                                intent.putExtra(MyKeys.EXTRA_PLAN_ID, workoutPlan.getTrainerWorkoutPlanId());
                                getTargetFragment().onActivityResult(getTargetRequestCode(), MyKeys.RESULT_DELETE, intent);
                                adapter.removeItem(position);
                            })
                            .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.cancel()).show();
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
    public void onStart() {

        super.onStart();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }


}

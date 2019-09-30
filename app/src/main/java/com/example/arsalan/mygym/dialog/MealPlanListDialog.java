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
import com.example.arsalan.mygym.models.MealPlan;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterTrainerMealPlanList;

import java.util.ArrayList;


public class MealPlanListDialog extends DialogFragment {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private ArrayList<MealPlan> mMealPlanList;
    private AdapterTrainerMealPlanList adapterMealLVLimited;


    public MealPlanListDialog() {
    }

    public static MealPlanListDialog newInstance(ArrayList<MealPlan> mealPlans) {
        MealPlanListDialog fragment = new MealPlanListDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_COLUMN_COUNT, mealPlans);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMealPlanList = getArguments().getParcelableArrayList(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mealplan_list, container, false);
        if (mMealPlanList != null) {

            adapterMealLVLimited = new AdapterTrainerMealPlanList(getDialog().getContext(), mMealPlanList, new AdapterTrainerMealPlanList.OnItemClickListener() {
                @Override
                public void onItemEditClick(MealPlan mealPlan) {
                    Intent intent = new Intent();
                    intent.putExtra(MyKeys.EXTRA_PLAN_ID, mealPlan.getTrainerMealPlanId());
                    intent.putExtra(MyKeys.EXTRA_EDIT_MODE, true);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), MyKeys.RESULT_EDIT, intent);
                }

                @Override
                public void onItemDeleteClick(final MealPlan mealPlan, final int position) {
                    new AlertDialog.Builder(getContext()).setMessage(getString(R.string.ask_remove_plan))
                            .setPositiveButton(getString(R.string.remove), (dialogInterface, ii) -> {
                                dialogInterface.dismiss();
                                Intent intent = new Intent();
                                intent.putExtra(MyKeys.EXTRA_PLAN_ID, mealPlan.getTrainerMealPlanId());
                                getTargetFragment().onActivityResult(getTargetRequestCode(), MyKeys.RESULT_DELETE, intent);
                                adapterMealLVLimited.removeItem(position);
                            })
                            .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.cancel()).show();
                }

                @Override
                public void onItemShowClick(MealPlan mealPlan) {
                    Intent intent = new Intent();
                    intent.putExtra(MyKeys.EXTRA_PLAN_ID, mealPlan.getTrainerMealPlanId());
                    intent.putExtra(MyKeys.EXTRA_EDIT_MODE, false);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), MyKeys.RESULT_SHOW, intent);
                }

                @Override
                public void onItemSendClick(MealPlan mealPlan) {
                    Intent intent = new Intent();
                    intent.putExtra(MyKeys.EXTRA_PLAN_ID, mealPlan.getTrainerMealPlanId());
                    getTargetFragment().onActivityResult(getTargetRequestCode(), MyKeys.RESULT_SEND, intent);
                }
            });
            ListView mealPlanLV = v.findViewById(R.id.listView);
            mealPlanLV.setAdapter(adapterMealLVLimited);
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

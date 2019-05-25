package com.example.arsalan.mygym.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.WorkoutPlan;

import java.util.List;

public class AdapterTrainerWorkoutPlanListSimple extends BaseAdapter {
    List<WorkoutPlan> mWorkoutPlanList;
    int index = -1;
    Context mContext;
    OnItemClickListener mListener;
    private static final String TAG = "AdapterTrainerWorkoutPl";

    public AdapterTrainerWorkoutPlanListSimple(Context context, List<WorkoutPlan> workoutPlans, OnItemClickListener onItemClickListener) {
        this.mWorkoutPlanList = workoutPlans;
        this.mContext = context;
        mListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return mWorkoutPlanList.size();
    }

    @Override
    public WorkoutPlan getItem(int i) {
        return mWorkoutPlanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mWorkoutPlanList.get(i).getTrainerWorkoutPlanId();
    }

    public void removeItem(int i) {
        mWorkoutPlanList.remove(i);
        this.notifyDataSetChanged();

    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_trainer_simple_workout_plan_list, null);
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            animation.setDuration(200);
            view.startAnimation(animation);
        }

        RadioButton rbSelect = view.findViewById(R.id.rbSelect);
        rbSelect.setText(mWorkoutPlanList.get(i).getTitle());
        rbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=i;
                notifyDataSetChanged();
            }
        });
        if (i == index) {
            rbSelect.setChecked(true);
            mListener.onItemSendClick(mWorkoutPlanList.get(i),i);
        } else {
            rbSelect.setChecked(false);
        }

        return view;
    }

    public interface OnItemClickListener {
        void onItemSendClick(WorkoutPlan workoutPlan,int position);
    }
}

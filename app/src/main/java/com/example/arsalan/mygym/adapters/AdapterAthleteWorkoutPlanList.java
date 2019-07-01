package com.example.arsalan.mygym.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.arsalan.mygym.models.WorkoutPlan;
import com.example.arsalan.mygym.R;

import java.util.List;

public class AdapterAthleteWorkoutPlanList extends BaseAdapter {
    private final List<WorkoutPlan> mWorkoutPlanList;

    private final Context mContext;
    private final OnItemClickListener mListener;

    public AdapterAthleteWorkoutPlanList(Context context, List<WorkoutPlan> workoutPlanList, OnItemClickListener onItemClickListener) {
        this.mWorkoutPlanList = workoutPlanList;
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
            view = View.inflate(mContext, R.layout.item_athlete_workout_plan, null);
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            animation.setDuration(200);
            view.startAnimation(animation);
        }

        TextView titleTV = view.findViewById(R.id.txtTitle);
       // TextView dateTV = view.findViewById(R.id.txtDate);

        titleTV.setText(mWorkoutPlanList.get(i).getTitle());
      //  dateTV.setText(mWorkoutPlanList.get(i).getDate());
        TextView trainerNameTV = view.findViewById(R.id.txtTrainerName);
        trainerNameTV.setText(mWorkoutPlanList.get(i).getTrainerName());
        view.setOnClickListener(view1 -> mListener.onItemShowClick(getItem(i)));


        return view;
    }

    public interface OnItemClickListener {


        void onItemShowClick(WorkoutPlan workoutPlan);


    }
}

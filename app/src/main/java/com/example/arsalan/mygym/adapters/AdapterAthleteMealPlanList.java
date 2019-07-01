package com.example.arsalan.mygym.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.arsalan.mygym.models.MealPlan;
import com.example.arsalan.mygym.R;

import java.util.List;

public class AdapterAthleteMealPlanList extends BaseAdapter {
    private final List<MealPlan> mMealPlanList;

    private final Context mContext;
    private final OnItemClickListener mListener;

    public AdapterAthleteMealPlanList(Context context, List<MealPlan> mealPlanList, OnItemClickListener onItemClickListener) {
        this.mMealPlanList = mealPlanList;
        this.mContext = context;
        mListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return mMealPlanList.size();
    }

    @Override
    public MealPlan getItem(int i) {
        return mMealPlanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mMealPlanList.get(i).getTrainerMealPlanId();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_athlete_meal_plan, null);
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            animation.setDuration(200);
            view.startAnimation(animation);
        }

        TextView titleTV = view.findViewById(R.id.txtTitle);

        titleTV.setText(mMealPlanList.get(i).getTitle());

        TextView trainerNameTV = view.findViewById(R.id.txtTrainerName);
        trainerNameTV.setText(mMealPlanList.get(i).getTrainerName());

        view.setOnClickListener(view1 -> mListener.onItemShowClick(getItem(i)));


        return view;
    }

    public interface OnItemClickListener {


        void onItemShowClick(MealPlan mealPlan);

    }
}

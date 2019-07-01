package com.example.arsalan.mygym.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.arsalan.mygym.models.MealPlan;
import com.example.arsalan.mygym.R;

import java.util.List;

public class AdapterTrainerMealPlanList extends BaseAdapter {
    private final List<MealPlan> mMealPlanList;

    private final Context mContext;
    private final OnItemClickListener mListener;

    public AdapterTrainerMealPlanList(Context context, List<MealPlan> mealPlanList, OnItemClickListener onItemClickListener) {
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

    public void removeItem(int i) {
        mMealPlanList.remove(i);
        this.notifyDataSetChanged();

    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_listview_trainer_plan, null);
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            animation.setDuration(200);
            view.startAnimation(animation);
        }

        TextView titleTV = view.findViewById(R.id.txtTitle);
        TextView dateTV = view.findViewById(R.id.txtDate);

        titleTV.setText(mMealPlanList.get(i).getTitle());
        dateTV.setText(mMealPlanList.get(i).getDate());

        view.setOnClickListener(view14 -> mListener.onItemShowClick(getItem(i)));

        Button deleteBtn = view.findViewById(R.id.btnDelete);
        deleteBtn.setOnClickListener(view13 -> mListener.onItemDeleteClick(getItem(i), i));

        Button editBtn = view.findViewById(R.id.btnShowTutorial);
        editBtn.setOnClickListener(view12 -> mListener.onItemEditClick(getItem(i)));

        Button sendBtn = view.findViewById(R.id.btnSend);
        sendBtn.setOnClickListener(view1 -> mListener.onItemSendClick(getItem(i)));
        return view;
    }

    public interface OnItemClickListener {
        void onItemEditClick(MealPlan mealPlan);

        void onItemDeleteClick(MealPlan mealPlan, int position);

        void onItemShowClick(MealPlan mealPlan);

        void onItemSendClick(MealPlan mealPlan);

    }
}

package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.databinding.ItemNewWorkoutBinding;
import com.example.arsalan.mygym.dialog.AddEditWorkoutPlanDialog;
import com.example.arsalan.mygym.models.WorkoutRow;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class NewWorkoutPlanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_IS_EDITABLE = "ARG_IS_EDITABLE";

    private static final String ARG_LIST = "param list";
    private static final int REQ_WORKOUT_PLAN = 1000;

    // TODO: Rename and change types of parameters
    private int mDayOfWeek;

    private List<WorkoutRow> mWorkoutRowList;
    private OnFragmentInteractionListener mListener;
    private boolean mIsEditable;
    private AdapterWorkoutLV adapter;

    public NewWorkoutPlanFragment() {
        // Required empty public constructor
    }


    public static NewWorkoutPlanFragment newInstance(int dayOfWeek, boolean isEditable) {
        NewWorkoutPlanFragment fragment = new NewWorkoutPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, dayOfWeek);
        args.putBoolean(ARG_IS_EDITABLE, isEditable);
        fragment.setArguments(args);
        return fragment;
    }

    public static NewWorkoutPlanFragment newInstance(int dayOfWeek, ArrayList<WorkoutRow> workoutRows, boolean isEditable) {
        NewWorkoutPlanFragment fragment = new NewWorkoutPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, dayOfWeek);
        args.putParcelableArrayList(ARG_LIST, workoutRows);
        args.putBoolean(ARG_IS_EDITABLE, isEditable);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDayOfWeek = getArguments().getInt(ARG_PARAM1);
            mIsEditable = getArguments().getBoolean(ARG_IS_EDITABLE);
            if (getArguments().getParcelableArrayList(ARG_LIST) != null) {
                mWorkoutRowList = getArguments().getParcelableArrayList(ARG_LIST);
            } else {
                mWorkoutRowList = new ArrayList<>();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName(), "onCreateView: ");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_workout_plan, container, false);


        ListView workoutLV = v.findViewById(R.id.lstMeals);
        adapter = new AdapterWorkoutLV(mWorkoutRowList);

        workoutLV.setAdapter(adapter);

        Button newRowBtn = v.findViewById(R.id.btnNewRow);
        newRowBtn.setVisibility(mIsEditable ? View.VISIBLE : View.GONE);
        newRowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  mWorkoutRowList.add(new WorkoutRow());
                Log.d(getClass().getSimpleName(), "newRowBtn.onClick: ");
                //   adapter.notifyDataSetChanged();
                AddEditWorkoutPlanDialog dialog = new AddEditWorkoutPlanDialog();
                dialog.setTargetFragment(NewWorkoutPlanFragment.this, REQ_WORKOUT_PLAN);
                dialog.show(getFragmentManager(), "");
            }
        });
        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_WORKOUT_PLAN) {
            if (resultCode == MyKeys.RESULT_NEW) {
                WorkoutRow row = data.getParcelableExtra(MyKeys.EXTRA_PARCLABLE_OBJ);
                mWorkoutRowList.add(row);
                adapter.notifyDataSetChanged();
                mListener.createWorkoutPlan(mDayOfWeek, mWorkoutRowList);
            } else if (resultCode == MyKeys.RESULT_EDIT) {
                WorkoutRow row = data.getParcelableExtra(MyKeys.EXTRA_PARCLABLE_OBJ);
                int index = data.getIntExtra(Intent.EXTRA_INDEX, 0);
                mWorkoutRowList.remove(index);
                mWorkoutRowList.add(index, row);
                adapter.notifyDataSetChanged();
                mListener.createWorkoutPlan(mDayOfWeek, mWorkoutRowList);

            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void createWorkoutPlan(int dayOfWeek, List<WorkoutRow> workoutRowList);
    }

    private class AdapterWorkoutLV extends BaseAdapter implements WorkoutRow.OnWorkoutRowEventListener {
        final List<WorkoutRow> workoutRowList;
        private static final String TAG = "AdapterWorkoutLV";

        public AdapterWorkoutLV(List<WorkoutRow> workoutRowList) {
            this.workoutRowList = workoutRowList;
        }

        @Override
        public int getCount() {
            return workoutRowList.size();
        }

        @Override
        public WorkoutRow getItem(int i) {
            return workoutRowList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return workoutRowList.get(i).getGroupId();
        }

        @Override
        public View getView(final int i, View v, ViewGroup viewGroup) {
            /*    if (convertView == null) {
        PlanetSpinnerItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.planet_spinner_item, parent, false);

        holder = new PlanetViewHolder(itemBinding);
        holder.view = itemBinding.getRoot();
        holder.view.setTag(holder);
    }
    else {
        holder = (PlanetViewHolder) convertView.getTag();
    }*/
            ItemNewWorkoutBinding bind = DataBindingUtil.getBinding(viewGroup);
            if (bind == null) {

                bind = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_new_workout, viewGroup, false);


            }
            bind.setWorkoutRow(workoutRowList.get(i));
            bind.setEventListener(this);
            bind.setIndex(i);
            v = bind.getRoot();


            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.push_left_in);
            animation.setDuration(200);
            v.startAnimation(animation);
            animation = null;

            ImageButton editBtn = v.findViewById(R.id.btnShowTutorial);
            editBtn.setOnClickListener(view -> {
                AddEditWorkoutPlanDialog dialog = AddEditWorkoutPlanDialog.newInstance(i, mWorkoutRowList.get(i));
                dialog.setTargetFragment(NewWorkoutPlanFragment.this, REQ_WORKOUT_PLAN);
                dialog.show(getFragmentManager(), "edit");
            });
            return v;
        }

        @Override
        public void onClick(int index) {
           // mListener.updateTimesUi(workoutRow);
            Log.d(TAG, "getView: v.setOnClickListener");
        }
    }


}

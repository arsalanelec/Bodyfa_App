package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.arsalan.mygym.activities.NewWorkoutPlanActivity;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.databinding.ItemNewWorkoutBinding;
import com.example.arsalan.mygym.dialog.TutorialVideoListDialog;
import com.example.arsalan.mygym.models.SelectableRow;
import com.example.arsalan.mygym.models.WorkoutRow;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;


public class WorkoutPlanFragment extends Fragment {
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
    private AdapterWorkoutLV adapter;
    private ListView workoutLV;
    private CountDownTimer countOneItem;
    private static final String TAG = "WorkoutPlanFragment";
    private MutableLiveData<NewWorkoutPlanActivity.CurrentPlayPauseFragment> mPlayPause;
    private long mSavedTime = 0;
    private boolean isPaused = false;

    public WorkoutPlanFragment() {
        // Required empty public constructor
    }


    public static WorkoutPlanFragment newInstance(int dayOfWeek) {
        WorkoutPlanFragment fragment = new WorkoutPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, dayOfWeek);
        fragment.setArguments(args);
        return fragment;
    }

    public static WorkoutPlanFragment newInstance(int dayOfWeek, ArrayList<WorkoutRow> workoutRows) {
        WorkoutPlanFragment fragment = new WorkoutPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, dayOfWeek);
        args.putParcelableArrayList(ARG_LIST, workoutRows);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDayOfWeek = getArguments().getInt(ARG_PARAM1);
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
        View v = inflater.inflate(R.layout.fragment_workout_plan, container, false);

        workoutLV = v.findViewById(R.id.lstMeals);
        adapter = new AdapterWorkoutLV(mWorkoutRowList);

        workoutLV.setAdapter(adapter);
        mPlayPause = new MutableLiveData<>();

        v.setRotation(180);
        return v;
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
        Log.d(TAG, "onDetach:    ");
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        //    void createWorkoutPlan(int dayOfWeek, List<WorkoutRow> workoutRowList);
        void updateTimesUi(WorkoutRow workoutPlanRow, long itemTime, long remainedItemTime, long totalTime, long remainedTotalTime, int dayOfWeek);

        void hasNextPrev(boolean hasNext, boolean hasPrev);


        void setCurrentWorkoutDay(int currentWorkoutDay);

        MutableLiveData<NewWorkoutPlanActivity.CurrentPlayPauseFragment> getPlayPause();

        MutableLiveData<NewWorkoutPlanActivity.ForwardBackward> goForwardBackward();

    }

    private class AdapterWorkoutLV extends BaseAdapter implements WorkoutRow.OnWorkoutRowEventListener {
        private static final String TAG = "AdapterWorkoutLV";
        List<WorkoutRow> workoutRowList;
        List<SelectableRow> selectableRows = new ArrayList<>();

        public AdapterWorkoutLV(List<WorkoutRow> workoutRowList) {
            this.workoutRowList = workoutRowList;
            for (WorkoutRow w : workoutRowList) {
                selectableRows.add(new SelectableRow(false));
            }
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
            ItemNewWorkoutBinding bind = DataBindingUtil.getBinding(viewGroup);
            if (bind == null) {
                bind = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_new_workout, viewGroup, false);
            }
            bind.setWorkoutRow(workoutRowList.get(i));
            bind.setEventListener(this);
            bind.setIndex(i);
            bind.setSelectable(selectableRows.get(i));
            bind.btnShowTutorial.setImageResource(R.drawable.ic_video_library_24dp);
            v = bind.getRoot();
            Animation animation = null;
            animation = AnimationUtils.loadAnimation(getContext(), R.anim.push_left_in);
            animation.setDuration(200);
            v.startAnimation(animation);
            animation = null;

            ImageButton showTutorial = v.findViewById(R.id.btnShowTutorial);
            showTutorial.setOnClickListener(view -> {
                TutorialVideoListDialog dialog = TutorialVideoListDialog.newInstance(getItem(i).getWorkoutId());
                dialog.show(getFragmentManager(), "");
            });
            return v;
        }

        public void selectFirstRow() {
            if (workoutRowList.size() > 0)
                onClick(workoutRowList.get(0), 0);
        }

        @Override
        public void onClick(WorkoutRow workoutRow, int index) {
            mListener.setCurrentWorkoutDay(mDayOfWeek);
            mListener.goForwardBackward().removeObservers(WorkoutPlanFragment.this);
            mListener.goForwardBackward().observe(WorkoutPlanFragment.this, forwardBackward -> {
                if (forwardBackward.getDay() == mDayOfWeek && forwardBackward.getStat() == NewWorkoutPlanActivity.ForwardBackward.FORWARD) {
                    if (index < getCount() - 1) {
                        onClick(workoutRow, index + 1);
                        return;
                    }
                } else if (forwardBackward.getDay() == mDayOfWeek && forwardBackward.getStat() == NewWorkoutPlanActivity.ForwardBackward.BACKWARD) {
                    if (index > 0) {
                        onClick(workoutRow, index - 1);
                        return;
                    }
                }
            });
            mListener.hasNextPrev(index < workoutRowList.size() - 1, index > 0);
            isPaused = false;
            for (SelectableRow selectableRow : selectableRows) {
                selectableRow.setSelected(false);
            }
            selectableRows.get(index).setSelected(true);
            notifyDataSetChanged();
            int totalTime = 0;
            for (int j = index; j < workoutRowList.size(); j++) {
                WorkoutRow w = workoutRowList.get(j);
                totalTime += (w.getRep() * w.getSet() * w.getSetDuration() + w.getSet() * (w.getRest() - 1));
                if (j < workoutRowList.size() - 1) {
                    totalTime += 60;
                }
            }
            int finalTotalTime = totalTime;

            Log.d(TAG, "onClick: totalTime:" + totalTime);
            int itemTimes = workoutRow.getRep() * workoutRow.getSet() * workoutRow.getSetDuration() + workoutRow.getSet() * (workoutRow.getRest() - 1);
            //mListener.setPlayPause(mPlayPause);
            mPlayPause = mListener.getPlayPause();
            mSavedTime = itemTimes * 1000;
            mListener.updateTimesUi(workoutRow, itemTimes * 1000, finalTotalTime * 1000, finalTotalTime * 1000, (finalTotalTime * 1000), mDayOfWeek);
            if (countOneItem != null) countOneItem.cancel();
            mPlayPause.removeObservers(WorkoutPlanFragment.this);
            mPlayPause.observe(WorkoutPlanFragment.this, b -> {
                Log.d(TAG, "onCreateView: observed:" + b + " dayofweek:" + mDayOfWeek);
                if (b.getDayOfWeek() != mDayOfWeek) {
                    isPaused = true;
                    mPlayPause.removeObservers(WorkoutPlanFragment.this);
                    if (countOneItem != null) countOneItem.cancel();
                }
                if (b.isPaused()) {
                    isPaused = true;
                } else {
                    isPaused = false;
                    countOneItem = new CountDownTimer(mSavedTime, 1000) {
                        @Override
                        public void onTick(long millis) {
                            if (isPaused) cancel();
                            mSavedTime = millis;
                            if (mListener != null)
                                mListener.updateTimesUi(workoutRow, itemTimes * 1000, millis, finalTotalTime * 1000, (finalTotalTime * 1000) - (itemTimes * 1000 - millis), mDayOfWeek);
                            else Log.d(TAG, "onTick: mlistener is null");
                        }

                        @Override
                        public void onFinish() {
                            if (index < workoutRowList.size() - 1) {
                                onClick(getItem(index + 1), index + 1);
                            }
                        }
                    }.start();

                }
            });


           /* new CountDownTimer(itemTimes * 1000, 1000) {
                @Override
                public void onTick(long millis) {
                    if (isPaused) cancel();
                    mSavedTime = millis;

                    if (mListener != null)
                        mListener.updateTimesUi(workoutRow, itemTimes * 1000, millis, finalTotalTime * 1000, (finalTotalTime * 1000) - (itemTimes * 1000 - millis), mDayOfWeek);
                }

                @Override
                public void onFinish() {
                    if (index < workoutRowList.size() - 1) {
                        onClick(getItem(index + 1), index + 1);
                    }
                }
            }.start();*/

            Log.d(TAG, "getView: v.setOnClickListener");
        }
    }


}

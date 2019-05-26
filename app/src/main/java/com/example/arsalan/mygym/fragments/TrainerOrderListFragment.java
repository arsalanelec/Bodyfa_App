package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.databinding.FragmentTrainerOrderListBinding;
import com.example.arsalan.mygym.databinding.RowTrainerMembershipReqBinding;
import com.example.arsalan.mygym.databinding.RowWorkoutPlanReqBinding;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.dialog.TrainerWorkoutPlanListToSendDialog;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.TrainerAthlete;
import com.example.arsalan.mygym.models.WorkoutPlanReq;
import com.example.arsalan.mygym.viewModels.AthleteListViewModel;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TrainerWorkoutPlanReqVm;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrainerOrderListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrainerOrderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainerOrderListFragment extends Fragment implements Injectable, SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "TrainerOrderListFragmen";
    @Inject
    MyViewModelFactory mFactory;

    private long mTrainerId;

    private OnFragmentInteractionListener mListener;
    private List<WorkoutPlanReq> mRequestList;
    private List<TrainerAthlete> mAthletes;
    private ExtendViewAdapter mAdapter;
    private FragmentTrainerOrderListBinding bind;
    private TrainerWorkoutPlanReqVm workoutPlanReqVm;
    private AthleteListViewModel athletesViewModel;

    public TrainerOrderListFragment() {
        // Required empty public constructor
        mRequestList = new ArrayList<>();
        mAthletes=new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param trainerId Parameter 1.
     * @return A new instance of fragment TrainerOrderListFragment.
     */
    public static TrainerOrderListFragment newInstance(long trainerId) {
        TrainerOrderListFragment fragment = new TrainerOrderListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, trainerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTrainerId = getArguments().getLong(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_trainer_order_list, container, false);


        mAdapter = new ExtendViewAdapter(mRequestList,mAthletes);
        bind.exListView.setAdapter(mAdapter);
        bind.swipeLay.setOnRefreshListener(this);
        bind.swipeLay.setRefreshing(true);
        View v = bind.getRoot();
        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        workoutPlanReqVm = ViewModelProviders.of(this, mFactory).get(TrainerWorkoutPlanReqVm.class);
        workoutPlanReqVm.init(mTrainerId);
        workoutPlanReqVm.getWorkoutPlanListLv().observe(this, workoutPlanReqs -> {
            Log.d(TAG, "onActivityCreated: workoutPlanReqs Changed listSize:" + workoutPlanReqs.size());
            mRequestList.removeAll(mRequestList);
            if (workoutPlanReqs != null && workoutPlanReqs.size() > 0) {
                mRequestList.addAll(workoutPlanReqs);
            }
            mAdapter.notifyDataSetChanged();

            bind.exListView.expandGroup(0);
            bind.swipeLay.setRefreshing(false);
        });

        athletesViewModel = ViewModelProviders.of(this, mFactory).get(AthleteListViewModel.class);
        athletesViewModel.init(mTrainerId,false);
        athletesViewModel.getAthleteList().observe(this, athletes -> {
            Log.d(TAG, "onActivityCreated: athletesViewModel Changed listSize:" + athletes.size());
            mAthletes.removeAll(mAthletes);
            if (athletes != null && athletes.size() > 0) {
                mAthletes.addAll(athletes);
            }
            mAdapter.notifyDataSetChanged();

            bind.exListView.expandGroup(2);
            bind.swipeLay.setRefreshing(false);
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
    public void onRefresh() {
        workoutPlanReqVm.init(mTrainerId);
    }

    public interface OnRequestRowClickListener {
        void onCancelClick(long requestId, int type);

        void onSubmitClick(long requestId, long trainerId, long athleteId, String athleteName, String athleteThumbUrl, int type);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onWorkoutPlanCancelRequest(long planId);
    }

    private class ExtendViewAdapter extends BaseExpandableListAdapter implements OnRequestRowClickListener {
        private static final int TYPE_WORKOUT = 0;
        private static final int TYPE_MEAL = 1;
        private static final int TYPE_MEMBERSHIP = 2;
        private static final String TAG = "ExtendViewAdapter";
        final String[] groupTitles = {"سفارشات برنامه ورزشی", "سفارشات برنامه تغذیه", "سفارشات اشتراک"};
        List<WorkoutPlanReq> workoutPlanReqList;
        List<TrainerAthlete> athletes;

        public ExtendViewAdapter(List<WorkoutPlanReq> workoutPlanReqList, List<TrainerAthlete> athleteList) {
            this.workoutPlanReqList = workoutPlanReqList;
            athletes=athleteList;
        }

        @Override
        public int getGroupCount() {
            return groupTitles.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
           switch (groupPosition) {
               case 0:    //workout
                return workoutPlanReqList.size();
               case 2:    //membership
                   return athletes.size();
            }
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
           switch (groupPosition) {
               case 0:    //workout plan
                return workoutPlanReqList.get(childPosition);
               case 2:    //membership
                   return athletes.get(childPosition);

            }
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
           switch (groupPosition ) {
               case 0:    //workout
                   return workoutPlanReqList.get(childPosition).getId();
               case 2:    //membership
                   return athletes.get(childPosition).getId();
            }
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.parent_header_simple_text, parent, false);
            }
            TextView titleTv = convertView.findViewById(R.id.txtTitle);
            titleTv.setText(groupTitles[groupPosition]);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (getChildrenCount(groupPosition) == 0) {
                convertView = getLayoutInflater().inflate(R.layout.no_data_view, parent, false);
                return convertView;
            }
            switch (groupPosition) {
                case 0:    //WorkoutPlan
                {
                    RowWorkoutPlanReqBinding bind;
                    if (convertView == null) {
                        bind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.row_workout_plan_req, parent, false);
                        convertView = bind.getRoot();
                    } else {
                        bind = ((RowWorkoutPlanReqBinding) convertView.getTag());
                    }
                    WorkoutPlanReq temp = (WorkoutPlanReq) getChild(groupPosition, childPosition);
                    Log.d(TAG, "getChildView: id:" + temp.getId() + " date" + temp.getRequestDateEnTs());
                    //bind data
                    bind.setWorkoutReq(temp);
                    bind.setOnDeleteClick(this);
                    bind.setTypeOfRequest(TYPE_WORKOUT);

                    //Load Thumbnails
                    Glide.with(getContext())
                            .load(MyConst.BASE_CONTENT_URL + temp.getAthleteThumbUrl())
                            .apply(new RequestOptions().placeholder(R.drawable.bodybuilder_place_holder).circleCrop())
                            .apply(RequestOptions.circleCropTransform())
                            .into(bind.imgAvatar);

                    convertView.setTag(bind);
                }
                break;
                case 2:    //Membership Request
                {
                    RowTrainerMembershipReqBinding bind;
                    if (convertView == null) {
                        bind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.row_trainer_membership_req, parent, false);
                        convertView = bind.getRoot();
                    } else {
                        bind = ((RowTrainerMembershipReqBinding) convertView.getTag());
                    }
                    TrainerAthlete athlete = (TrainerAthlete) getChild(groupPosition, childPosition);
                    //bind data
                    bind.setAthlete(athlete);
                    bind.setOnDeleteClick(this);
                    bind.setTypeOfRequest(TYPE_MEMBERSHIP);
                    //Load Thumbnails
                    Glide.with(getContext())
                            .load(MyConst.BASE_CONTENT_URL + athlete.getAthleteThumbPicture())
                            .apply(new RequestOptions().placeholder(R.drawable.bodybuilder_place_holder).circleCrop())
                            .apply(RequestOptions.circleCropTransform())
                            .into(bind.imgAvatar);

                    convertView.setTag(bind);
                }
                break;
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }


        @Override
        public void onGroupExpanded(int groupPosition) {

        }

        @Override
        public void onGroupCollapsed(int groupPosition) {

        }

        @Override
        public void onCancelClick(long requestId, int type) {
            switch (type) {
                case TYPE_WORKOUT:
                    workoutPlanReqVm.cancelRequest(requestId).observe(TrainerOrderListFragment.this, status -> {
                        if (status == 1) {
                            Toast.makeText(getContext(), "حذف انجام شد!", Toast.LENGTH_LONG).show();
                        }
                    });
                    mListener.onWorkoutPlanCancelRequest(requestId);
                    break;
                case TYPE_MEMBERSHIP:
                    athletesViewModel.cancelRequest(requestId).observe(TrainerOrderListFragment.this,status->{
                        if (status == 1) {
                            Toast.makeText(getContext(), "حذف انجام شد!", Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
            }

        }

        @Override
        public void onSubmitClick(long requestId, long trainerId, long athleteId, String athleteName, String athleteThumbUrl, int type) {
            switch (type) {
                case TYPE_WORKOUT:
                    TrainerWorkoutPlanListToSendDialog dialog = TrainerWorkoutPlanListToSendDialog.newInstance(
                            requestId,
                            trainerId,
                            athleteId,
                            athleteName,
                            athleteThumbUrl);
                    Log.d(TAG, "onSubmitClick: athleteId:" + athleteId);
                    dialog.show(getFragmentManager(), "");
                    break;
                case TYPE_MEMBERSHIP:
                    athletesViewModel.acceptRequest(requestId);
                    Toast.makeText(getContext(), R.string.trainer_athlete_membership_accepted, Toast.LENGTH_SHORT).show();
                    break;
            }
        }


    }
}

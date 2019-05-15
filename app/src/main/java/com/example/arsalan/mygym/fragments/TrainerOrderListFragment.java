package com.example.arsalan.mygym.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.databinding.FragmentTrainerOrderListBinding;
import com.example.arsalan.mygym.databinding.RowWorkoutPlanReqBinding;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.dialog.TrainerWorkoutPlanListToSendDialog;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.WorkoutPlan;
import com.example.arsalan.mygym.models.WorkoutPlanReq;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TrainerWorkoutPlanReqVm;

import org.bytedeco.javacpp.presets.opencv_core;

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

    @Inject
    MyViewModelFactory mFactory;

    private long mTrainerId;

    private OnFragmentInteractionListener mListener;
    private List<WorkoutPlanReq> mRequestList;
    private ExtendViewAdapter mAdapter;
    private FragmentTrainerOrderListBinding bind;
    private TrainerWorkoutPlanReqVm viewModel;

    public TrainerOrderListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param trainerId Parameter 1.
     * @return A new instance of fragment TrainerOrderListFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        mRequestList = new ArrayList<>();

        mAdapter = new ExtendViewAdapter(mRequestList);
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
         viewModel = ViewModelProviders.of(this, mFactory).get(TrainerWorkoutPlanReqVm.class);
        viewModel.init(mTrainerId);
        viewModel.getWorkoutPlanListLv().observe(this, workoutPlanReqs -> {
            mRequestList.removeAll(mRequestList);
            if (workoutPlanReqs != null && workoutPlanReqs.size() > 0) {
                mRequestList.addAll(workoutPlanReqs);
            }
            mAdapter.notifyDataSetChanged();

            bind.exListView.expandGroup(0);
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
        viewModel.init(mTrainerId);
    }

    private class ExtendViewAdapter extends BaseExpandableListAdapter implements OnRequestRowClickListener {
        List<WorkoutPlanReq> workoutPlanReqList;
        final String[] groupTitles = {"سفارشات برنامه ورزشی", "سفارشات برنامه تغذیه", "سفارشات اشتراک"};
        private static final String TAG = "ExtendViewAdapter";

        public ExtendViewAdapter(List<WorkoutPlanReq> workoutPlanReqList) {
            this.workoutPlanReqList = workoutPlanReqList;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getGroupCount() {
            return groupTitles.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (groupPosition == 0) {
                return workoutPlanReqList.size();
            }
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public WorkoutPlanReq getChild(int groupPosition, int childPosition) {
            if (groupPosition == 0) {
                return workoutPlanReqList.get(childPosition);
            }
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            if (groupPosition == 0) {
                return workoutPlanReqList.get(childPosition).getId();
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
            if (groupPosition == 0) {
                RowWorkoutPlanReqBinding bind;
                if (convertView == null) {
                    bind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.row_workout_plan_req, parent, false);
                    convertView = bind.getRoot();
                } else {
                    bind = ((RowWorkoutPlanReqBinding) convertView.getTag());
                }
                WorkoutPlanReq temp = getChild(groupPosition, childPosition);
                Log.d(TAG, "getChildView: id:" + temp.getId() + " date" + temp.getRequestDateEnTs());
                //bind data
                bind.setWorkoutReq(temp);
                bind.setOnDeleteClick(this);
                //Load Thumbnails
                Glide.with(getContext())
                        .load(MyConst.BASE_CONTENT_URL + temp.getAthleteThumbUrl())
                        .apply(new RequestOptions().placeholder(R.drawable.bodybuilder_place_holder).circleCrop())
                        .apply(RequestOptions.circleCropTransform())
                        .into(bind.imgAvatar);

                convertView.setTag(bind);
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {

        }

        @Override
        public void onGroupCollapsed(int groupPosition) {

        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return 0;
        }

        @Override
        public void onDeleteClick(int requestId) {
            viewModel.cancelRequest(requestId).observe(TrainerOrderListFragment.this, status -> {
                if(status==1){
                    Toast.makeText(getContext(), "حذف انجام شد!", Toast.LENGTH_LONG).show();
                }
            });
            mListener.onWorkoutPlanCancelRequest(requestId);
        }

        @Override
        public void onSubmitClick(int requestId, long trainerId, long athleteId, String athleteName, String athleteThumbUrl) {
            TrainerWorkoutPlanListToSendDialog dialog=TrainerWorkoutPlanListToSendDialog.newInstance(requestId,trainerId,athleteId,athleteName,athleteThumbUrl);
            dialog.show(getFragmentManager(),"");
        }


    }
    public interface    OnRequestRowClickListener{
        void onDeleteClick(int requestId);
        void onSubmitClick( int requestId,long trainerId,long athleteId,String athleteName,String athleteThumbUrl);
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
        void onWorkoutPlanCancelRequest(int planId);
    }
}

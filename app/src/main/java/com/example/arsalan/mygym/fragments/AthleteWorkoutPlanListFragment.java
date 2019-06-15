package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterAthleteWorkoutPlanList;
import com.example.arsalan.mygym.databinding.ItemAthleteWorkoutPlanRequestBinding;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.dialog.RequestWorkoutPlanDialog;
import com.example.arsalan.mygym.dialog.TrainerListDialog;
import com.example.arsalan.mygym.models.RetWorkoutPlan;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.models.WorkoutPlan;
import com.example.arsalan.mygym.models.WorkoutPlanReq;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.viewModels.AthleteWorkoutPlanListViewModel;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TrainerWorkoutPlanReqVm;
import com.example.arsalan.room.WorkoutPlanDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AthleteWorkoutPlanListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AthleteWorkoutPlanListFragment#newInstance} mFactory method to
 * create an instance of this fragment.
 */
public class AthleteWorkoutPlanListFragment extends Fragment implements Injectable {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final int REQ_SELECT_TRAINER = 1;
    private static final String TAG = "AthleteWorkoutPlanListF";
    @Inject
    MyViewModelFactory mFactory;
    @Inject
    WorkoutPlanDao workoutPlanDao;
    private User mUser;
    private AthleteWorkoutPlanListViewModel workoutPlanListViewModel;
    private OnFragmentInteractionListener mListener;
    private ListView mPlanListView;
    private ArrayList<WorkoutPlan> mWorkoutPlanList;
    private AdapterAthleteWorkoutPlanList mAdapter;
    private View nothingToseeView;
    private TrainerWorkoutPlanReqVm workoutPlanReqVm;
    private List<WorkoutPlanReq> mRequestList;
    private WorkoutPlanRequestAdapter mWorkoutReqAdapter;

    public AthleteWorkoutPlanListFragment() {
        // Required empty public constructor
    }


    public static AthleteWorkoutPlanListFragment newInstance(User user) {
        AthleteWorkoutPlanListFragment fragment = new AthleteWorkoutPlanListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, user);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_athlete_workout_plan_list, container, false);
        TextView title = v.findViewById(R.id.txtTitle);
        title.setText(getString(R.string.workout_plan_list));
        mPlanListView = v.findViewById(R.id.lst_plan);
        mWorkoutPlanList = new ArrayList<>();

        mAdapter = new AdapterAthleteWorkoutPlanList(getContext(), mWorkoutPlanList, new AdapterAthleteWorkoutPlanList.OnItemClickListener() {
            @Override
            public void onItemShowClick(WorkoutPlan workoutPlan, int position) {
                getAthleteWorkoutPlanWeb(workoutPlan.getAthleteWorkoutPlanId(), new OnGetPlanListener() {
                    @Override
                    public void onGetPlan(long planId, String title, String body) {
                        mListener.addEditWorkoutPlan(planId, title, body, false);

                    }
                });

            }
        });

        mPlanListView.setAdapter(mAdapter);
        FloatingActionButton addPlanBtn = v.findViewById(R.id.fab_add_plan);
        addPlanBtn.setOnClickListener(b -> {
            TrainerListDialog dialog = new TrainerListDialog();
            dialog.setTargetFragment(AthleteWorkoutPlanListFragment.this, REQ_SELECT_TRAINER);
            dialog.show(getFragmentManager(), "");
        });
        mRequestList = new ArrayList<>();
        mWorkoutReqAdapter = new WorkoutPlanRequestAdapter(mRequestList);
        ListView workoutRequestLst = v.findViewById(R.id.lst_request);
        workoutRequestLst.setAdapter(mWorkoutReqAdapter);
        nothingToseeView = v.findViewById(R.id.layNothingToShow);
        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        workoutPlanListViewModel = ViewModelProviders.of(this, mFactory).get(AthleteWorkoutPlanListViewModel.class);
        workoutPlanListViewModel.init("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mUser.getId());
        workoutPlanListViewModel.getWorkoutPlanItemList().observe(this, workoutPlans -> {
            Log.d(getClass().getSimpleName(), "onActivityCreated observe: workoutPlan cnt:" + workoutPlans.size());
            mWorkoutPlanList.removeAll(mWorkoutPlanList);
            mWorkoutPlanList.addAll(workoutPlans);
            mAdapter.notifyDataSetChanged();
            if (workoutPlans.size() > 0) {
                nothingToseeView.setVisibility(View.GONE);
            } else {
                nothingToseeView.setVisibility(View.VISIBLE);
            }

            // waitingFL.setVisibility(View.GONE);
        });

        workoutPlanReqVm = ViewModelProviders.of(this, mFactory).get(TrainerWorkoutPlanReqVm.class);
        workoutPlanReqVm.initAll(mUser.getId());
        workoutPlanReqVm.getWorkoutPlanListLv().observe(this, workoutPlanReqs -> {
            Log.d(TAG, "onActivityCreated: workoutPlanReqs.size:"+workoutPlanReqs.size());
            mRequestList.clear();
            if (workoutPlanReqs != null && workoutPlanReqs.size() > 0) {
                mRequestList.addAll(workoutPlanReqs);
            }
            mWorkoutReqAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_SELECT_TRAINER) {    //Result of Selected Trainer from dialog
            Trainer trainer = data.getParcelableExtra(MyKeys.EXTRA_OBJ_TRAINER);
            RequestWorkoutPlanDialog dialog = RequestWorkoutPlanDialog.newInstance(mUser.getId(), trainer.getId(), trainer.getWorkoutPlanPrice());
            dialog.show(getFragmentManager(), "");
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void getAthleteWorkoutPlanWeb(final long planId, final OnGetPlanListener onGetPlanListener) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Log.d("getView", "onClick: id:" + planId);
        Log.d(getClass().getSimpleName(), "getAthleteWorkoutPlanWeb: در حال دریافت برنامه غذایی...");
        Call<RetWorkoutPlan> call = apiService.getAthleteWorkoutPlan("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), planId);
        call.enqueue(new Callback<RetWorkoutPlan>() {
            @Override
            public void onResponse(Call<RetWorkoutPlan> call, Response<RetWorkoutPlan> response) {


                if (response.isSuccessful()) {
                    onGetPlanListener.onGetPlan(planId, response.body().getRecord().getTitle(), response.body().getRecord().getDescription());
                    workoutPlanDao.updatePlan(response.body().getRecord());
                } else {
                    WorkoutPlan workoutPlan = workoutPlanDao.getWorkoutPlanByAthletePlanId(planId);
                    if (workoutPlan != null)
                        onGetPlanListener.onGetPlan(planId, workoutPlan.getTitle(), workoutPlan.getDescription());
                }
            }

            @Override
            public void onFailure(Call<RetWorkoutPlan> call, Throwable t) {

                WorkoutPlan workoutPlan = workoutPlanDao.getWorkoutPlanByAthletePlanId(planId);
                if (workoutPlan != null)
                    onGetPlanListener.onGetPlan(planId, workoutPlan.getTitle(), workoutPlan.getDescription());
            }
        });

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
        void addEditWorkoutPlan(long planId, String planName, String planBody, boolean editMode);

    }

    private interface OnGetPlanListener {
        void onGetPlan(long planId, String title, String body);
    }

    private class WorkoutPlanRequestAdapter extends BaseAdapter {
        List<WorkoutPlanReq> workoutPlanReqList;

        public WorkoutPlanRequestAdapter(List<WorkoutPlanReq> workoutPlanReqList) {

            this.workoutPlanReqList = workoutPlanReqList;
        }

        @Override
        public int getCount() {
            return workoutPlanReqList.size();
        }

        @Override
        public WorkoutPlanReq getItem(int position) {
            return workoutPlanReqList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return workoutPlanReqList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemAthleteWorkoutPlanRequestBinding binding;
            if (convertView != null) {
                binding = (ItemAthleteWorkoutPlanRequestBinding) convertView.getTag();
                binding.setWorkoutReq(getItem(position));
            } else {
                binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_athlete_workout_plan_request, parent, false);
                convertView = binding.getRoot();
                convertView.setTag(binding);
            }

            return convertView;
        }
    }


}

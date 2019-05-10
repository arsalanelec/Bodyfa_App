package com.example.arsalan.mygym.fragments;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.models.WorkoutPlan;
import com.example.arsalan.mygym.models.RetWorkoutPlan;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterAthleteWorkoutPlanList;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.viewModels.AthleteWorkoutPlanListViewModel;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.room.WorkoutPlanDao;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AthleteWorkoutPlanListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AthleteWorkoutPlanListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AthleteWorkoutPlanListFragment extends Fragment implements Injectable{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    private User mUser;

    private AthleteWorkoutPlanListViewModel workoutPlanListViewModel;

    @Inject
    MyViewModelFactory factory;

    @Inject
    WorkoutPlanDao workoutPlanDao;

    private OnFragmentInteractionListener mListener;
    private ListView mPlanListView;
    private ArrayList<WorkoutPlan> mWorkoutPlanList;
    private AdapterAthleteWorkoutPlanList mAdapter;

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
        View v = inflater.inflate(R.layout.fragment_athlete_meal_plan_list, container, false);
        TextView title = v.findViewById(R.id.txtTitle);
        title.setText(getString(R.string.workout_plan_list));

         mPlanListView = v.findViewById(R.id.lstPlan);
         mWorkoutPlanList = new ArrayList<>();

        mAdapter = new AdapterAthleteWorkoutPlanList(getContext(), mWorkoutPlanList, new AdapterAthleteWorkoutPlanList.OnItemClickListener() {
            @Override
            public void onItemShowClick(WorkoutPlan workoutPlan, int position) {
                getAthleteWorkoutPlanWeb(workoutPlan.getAthleteWorkoutPlanId(), new OnGetPlanListener() {
                    @Override
                    public void onGetPlan(long planId, String title, String body) {
                        mListener.addEditWorkoutPlan(planId, title,body, false);

                    }
                });

            }
        });
        mPlanListView.setAdapter(mAdapter);
        v.setRotation(180);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        workoutPlanListViewModel = ViewModelProviders.of(this, factory).get(AthleteWorkoutPlanListViewModel.class);
        workoutPlanListViewModel.init("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mUser.getId());
        workoutPlanListViewModel.getWorkoutPlanItemList().observe(this, workoutPlans -> {
            Log.d(getClass().getSimpleName(), "onActivityCreated observe: workoutPlan cnt:" + workoutPlans.size());
            mWorkoutPlanList.removeAll(mWorkoutPlanList);
            mWorkoutPlanList.addAll(workoutPlans);
            mAdapter.notifyDataSetChanged();

            // waitingFL.setVisibility(View.GONE);
        });

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



    /*private void getWorkoutPlanListWeb(long userId, final ArrayList<WorkoutPlan> workoutPlans, final AdapterAthleteWorkoutPlanList adapter) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Log.d(getClass().getSimpleName(), "getWorkoutPlanListWeb: در حال دریافت لیست برنامه غذایی");
        Call<RetWorkoutPlanList> call = apiService.getAthleteWorkoutPlanList("Bearer " + ((MyApplication2) getActivity().getApplication()).getCurrentToken().getToken(), userId);
        call.enqueue(new Callback<RetWorkoutPlanList>() {
            @Override
            public void onResponse(Call<RetWorkoutPlanList> call, Response<RetWorkoutPlanList> response) {
                if(response!=null&&response.isSuccessful()) {
                    workoutPlans.removeAll(workoutPlans);
                    workoutPlans.addAll(response.body().getRecords());
                    adapter.notifyDataSetChanged();
                    Log.d(getClass().getSimpleName(), "onResponse: ");
                }
            }

            @Override
            public void onFailure(Call<RetWorkoutPlanList> call, Throwable t) {
                Log.d(getClass().getSimpleName(), "onFailed");
            }
        });
    }*/

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



}

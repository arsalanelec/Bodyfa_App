package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterAthleteMealPlanList;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.MealPlan;
import com.example.arsalan.mygym.models.RetMealPlan;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.viewModels.AthleteMealPlanListViewModel;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.room.MealPlanDao;
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
 * {@link AthleteMealPlanListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AthleteMealPlanListFragment#newInstance} mFactory method to
 * create an instance of this fragment.
 */
public class AthleteMealPlanListFragment extends Fragment implements Injectable {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    @Inject
    MyViewModelFactory factory;
    @Inject
    MealPlanDao mealPlanDao;
    private User mUser;
    private OnFragmentInteractionListener mListener;
    private AthleteMealPlanListViewModel mealPlanListViewModel;
    private List<MealPlan> mMealPlanList;
    private AdapterAthleteMealPlanList mAdapter;
    private ListView planList;
    private View mNothingToSee;

    public AthleteMealPlanListFragment() {
        // Required empty public constructor
    }


    public static AthleteMealPlanListFragment newInstance(User user) {
        AthleteMealPlanListFragment fragment = new AthleteMealPlanListFragment();
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
        View v = inflater.inflate(R.layout.fragment_athlete_plan_list, container, false);
        TextView title = v.findViewById(R.id.txtTitle);
        title.setText(getString(R.string.meal_plan_list));
        planList = v.findViewById(R.id.lst_plan);
        mMealPlanList = new ArrayList<>();
        mNothingToSee = v.findViewById(R.id.layNothingToShow);

        mAdapter = new AdapterAthleteMealPlanList(getContext(), mMealPlanList, mealPlan -> getAthleteMealPlanWeb(mealPlan.getAthleteMealPlanId(), (planId, title1, body) -> mListener.addEditMealPlan(planId, title1, body, false)));
        FloatingActionButton floatingActionButton = v.findViewById(R.id.fab_add_plan);
        floatingActionButton.setVisibility(View.GONE);
        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mealPlanListViewModel = ViewModelProviders.of(this, factory).get(AthleteMealPlanListViewModel.class);
        mealPlanListViewModel.init("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mUser.getId());
        mealPlanListViewModel.getMealPlanItemList().observe(this, mealPlans -> {
            mNothingToSee.setVisibility((mealPlans.size() == 0) ? View.VISIBLE : View.GONE);
            Log.d(getClass().getSimpleName(), "onActivityCreated observe: mealPlans cnt:" + mealPlans.size());
            mMealPlanList.clear();
            mMealPlanList.addAll(mealPlans);
            mAdapter.notifyDataSetChanged();
            planList.setAdapter(mAdapter);

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


/*

    private void getMealPlanListWeb(long userId, final ArrayList<MealPlan> mealPlans, final AdapterAthleteMealPlanList adapter) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Log.d(getClass().getSimpleName(), "getMealPlanListWeb: در حال دریافت لیست برنامه غذایی");
        Call<RetMealPlanList> call = apiService.getAthleteMealPlanList("Bearer " + ((MyApplication2) getActivity().getApplication()).getCurrentToken().getToken(), userId);
        call.enqueue(new Callback<RetMealPlanList>() {
            @Override
            public void onResponse(Call<RetMealPlanList> call, Response<RetMealPlanList> response) {
                if(response.isSuccessful()) {
                    mealPlans.removeAll(mealPlans);
                    mealPlans.addAll(response.body().getRecords());
                    adapter.notifyDataSetChanged();
                    Log.d(getClass().getSimpleName(), "onResponse: ");
                }
            }

            @Override
            public void onFailure(Call<RetMealPlanList> call, Throwable t) {
                Log.d(getClass().getSimpleName(), "onFailed");
            }
        });
    }
*/

    private void getAthleteMealPlanWeb(final long planId, final OnGetPlanListener onGetPlanListener) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Log.d("getView", "onClick: id:" + planId);
        Log.d(getClass().getSimpleName(), "getAthleteMealPlanWeb: در حال دریافت برنامه غذایی...");
        Call<RetMealPlan> call = apiService.getAthleteMealPlan("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), planId);
        call.enqueue(new Callback<RetMealPlan>() {
            @Override
            public void onResponse(Call<RetMealPlan> call, Response<RetMealPlan> response) {

                if (response.isSuccessful()) {
                    onGetPlanListener.onGetPlan(planId, response.body().getRecord().getTitle(), response.body().getRecord().getDescription());
                    mealPlanDao.updateMealPlan(response.body().getRecord());
                } else {
                    MealPlan mealPlan = mealPlanDao.getMealPlanByAthletePlanId(planId);
                    if (mealPlan != null)
                        onGetPlanListener.onGetPlan(planId, mealPlan.getTitle(), mealPlan.getDescription());
                }
            }

            @Override
            public void onFailure(Call<RetMealPlan> call, Throwable t) {
                MealPlan mealPlan = mealPlanDao.getMealPlanByAthletePlanId(planId);
                if (mealPlan != null) {
                    onGetPlanListener.onGetPlan(planId, mealPlan.getTitle(), mealPlan.getDescription());
                } else {
                    Log.d(getClass().getSimpleName(), "onFailure: mealPlan is null");
                }

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
        void addEditMealPlan(long planId, String planName, String planBody, boolean editMode);

    }

    private interface OnGetPlanListener {
        void onGetPlan(long planId, String title, String body);
    }


}

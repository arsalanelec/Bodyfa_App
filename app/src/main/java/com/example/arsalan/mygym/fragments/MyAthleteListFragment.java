package com.example.arsalan.mygym.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arsalan.mygym.activities.MessageRoomActivity;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.TrainerAthlete;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterAthletes;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.viewModels.AthleteListViewModel;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.room.UserDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class MyAthleteListFragment extends Fragment implements Injectable{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TRAINER = "param2";
    private static final String ARG_USER = "param1";
    private static final String TAG = "MyAthleteListFragment";

    private List<TrainerAthlete> mAthleteList;
    private OnFragmentInteractionListener mListener;
    private AdapterAthletes mAdapter;

    private Trainer mCurrentTrainer;
    private User mCurrentUser;
    private TextView athleteCnt;
    private View mAthleteContainer;
    private AthleteListViewModel acceptedAthleteListViewModel;

    @Inject
    MyViewModelFactory factory;

    @Inject
    UserDao userDao;
    private View waitingFL;

    public MyAthleteListFragment() {
        // Required empty public constructor
    }


    public static MyAthleteListFragment newInstance(User currentUser, Trainer trainer) {
        MyAthleteListFragment fragment = new MyAthleteListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TRAINER, trainer);
        args.putParcelable(ARG_USER, currentUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentTrainer = getArguments().getParcelable(ARG_TRAINER);
            mCurrentUser = getArguments().getParcelable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_athlete_list, container, false);

        athleteCnt = v.findViewById(R.id.txtAthleteCount);
        athleteCnt.setVisibility(View.INVISIBLE);
        mAthleteContainer = v.findViewById(R.id.container_athlete_profile);
        RecyclerView rv = v.findViewById(R.id.rv_trainers);
        mAthleteList = new ArrayList<>();
        mAdapter = new AdapterAthletes(mAthleteList, new AdapterAthletes.OnItemClickListener() {
            @Override
            public void onItemClick(TrainerAthlete athlete) {
                Log.d(TAG, "onItemClick: athlete id:"+athlete.getAthleteId()+" userName:"+athlete.getAthleteUsername());
                Fragment athleteProfileFragment= AthleteProfileFragment.newInstance(athlete.getAthleteId(),athlete.getAthleteName());
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_athlete_profile,athleteProfileFragment)
                        .commit();
                mAthleteContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSendMessageClicked(TrainerAthlete trainerAthlete) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MessageRoomActivity.class);
                intent.putExtra(MyKeys.EXTRA_USER_ID, mCurrentUser.getId());
                intent.putExtra(MyKeys.EXTRA_PARTY_ID, trainerAthlete.getAthleteId());
                intent.putExtra(MyKeys.EXTRA_PARTY_NAME, trainerAthlete.getAthleteName());
                intent.putExtra(MyKeys.EXTRA_PARTY_THUMB, trainerAthlete.getAthleteThumbPicture());

                startActivity(intent);

            }
        });
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        waitingFL = v.findViewById(R.id.fl_waiting);
        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        acceptedAthleteListViewModel = ViewModelProviders.of(this, factory).get(AthleteListViewModel.class);
        acceptedAthleteListViewModel.init( mCurrentUser.getId(),true);
        acceptedAthleteListViewModel.getAthleteList().observe(this, userList -> {
            Log.d(getClass().getSimpleName(), "onActivityCreated observe: mealPlans cnt:" + userList.size());
            mAthleteList.clear();
            mAthleteList.addAll(userList);
            athleteCnt.setText(getString(R.string.athlete_count, userList.size()));
            athleteCnt.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        waitingFL.setVisibility(View.GONE);
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
    //Pressed return button - returns to the results menu
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            Log.d(TAG, "onKey: back presssed!");
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && mAthleteContainer.getVisibility()==View.VISIBLE){
                mAthleteContainer.setVisibility(View.GONE);
                //your code
                return true;
            }
            return false;
        });
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}

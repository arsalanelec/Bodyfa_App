package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterTrainers;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.CityNState;
import com.example.arsalan.mygym.models.Province;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TrainerListViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class TrainerListFragment extends Fragment implements Injectable {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ATHLETE_ID = "param-athlete-id";
    private static final String ARG_TRAINER_ID = "param-trainer-id";
    @Inject
    MyViewModelFactory factory;
    private List<Trainer> trainerList;
    private OnFragmentInteractionListener mListener;
    private AdapterTrainers adapter;
    private ToggleButton byMedalBtn;
    private ToggleButton byRankBtn;
    private TrainerListViewModel viewModel;
    private View waitingFL;
    private long mUserId;
    private long mTrainerId;
    private View detailFragment;

    public TrainerListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this mFactory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @return A new instance of fragment GymListFragment.
     */
    public static TrainerListFragment newInstance(long userId, long currentTrainerId) {
        TrainerListFragment fragment = new TrainerListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ATHLETE_ID, userId);
        args.putLong(ARG_TRAINER_ID, currentTrainerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getLong(ARG_ATHLETE_ID);
            mTrainerId = getArguments().getLong(ARG_TRAINER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trainer_list, container, false);
        detailFragment = v.findViewById(R.id.container_trainer);
        RecyclerView rv = v.findViewById(R.id.rv_trainers);
        trainerList = new ArrayList<>();
        adapter = new AdapterTrainers(AdapterTrainers.SHOW_NO_PRICE,(trainer, view) -> {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_trainer, MyTrainerFragment.newInstance(mUserId, trainer.getId(), false))
                    .commit();
            detailFragment.setVisibility(View.VISIBLE);
            //mListener.onGoToTrainerPage(trainer.getId(),false);
        });
        adapter.addAll(trainerList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        Spinner provinceSpn = v.findViewById(R.id.spnProvince);
        provinceSpn.setPrompt(getString(R.string.choose_province));

        provinceSpn.setAdapter(new ProvinceAdapter());

        waitingFL = v.findViewById(R.id.fl_waiting);
        byMedalBtn = v.findViewById(R.id.btnByMedals);
        byRankBtn = v.findViewById(R.id.btnByRank);
        byMedalBtn.setOnCheckedChangeListener((compoundButton, b) -> {
          //  waitingFL.setVisibility(View.VISIBLE);
            byRankBtn.setChecked(!b);
            /*getTrainerWeb(0, 0, b ? 1 : 2, new OnGetTrainerListner() {
                @Override
                public void onSuccess() {
                    waitingFL.setVisibility(View.GONE);
                }

                @Override
                public void onFailed() {

                }
            });*/
            compoundButton.setEnabled(!b);
        });
        byRankBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            byMedalBtn.setChecked(!b);
          //  compoundButton.setEnabled(!b);
        });


        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(TrainerListViewModel.class);
        viewModel.init(3);
        viewModel.getTrainerList().observe(this, trainerList -> {
            Log.d("onActivityCreated", "observe: ");
            this.trainerList.clear();
            this.trainerList.addAll(trainerList);
            adapter.addAll(trainerList);
            waitingFL.setVisibility(View.GONE);
        });


        Log.d(getClass().getSimpleName(), "onActivityCreated: ");
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

    @Override
    //Pressed return button - returns to the results menu
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && detailFragment.getVisibility() == View.VISIBLE) {
                detailFragment.setVisibility(View.GONE);
                //your code

                return true;
            }
            return false;
        });
    }

    private interface OnGetTrainerListner {
        void onSuccess();

        void onFailed();
    }

    public interface OnFragmentInteractionListener {

        void onGoToTrainerPage(long trainerId, boolean isMyTrainer);
    }

    class ProvinceAdapter implements SpinnerAdapter {

        final List<Province> provinceList;

        public ProvinceAdapter() {
            provinceList = new ArrayList<>();
            this.provinceList.addAll(CityNState.getProvinceList());
        }

        @Override
        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = new TextView(getContext());
                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.iran_sans_mobile);

                ((TextView) view).setTypeface(typeface);
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                ((TextView) view).setTextColor(Color.BLACK);
            }
            ((TextView) view).setText(getItem(i).getName());
            return view;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public int getCount() {
            return provinceList.size();
        }

        @Override
        public Province getItem(int i) {
            return provinceList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return provinceList.get(i).getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = new TextView(getContext());
                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.iran_sans_mobile);

                ((TextView) view).setTypeface(typeface);
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                ((TextView) view).setTextColor(Color.BLACK);
            }
            if (i == 0) {
                ((TextView) view).setText(getString(R.string.choose_province));
                return view;
            }
            ((TextView) view).setText(getItem(i - 1).getName());
            return view;
        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }

}

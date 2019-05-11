package com.example.arsalan.mygym.fragments;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.arsalan.mygym.models.CityNState;
import com.example.arsalan.mygym.models.Province;
import com.example.arsalan.mygym.models.RetTrainerList;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.activities.ProfileTrainerActivity;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterTrainers;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TrainerListViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.EXTRA_IMAGE_TRANSITION_NAME;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PARCLABLE_OBJ;


public class TrainerListFragment extends Fragment implements Injectable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Trainer> trainerList;
    private OnFragmentInteractionListener mListener;
    private AdapterTrainers adapter;

    private ToggleButton byMedalBtn;
    private ToggleButton byRankBtn;

    private TrainerListViewModel viewModel;

    @Inject
    MyViewModelFactory factory;

    private View waitingFL;

    public TrainerListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GymListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrainerListFragment newInstance(String param1, String param2) {
        TrainerListFragment fragment = new TrainerListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trainer_list, container, false);

        RecyclerView rv = v.findViewById(R.id.rvTrainers);
        trainerList = new ArrayList<>();
        adapter = new AdapterTrainers(trainerList, new AdapterTrainers.OnItemClickListener() {
            @Override
            public void onItemClick(Trainer trainer, View view) {
                Intent i = new Intent();
                i.setClass(getActivity(), ProfileTrainerActivity.class);
                i.putExtra(EXTRA_PARCLABLE_OBJ, trainer);
                i.putExtra(EXTRA_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(view));

              /*  ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        view,
                        ViewCompat.getTransitionName(view));
*/
             //   startVideoRecorderActivity(i, options.toBundle());
                startActivity(i);
            }
        });
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        Spinner provinceSpn = v.findViewById(R.id.spnProvince);
        provinceSpn.setPrompt(getString(R.string.choose_province));

        provinceSpn.setAdapter(new ProvinceAdapter());

         waitingFL = v.findViewById(R.id.flWaiting);
        byMedalBtn = v.findViewById(R.id.btnByMedals);
        byRankBtn = v.findViewById(R.id.btnByRank);
        byMedalBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                waitingFL.setVisibility(View.VISIBLE);
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
            }
        });
        byRankBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byMedalBtn.setChecked(!b);
                compoundButton.setEnabled(!b);
            }
        });


       /* getTrainerWeb(0, 0, 1, new OnGetTrainerListner() {
            @Override
            public void onSuccess() {
                waitingFL.setVisibility(View.GONE);
            }

            @Override
            public void onFailed() {

            }
        });*/
        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(TrainerListViewModel.class);
        viewModel.getTrainerList().observe(this, trainerList -> {
            Log.d("onActivityCreated", "observe: ");
            this.trainerList.removeAll(this.trainerList);
            this.trainerList.addAll(trainerList);
            adapter.notifyDataSetChanged();
            waitingFL.setVisibility(View.GONE);
        });
        viewModel.init(2);

        Log.d(getClass().getSimpleName(), "onActivityCreated: ");
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

    private interface OnGetTrainerListner {
        void onSuccess();

        void onFailed();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getTrainerWeb(int cityId, int gymId, int sortType, final OnGetTrainerListner onGetTrainerListner) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RetTrainerList> call = apiService.getTrainerList(0, 10, gymId, cityId, sortType);
        call.enqueue(new Callback<RetTrainerList>() {
            @Override
            public void onResponse(Call<RetTrainerList> call, Response<RetTrainerList> response) {
                onGetTrainerListner.onSuccess();
                if (response.isSuccessful())
                    Log.d("getNewsWeb", "onResponse: records:" + response.body().getRecordsCount());
                trainerList.removeAll(trainerList);
                trainerList.addAll(response.body().getRecords());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RetTrainerList> call, Throwable t) {
                onGetTrainerListner.onFailed();
            }
        });

    }

    class ProvinceAdapter implements SpinnerAdapter {

        List<Province> provinceList;

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
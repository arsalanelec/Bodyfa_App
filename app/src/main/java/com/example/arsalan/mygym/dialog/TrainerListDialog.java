package com.example.arsalan.mygym.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterTrainers;
import com.example.arsalan.mygym.models.RetTrainerList;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrainerListDialog extends DialogFragment {
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

    public TrainerListDialog() {
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
    public static TrainerListDialog newInstance(String param1, String param2) {
        TrainerListDialog fragment = new TrainerListDialog();
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
        View v = inflater.inflate(R.layout.dialog_trainer_list, container, false);
        RecyclerView rv = v.findViewById(R.id.rvTrainers);
        trainerList = new ArrayList<>();
        /*adapter = new AdapterTrainers(trainerList, new AdapterTrainers.OnItemClickListener() {
            @Override
            public void onItemClick(Trainer trainer, View view) {
                Intent intent = new Intent();
                intent.putExtra(MyKeys.EXTRA_TRAINER_ID, trainer.getId());
                getTargetFragment().onActivityResult(getTargetRequestCode(), MyKeys.RESULT_OK, intent);
                dismiss();
            }
        });*/
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        byMedalBtn = v.findViewById(R.id.btnByMedals);
        byRankBtn = v.findViewById(R.id.btnByRank);
        byMedalBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byRankBtn.setChecked(!b);
                getTrainerWeb(0, 0, b ? 1 : 2);
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

        getTrainerWeb(0, 0, 1);
        return v;
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

    private void getTrainerWeb(int cityId, int gymId, int sortType) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        waitingDialog.setMessage(getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        Call<RetTrainerList> call = apiService.getTrainerList(0, 10, gymId, cityId, sortType);
        call.enqueue(new Callback<RetTrainerList>() {
            @Override
            public void onResponse(Call<RetTrainerList> call, Response<RetTrainerList> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful())
                    Log.d("getNewsWeb", "onResponse: records:" + response.body().getRecordsCount());
                trainerList.removeAll(trainerList);
                trainerList.addAll(response.body().getRecords());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RetTrainerList> call, Throwable t) {
                waitingDialog.dismiss();

            }
        });

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}

package com.example.arsalan.mygym.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterTrainers;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TrainerListViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;


public class TrainerListDialog extends DialogFragment implements Injectable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Inject
    MyViewModelFactory mFactory;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Trainer> trainerList;
    private List<Trainer> filteredTrainerList;
    private OnFragmentInteractionListener mListener;
    private AdapterTrainers adapter;
    private TrainerListViewModel viewModel;
    private View waitingFL;


    public TrainerListDialog() {
        // Required empty public constructor
    }

    /**
     * Use this mFactory method to create a new instance of
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
        waitingFL = v.findViewById(R.id.fl_waiting);
        RecyclerView rv = v.findViewById(R.id.rv_trainers);
        filteredTrainerList = new ArrayList<>();
        trainerList = new ArrayList<>();
        adapter = new AdapterTrainers((trainer, view) -> {
            dismiss();
            Intent intent = new Intent();
            intent.putExtra(MyKeys.EXTRA_OBJ_TRAINER,trainer);
            getTargetFragment().onActivityResult(getTargetRequestCode(),RESULT_OK,intent);
        });
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        androidx.appcompat.widget.SearchView searchView = v.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filteredTrainerList.clear();
                if (query.isEmpty()) {
                    filteredTrainerList.addAll(trainerList);
                } else {
                    for (Trainer trainer : trainerList) {
                        if (trainer.getName().toLowerCase().contains(query.toLowerCase())) {
                            filteredTrainerList.add(trainer);

                        }
                    }
                }
                adapter.replaceAll(filteredTrainerList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredTrainerList.clear();
                if (newText.isEmpty()) {
                    filteredTrainerList.addAll(trainerList);
                } else {
                    for (Trainer trainer : trainerList) {
                        if (trainer.getName().toLowerCase().contains(newText.toLowerCase())) {
                            filteredTrainerList.add(trainer);

                        }
                    }
                }
                adapter.replaceAll(filteredTrainerList);
                return false;
            }
        });
        ImageButton cancelBtn=v.findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(b->dismiss());
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, mFactory).get(TrainerListViewModel.class);
        viewModel.getTrainerList().observe(this, trainerList -> {
            Log.d("onActivityCreated", "observe: ");
            this.trainerList = trainerList;
            filteredTrainerList.clear();
            filteredTrainerList.addAll(trainerList);
            adapter.addAll(trainerList);
            waitingFL.setVisibility(View.GONE);
        });
        viewModel.init(2);

        Log.d(getClass().getSimpleName(), "onActivityCreated: ");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
       dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}

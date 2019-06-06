package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.databinding.ItemMyTrainerBinding;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.TrainerAthlete;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TrainerAthleteViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyTrainerMembershipFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyTrainerMembershipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTrainerMembershipFragment extends Fragment implements Injectable {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "MyTrainerMembershipFrag";
    @Inject
    MyViewModelFactory factory;
    private long mUserId;
    private OnFragmentInteractionListener mListener;
    private TrainerAthleteViewModel viewModel;
    private List<TrainerAthlete> mActiveRequestList;
    private RequestAdapter mAdapter;
    private RequestAdapter mAdapterActiveReq;
    private List<TrainerAthlete> mOtherRequestList;
private TextView noActiveRequestTxt;
private TextView noDeactiveRequestTxt;
    public MyTrainerMembershipFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @return A new instance of fragment MyTrainerMembershipFragment.
     */
    public static MyTrainerMembershipFragment newInstance(long userId) {
        MyTrainerMembershipFragment fragment = new MyTrainerMembershipFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getLong(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_trainer_membership, container, false);
        noActiveRequestTxt = v.findViewById(R.id.txtNoActiveRequest);
        noDeactiveRequestTxt = v.findViewById(R.id.txtNoDeactiveRequest);
        RecyclerView activeRequestRv = v.findViewById(R.id.rvActiveMembershipRequests);
        activeRequestRv.setLayoutManager(new LinearLayoutManager(getContext()));
        activeRequestRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mActiveRequestList = new ArrayList<>();
        mAdapterActiveReq = new RequestAdapter(mActiveRequestList);
        activeRequestRv.setAdapter(mAdapterActiveReq);

        RecyclerView requestRv = v.findViewById(R.id.rvMembershipRequests);
        requestRv.setLayoutManager(new LinearLayoutManager(getContext()));
        requestRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));


        mOtherRequestList = new ArrayList<>();
        mAdapter = new RequestAdapter(mOtherRequestList);

        requestRv.setAdapter(mAdapter);
        v.setRotation(180);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(TrainerAthleteViewModel.class);
        viewModel.initByUser(mUserId);

        viewModel.getActiveTrainerList().observe(this, requestList -> {

            mActiveRequestList.clear();
            mOtherRequestList.clear();
            for (TrainerAthlete trainerAthlete : requestList) {
                if (trainerAthlete.getStatus().equalsIgnoreCase("confirmed")) {
                    mActiveRequestList.add(trainerAthlete);

                } else {
                    mOtherRequestList.add(trainerAthlete);
                }
            }
            noActiveRequestTxt.setVisibility((mActiveRequestList.size()>0)?View.GONE:View.VISIBLE);
            noDeactiveRequestTxt.setVisibility((mOtherRequestList.size()>0)?View.GONE:View.VISIBLE);

            mAdapterActiveReq.notifyDataSetChanged();
            mAdapter.notifyDataSetChanged();
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
        void onGoToTrainerPage(long trainerId, boolean isMyTrainer);
    }

    public interface OnRequestItemListener {
        void onItemClicked(long trainerId);
    }

    private class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> implements OnRequestItemListener {
        List<TrainerAthlete> trainerAthletes;

        public RequestAdapter(List<TrainerAthlete> trainerAthletes) {
            this.trainerAthletes = trainerAthletes;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemMyTrainerBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_my_trainer, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.setTrainer(trainerAthletes.get(position));
            holder.binding.setOnClickListener(this::onItemClicked);
            Glide.with(getContext())
                    .load(MyConst.BASE_CONTENT_URL + trainerAthletes.get(position).getParentThumbUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.bodybuilder_place_holder).circleCrop().fitCenter())
                    .apply(new RequestOptions().circleCrop())
                    .into(holder.binding.imgThumb);

        }

        @Override
        public int getItemCount() {
            return trainerAthletes.size();
        }

        @Override
        public void onItemClicked(long trainerId) {
            mListener.onGoToTrainerPage(trainerId, true);
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            ItemMyTrainerBinding binding;

            public ViewHolder(@NonNull ItemMyTrainerBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

}

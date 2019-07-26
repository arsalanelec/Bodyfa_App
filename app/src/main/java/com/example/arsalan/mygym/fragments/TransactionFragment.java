package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.databinding.FragmentTransactionBinding;
import com.example.arsalan.mygym.databinding.ItemTransactionBinding;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.Transaction;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TransactionsViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;

public class TransactionFragment extends Fragment implements Injectable {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "TransactionListActivity";
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    MyViewModelFactory mFactory;

    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private long mUserId;

    public TransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static TransactionFragment newInstance(long userId) {
        TransactionFragment fragment = new TransactionFragment();
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
       FragmentTransactionBinding bind= DataBindingUtil.inflate(getLayoutInflater(),R.layout.fragment_transaction, container, false);
        List<Transaction> transactionList = new ArrayList<>();
        RecyclerAdapter adapter = new RecyclerAdapter(transactionList);
        bind.recyclerView.setAdapter(adapter);
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bind.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        bind.swipeRefresh.setRefreshing(true);

        TransactionsViewModel viewModel = ViewModelProviders.of(this, mFactory).get(TransactionsViewModel.class);
        viewModel.init(mUserId);
        viewModel.getTransactionList().observe(this, tList -> {
            if (tList != null) {
                bind.swipeRefresh.setRefreshing(false);
                transactionList.clear();
                transactionList.addAll(tList);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "onCreate: count:"+tList.size());
            }
        });
        bind.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.init(mUserId);
        });
        bind.getRoot().setRotation(180);
       return bind.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }/* else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * adapter for trancation recyclerView
     */
    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        final List<Transaction> transactions;

        public RecyclerAdapter(List<Transaction> transactions) {
            this.transactions = transactions;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemTransactionBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_transaction, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.setTAction(transactions.get(position));
        }

        @Override
        public int getItemCount() {
            return transactions.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            final ItemTransactionBinding binding;

            public ViewHolder(ItemTransactionBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

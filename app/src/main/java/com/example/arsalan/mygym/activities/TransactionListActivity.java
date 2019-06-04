package com.example.arsalan.mygym.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.databinding.ActivityTrancastionListBinding;
import com.example.arsalan.mygym.databinding.ItemTransactionBinding;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.Transaction;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TransactionsViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.example.arsalan.mygym.MyKeys.EXTRA_USER_ID;

public class TransactionListActivity extends AppCompatActivity implements Injectable, HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    MyViewModelFactory mfactory;
    private static final String TAG = "TransactionListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long mUserId;
        if (getIntent().getExtras() != null) {
            mUserId = getIntent().getExtras().getLong(EXTRA_USER_ID, 0);
        } else {
            throw new RuntimeException("No UserId Passed!");
        }
        ActivityTrancastionListBinding bind = DataBindingUtil.setContentView(this, R.layout.activity_trancastion_list);
        setTitle(R.string.transactions);
        List<Transaction> transactionList = new ArrayList<>();
        RecyclerAdapter adapter = new RecyclerAdapter(transactionList);
        bind.recyclerView.setAdapter(adapter);
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bind.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        TransactionsViewModel viewModel = ViewModelProviders.of(TransactionListActivity.this, mfactory).get(TransactionsViewModel.class);
        viewModel.init(mUserId);
        viewModel.getTransactionList().observe(this, tList -> {
            if (tList != null) {
                transactionList.removeAll(transactionList);
                transactionList.addAll(tList);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "onCreate: count:"+tList.size());
            }
        });
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        List<Transaction> transactions;

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
            ItemTransactionBinding binding;

            public ViewHolder(ItemTransactionBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}

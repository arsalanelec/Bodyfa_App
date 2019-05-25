package com.example.arsalan.mygym.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalan.mygym.models.Honor;
import com.example.arsalan.mygym.models.RetHonorList;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.dialog.AddMedalDialog;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HonorListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private User mUser;
    private Context mContext;
    private List<Honor> mHonorList;
    private AdapterHonors mAdapter;
    private TextView errorTv;
    private static final String TAG = "HonorListActivity";
    private SwipeRefreshLayout mRefreshLayout;

    public HonorListActivity() {
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honor_list);
        setTitle(getString(R.string.medal_list));
        if (getIntent().getExtras() != null) {
            mUser = getIntent().getParcelableExtra(MyKeys.EXTRA_OBJ_USER);
        }else {
            new RuntimeException(this.toString()+" should receive a USER in intent");
        }

        FloatingActionButton addHonorBtn = findViewById(R.id.btnAddMedal);
        addHonorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMedalDialog dialog = AddMedalDialog.newInstance(mUser);
                dialog.show(getSupportFragmentManager(), "add honor dialog");
            }
        });
        ListView honorLV = findViewById(R.id.listView);
        mHonorList = new ArrayList<>();
        mAdapter = new AdapterHonors(mHonorList);
        errorTv = findViewById(R.id.txtError);
         mRefreshLayout = findViewById(R.id.refreshLay);
        mRefreshLayout.setOnRefreshListener(this);
        honorLV.setAdapter(mAdapter);
        mRefreshLayout.setRefreshing(true);
        getHonorsWeb(mUser.getId());

    }

    @Override
    public void onRefresh() {
        getHonorsWeb(mUser.getId());
    }

    private class AdapterHonors extends BaseAdapter {
        List<Honor> honorList;

        public AdapterHonors(List<Honor> honorList) {
            this.honorList = honorList;
        }

        @Override
        public int getCount() {
            return honorList.size();
        }

        @Override
        public Honor getItem(int i) {
            return honorList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return getItem(i).getId();
        }



        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(mContext, R.layout.item_my_honor, null);
            }
            TextView titleTv = view.findViewById(R.id.txtTitle);
            titleTv.setText(getItem(i).getTitle());

            ImageView medalImg = view.findViewById(R.id.imgMedal);
            int medalImageRes;
            switch (honorList.get(i).getCategory()) {
                case 0:
                    medalImageRes = R.drawable.medal_jahani_1;
                    break;
                case 1:
                    medalImageRes = R.drawable.medal_jahani_2;
                    break;
                case 2:
                    medalImageRes = R.drawable.medal_jahani_3;
                    break;
                case 3:
                    medalImageRes = R.drawable.medal_keshvari_1;
                    break;
                case 4:
                    medalImageRes = R.drawable.medal_keshvari_2;
                    break;
                case 5:
                    medalImageRes = R.drawable.medal_keshvari_3;
                    break;
                case 6:
                    medalImageRes = R.drawable.medal_ostani_1;
                    break;
                case 7:
                    medalImageRes = R.drawable.medal_ostani_2;
                    break;
                case 8:
                    medalImageRes = R.drawable.medal_ostani_3;
                    break;
                default:
                    medalImageRes = R.drawable.medal_ostani_3;
                    break;
            }
            medalImg.setImageResource(medalImageRes);

            Button removeBtn = view.findViewById(R.id.btnRemove);
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog removeDialog = new AlertDialog.Builder(mContext,R.style.AlertDialogCustomPrivate)
                            .setMessage(R.string.ask_remove_medal)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int ii) {
                                    removeHonorWeb(getItemId(i),i);
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create();
                    removeDialog.show();
                }
            });
            return view;
        }

    }

    private void getHonorsWeb(long userId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<RetHonorList> call = apiService.getHonorList("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), userId);
        call.enqueue(new Callback<RetHonorList>() {
            @Override
            public void onResponse(Call<RetHonorList> call, Response<RetHonorList> response) {
                mRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: success! cnt:"+response.body().getRecordsCount()+" id"+userId);

                    if (response.body().getRecordsCount() > 0) {
                        mHonorList.removeAll(mHonorList);
                        mHonorList.addAll(response.body().getRecords());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        errorTv.setVisibility(View.VISIBLE);
                    }
                } else {
                    errorTv.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onResponse: error!");

                }
            }

            @Override
            public void onFailure(Call<RetHonorList> call, Throwable t) {
                mRefreshLayout.setRefreshing(false);
                errorTv.setVisibility(View.VISIBLE);
                Log.d(TAG, "onResponse: throws:"+t.getCause());


            }
        });

    }

    private void removeHonorWeb(long honorId,int position) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<RetroResult> call = apiService.removeHonor("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), honorId);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
        if(response.isSuccessful()){
            Toast.makeText(mContext, getString(R.string.removed_successfuly), Toast.LENGTH_SHORT).show();
            mHonorList.remove(position);
            mAdapter.notifyDataSetChanged();
        }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                Toast.makeText(mContext, R.string.error_accord_try_again, Toast.LENGTH_SHORT).show();

            }
        });
    }

}

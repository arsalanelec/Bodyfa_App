package com.example.arsalan.mygym.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.PrivateMessage;
import com.example.arsalan.mygym.models.RetPMList;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.EXTRA_PARTY_ID;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PARTY_NAME;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PARTY_THUMB;
import static com.example.arsalan.mygym.MyKeys.EXTRA_USER_ID;
import static com.example.arsalan.mygym.MyKeys.VIEW_TYPE_PM_ME;
import static com.example.arsalan.mygym.MyKeys.VIEW_TYPE_PM_OTHERS;

public class MessageRoomActivity extends AppCompatActivity {

    private List<PrivateMessage> mPrivateMessageList;
    private AdapterPMList adapter;
    private long mUserId;
    private long mPartyId;
    private String mPartyThumUrl;
    private ProgressBar waitingPB;
    private ImageButton sendBtn;
    private EditText sendMessageET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_room);

        mUserId = getIntent().getLongExtra(EXTRA_USER_ID, 0);
        mPartyId = getIntent().getLongExtra(EXTRA_PARTY_ID, 0);
        String title = getIntent().getStringExtra(EXTRA_PARTY_NAME);
        mPartyThumUrl = MyConst.BASE_CONTENT_URL + getIntent().getStringExtra(EXTRA_PARTY_THUMB);

        if (title != null) setTitle(title);

        final RecyclerView pmListRV = findViewById(R.id.rvPM);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        pmListRV.setLayoutManager(mLayoutManager);
        mPrivateMessageList = new ArrayList<>();
        adapter = new AdapterPMList(mPrivateMessageList);
        pmListRV.setAdapter(adapter);
        pmListRV.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    pmListRV.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pmListRV.smoothScrollToPosition(
                                    0);
                        }
                    }, 100);
                }
            }
        });
        waitingPB = findViewById(R.id.pbWaiting);
        getPMListWeb();

        sendMessageET = findViewById(R.id.etSendMessage);

        sendBtn = findViewById(R.id.btnSend);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sendMessageET.getText().toString().isEmpty()) return;
                sendMessageWeb(sendMessageET.getText().toString().replace("\n", "").replace("\r", ""));
            }
        });

    }

    private void sendMessageWeb(String message) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<RetroResult> call = apiService.sendPrivateMessage("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), mUserId, mPartyId, RequestBody.create(MediaType.parse("text/plain"), message));
        sendBtn.setEnabled(false);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                getPMListWeb();
                sendMessageET.setText("");
                sendBtn.setEnabled(true);

            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                Log.d(getClass().getSimpleName(), "onFailure: " + t.getMessage());
                sendBtn.setEnabled(true);
            }
        });
    }

    private void getPMListWeb() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RetPMList> call = apiService.getPmList("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), mUserId, mPartyId);
        call.enqueue(new Callback<RetPMList>() {
            @Override
            public void onResponse(Call<RetPMList> call, Response<RetPMList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    waitingPB.setVisibility(View.GONE);
                    mPrivateMessageList.removeAll(mPrivateMessageList);
                    mPrivateMessageList.addAll(response.body().getRecords());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RetPMList> call, Throwable t) {
                waitingPB.setVisibility(View.GONE);
                Log.d(getClass().getSimpleName(), "onFailure: " + t.getMessage());

            }
        });
    }


    private class AdapterPMList extends RecyclerView.Adapter<ViewHolder> {
        List<PrivateMessage> privateMessageList;

        public AdapterPMList(List<PrivateMessage> privateMessageList) {
            this.privateMessageList = privateMessageList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == VIEW_TYPE_PM_ME) {
                view = getLayoutInflater().inflate(R.layout.item_pm_me, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.item_pm_others, parent, false);
            }

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder h, int p) {
            PrivateMessage pm = privateMessageList.get(p);
            h.messageTV.setText(pm.getMessage());
            if (getItemViewType(p) == VIEW_TYPE_PM_OTHERS) {
                Glide.with(MessageRoomActivity.this)
                        .load(mPartyThumUrl)
                        .apply(new RequestOptions().placeholder(R.drawable.bodybuilder_place_holder).circleCrop())
                        .apply(RequestOptions.circleCropTransform())
                        .into(h.thumbImg);
            }
            //   h.dateTV.setText(pm.getSendDate());
        }

        @Override
        public int getItemCount() {
            return privateMessageList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (privateMessageList.get(position).getSenderId() == mUserId)
                return VIEW_TYPE_PM_ME;
            else return VIEW_TYPE_PM_OTHERS;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTV;
        //  TextView dateTV;
        ImageView thumbImg;

        public ViewHolder(View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.txtView);
            //   dateTV = itemView.findViewById(R.id.txtDate);
            thumbImg = itemView.findViewById(R.id.imgThumb);
        }
    }
}

package com.example.arsalan.mygym.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterComments;
import com.example.arsalan.mygym.models.Comment;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.News;
import com.example.arsalan.mygym.models.RetCommentList;
import com.example.arsalan.mygym.models.RetNewsDetail;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailActivity extends AppCompatActivity {
    public static final String KEY_NEWS_ID = "new id key";
    private static final String TAG = "NewsDetailActivity";
    private SimpleDraweeView image;
    private TextView titleTV;
    private TextView contentTV;
    private TextView commentsCountTV;
    private TextView sendCommentTV;

    private AdapterComments adapter;
    private RecyclerView commentRV;
    private List<Comment> commentList;
    private FloatingActionButton sendCommentBtn;
    private TextView likeCntTV;
    private ImageButton isLikedBtn;

    private boolean mIsLiked = false;
    private int mLikeCount;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final long newsId = getIntent().getLongExtra(KEY_NEWS_ID, 1);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titleTV = findViewById(R.id.txtTitle);
        contentTV = findViewById(R.id.txtContent);
        likeCntTV = findViewById(R.id.txtLikeCnt);

        isLikedBtn = findViewById(R.id.imgBtnIsLiked);
        isLikedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeANewsWeb(newsId, !mIsLiked);
                isLikedBtn.setImageResource(mIsLiked ? R.drawable.ic_favorite_border_48dp : R.drawable.ic_favorite_48dp);
            }
        });

        image = findViewById(R.id.image);
        image.setOnTouchListener(new View.OnTouchListener() {

            private GestureDetector gestureDetector = new GestureDetector(NewsDetailActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d("TEST", "onDoubleTap");
                    ViewPropertyAnimator animator;
                    final ImageView heartImg = findViewById(R.id.imgHeart);
                    heartImg.setVisibility(View.VISIBLE);
                    heartImg.animate()
                            .setDuration(300)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .scaleX(10.0f)
                            .scaleY(10.0f)
                            .alpha(0.0f)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    heartImg.setVisibility(View.GONE);
                                    heartImg.setAlpha(1.0f);
                                    heartImg.setScaleX(0.1f);
                                    heartImg.setScaleY(1.0f);
                                    if (!mIsLiked) {
                                        isLikedBtn.setImageResource(R.drawable.ic_favorite_48dp);
                                        likeANewsWeb(newsId, true);
                                    }
                                }
                            })
                            .start();
                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.d("TEST", "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
                gestureDetector.onTouchEvent(event);
                return true;
            }

        });
        commentsCountTV = findViewById(R.id.txtCommentsTitle);

        commentList = new ArrayList<>();

        adapter = new AdapterComments(commentList);
        commentRV = findViewById(R.id.rcyComments);
        commentRV.setLayoutManager(new LinearLayoutManager(this));
        commentRV.setAdapter(adapter);

        final AlertDialog sendCommentDialog = new AlertDialog.Builder(NewsDetailActivity.this, R.style.AlertDialogCustom)

                .setNeutralButton(getString(R.string.send), null)
                .setCustomTitle(LayoutInflater.from(NewsDetailActivity.this).inflate(R.layout.title_send_comment, null, false))
                .setView(LayoutInflater.from(NewsDetailActivity.this).inflate(R.layout.simple_edit_text, null, false))
                .create();

        sendCommentDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                sendCommentTV = sendCommentDialog.findViewById(R.id.editText);
                sendCommentDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (sendCommentTV.length() < 2) {
                            sendCommentTV.setError(getString(R.string.text_is_too_short));
                            return;
                        }
                        sendCommentWeb(newsId, "" + sendCommentTV.getText().toString());
                        sendCommentDialog.dismiss();
                    }
                });
            }
        });
        sendCommentBtn = findViewById(R.id.fabSendComment);
        sendCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sendCommentDialog.show();

            }
        });
        getNewsDetaiWeb(newsId);

    }

    private void getNewsDetaiWeb(final long newId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(this);
        waitingDialog.setMessage(getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        Call<RetNewsDetail> call = apiService.getNewsDetail("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), ((MyApplication) getApplication()).getCurrentUser().getId(), newId);
        call.enqueue(new Callback<RetNewsDetail>() {
            @Override
            public void onResponse(Call<RetNewsDetail> call, Response<RetNewsDetail> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("getNewsWeb", "onResponse: record:" + response.body().getRecord().getTitle() + " newsId:" + response.body().getRecord().getId());
                    News news = response.body().getRecord();
                    titleTV.setText(news.getTitle());
                    contentTV.setText(news.getDesc());
                    image.setImageURI(MyConst.BASE_CONTENT_URL + news.getPictureUrl());
                    try {
                        mLikeCount = news.getLikeCnt();
                        likeCntTV.setText(getString(R.string.likedCount, mLikeCount));

                    } catch (NumberFormatException e) {
                        likeCntTV.setText(R.string.unknown);

                    }

                    mIsLiked = news.isLiked();
                    isLikedBtn.setImageResource(news.isLiked() ? R.drawable.ic_favorite_48dp : R.drawable.ic_favorite_border_48dp);

                    Log.d(TAG, "onResponse: pictureUrl:" + news.getPictureUrl());
                    commentsCountTV.setText(getString(R.string.user_comments, news.getCommentCnt()));
                    getCommentWeb(news.getId());
                } else {
                    Log.d(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<RetNewsDetail> call, Throwable t) {
                waitingDialog.dismiss();

            }
        });
    }

    private void getCommentWeb(final long newId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(this);
        waitingDialog.setMessage(getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        Call<RetCommentList> call = apiService.getCommentList("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), 0, 100, newId);
        call.enqueue(new Callback<RetCommentList>() {
            @Override
            public void onResponse(Call<RetCommentList> call, Response<RetCommentList> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful() && response.body() != null)
                    Log.d("getNewsWeb", "onResponse: record:" + response.body().getRecords().size());
                commentList.removeAll(commentList);
                if (response.body() != null && response.body().getRecords().size() > 0)
                    commentList.addAll(response.body().getRecords());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RetCommentList> call, Throwable t) {
                waitingDialog.dismiss();
            }
        });
    }

    private void sendCommentWeb(final long newId, String comment) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(this);
        waitingDialog.setMessage(getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        Call<RetroResult> call = apiService.sendNewsComment("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), ((MyApplication) getApplication()).getCurrentUser().getId(), newId, comment);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful() && response.body() != null)
                    Log.d("sendCommentWeb", "onResponse: record:" + response.body().getResult());
                if (response.body().getResult().equals("OK")) {
                    Snackbar.make(sendCommentBtn, R.string.your_comment_submited, Snackbar.LENGTH_LONG)
                            .setAction(R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(Color.YELLOW).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_accord_try_again, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.error_accord_try_again, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void likeANewsWeb(long newsId, final boolean isLiked) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(this);
        waitingDialog.setMessage(getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        Call<RetroResult> call;
        if (isLiked) {
            call = apiService.likeANews("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), ((MyApplication) getApplication()).getCurrentUser().getId(), newsId);

        } else {
            call = apiService.dislikeANews("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), ((MyApplication) getApplication()).getCurrentUser().getId(), newsId);

        }
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("sendCommentWeb", "onResponse: record:" + response.body().getResult());
                    isLikedBtn.setImageResource(isLiked ? R.drawable.ic_favorite_48dp : R.drawable.ic_favorite_border_48dp);
                    mIsLiked = isLiked;
                    if (isLiked) mLikeCount++;
                    else mLikeCount--;
                    likeCntTV.setText(getString(R.string.likedCount, mLikeCount));
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.error_accord_try_again, Toast.LENGTH_LONG).show();
            }
        });
    }

}

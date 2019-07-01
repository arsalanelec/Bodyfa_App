package com.example.arsalan.mygym.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterComments;
import com.example.arsalan.mygym.databinding.FragmentNewsDetailBinding;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.Comment;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.RetCommentList;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.Token;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.NewsDetailViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsDetailFragment#newInstance} mFactory method to
 * create an instance of this fragment.
 */
public class NewsDetailFragment extends Fragment implements Injectable {
    //
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_ID = "param1";
    private static final String ARG_NEWS_ID = "param2";
    private static final String TAG = "NewsDetailFragment";
    private static final String ARG_NEWS_TYPE = "new type";
    @Inject
    MyViewModelFactory mFactory;
    @Inject
    Token mToken;
    private long mUserId;
    private long mNewsId;
    private OnFragmentInteractionListener mListener;
    private FragmentNewsDetailBinding mBind;
    private boolean mIsLiked;

    private List<Comment> mCommentList;
    private AdapterComments mAdapter;
    private int mLikeCount;
    private Integer mNextNewsId;
    private NewsDetailViewModel viewModel;
    private Integer mPrevNewsId;
    private int mCatType;

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this mFactory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @param newsId Parameter 2.
     * @return A new instance of fragment NewsDetailFragment.
     */
    public static NewsDetailFragment newInstance(long userId, long newsId,int newsType) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        args.putLong(ARG_NEWS_ID, newsId);
        args.putInt(ARG_NEWS_TYPE, newsType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getLong(ARG_USER_ID);
            mNewsId = getArguments().getLong(ARG_NEWS_ID);
            mCatType = getArguments().getInt(ARG_NEWS_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mBind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_news_detail, container, false);
        mBind.imgBtnIsLiked.setOnClickListener(view -> {
            likeANewsWeb(mNewsId, !mIsLiked);
            mBind.imgBtnIsLiked.setImageResource(mIsLiked ? R.drawable.ic_favorite_border_48dp : R.drawable.ic_favorite_48dp);
        });
        mBind.image.setOnTouchListener(new View.OnTouchListener() {

            private final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d("TEST", "onDoubleTap");
                    ViewPropertyAnimator animator;
                    mBind.imgHeart.setVisibility(View.VISIBLE);
                    mBind.imgHeart.animate()
                            .setDuration(300)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .scaleX(10.0f)
                            .scaleY(10.0f)
                            .alpha(0.0f)
                            .withEndAction(() -> {
                                mBind.imgHeart.setVisibility(View.GONE);
                                mBind.imgHeart.setAlpha(1.0f);
                                mBind.imgHeart.setScaleX(0.1f);
                                mBind.imgHeart.setScaleY(1.0f);
                                if (!mIsLiked) {
                                    mBind.imgBtnIsLiked.setImageResource(R.drawable.ic_favorite_48dp);
                                    likeANewsWeb(mNewsId, true);
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
        mBind.txtToolbar.setSelected(true);

        mCommentList = new ArrayList<>();

        mAdapter = new AdapterComments(mCommentList);
        mBind.rvComments.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,true));
        mBind.rvComments.setAdapter(mAdapter);

        mBind.imgBtnCommentSubmit.setOnClickListener(b->{
            if(mBind.etComment.getText().length()<2){
                mBind.tilComment.setError(getString(R.string.text_is_too_short));
                mBind.tilComment.requestFocus();
                return;
            }
            sendCommentWeb(mNewsId, "" + mBind.etComment.getText().toString());
            mBind.etComment.setText(""); //clear editText

        });
        mBind.etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBind.tilComment.setError("");
            }
        });

        mBind.ibBack.setOnClickListener(b->{
           container.setVisibility(View.GONE);
        });
        mBind.imgBtnNext.setOnClickListener(b->{
            viewModel.init(mUserId, mNextNewsId,mCatType);
        });

        mBind.imgBtnPrevious.setOnClickListener(b->{
            viewModel.init(mUserId, mPrevNewsId,mCatType);
        });

        return mBind.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel= ViewModelProviders.of(this,mFactory).get(NewsDetailViewModel.class);
        viewModel.init(mUserId,mNewsId,mCatType);
        viewModel.getNews().observe(this,news->{
            if(news!=null) {
                Log.d("NewsDetailViewModel", "onActivityCreated: newsId:"+news.getId());
                mBind.setNews(news);

                getCommentWeb(news.getId());
                mBind.image.setImageURI(MyConst.BASE_CONTENT_URL + news.getPictureUrl());

                mBind.flWaiting.setVisibility(View.GONE);
            }else {
                mBind.txtError.setVisibility(View.VISIBLE);
                mBind.pbWaiting2.setVisibility(View.GONE);
            }

        });

        viewModel.getNextNews().observe(this,nextNews->{
            if(nextNews!=null) {
                mBind.imgBtnNext.setVisibility(View.VISIBLE);
                mNextNewsId =nextNews;
            }else {
                mBind.imgBtnNext.setVisibility(View.GONE);
            }
        });

        viewModel.getPrevNewsId().observe(this,prevNewsId->{
            if(prevNewsId!=null) {
                mBind.imgBtnPrevious.setVisibility(View.VISIBLE);
                mPrevNewsId =prevNewsId;
            }else {
                mBind.imgBtnPrevious.setVisibility(View.GONE);
            }
        });
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


    private void getCommentWeb(final long newId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RetCommentList> call = apiService.getCommentList(mToken.getTokenBearer(), 0, 100, newId);
        call.enqueue(new Callback<RetCommentList>() {
            @Override
            public void onResponse(Call<RetCommentList> call, Response<RetCommentList> response) {
                mBind.pbWaiting.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mCommentList.clear();
                    mCommentList.addAll(response.body().getRecords());
                    mAdapter.notifyDataSetChanged();
                    if(mCommentList.size()>0){
                        mBind.rvComments.setVisibility(View.VISIBLE);
                        mBind.txtNoComment.setVisibility(View.GONE);
                    }else {
                        mBind.rvComments.setVisibility(View.GONE);
                        mBind.txtNoComment.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<RetCommentList> call, Throwable t) {
                mBind.pbWaiting.setVisibility(View.GONE);
            }
        });
    }

    private void sendCommentWeb(final long newId, String comment) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        waitingDialog.setMessage(getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        RequestBody commentReq = RequestBody.create(MediaType.parse("text/plain"), comment);

        Call<RetroResult> call = apiService.sendNewsComment(mToken.getTokenBearer(),mUserId , newId, commentReq);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful() && response.body() != null)
                    Log.d("sendCommentWeb", "onResponse: record:" + response.body().getResult());
                if (response.body().getResult().equals("OK")) {
                    getCommentWeb(newId);
                    Snackbar.make(mBind.imgBtnCommentSubmit, R.string.your_comment_submited, Snackbar.LENGTH_LONG)
                            .setAction(R.string.ok, view -> {

                            })
                            .setActionTextColor(Color.YELLOW).show();
                } else {
                    Toast.makeText(getContext(), R.string.error_accord_try_again, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_accord_try_again, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void likeANewsWeb(long newsId, final boolean isLiked) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        waitingDialog.setMessage(getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        Call<RetroResult> call;
        if (isLiked) {
            call = apiService.likeANews(mToken.getTokenBearer(),mUserId , newsId);

        } else {
            call = apiService.dislikeANews(mToken.getTokenBearer(),mUserId , newsId);

        }
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("sendCommentWeb", "onResponse: record:" + response.body().getResult());
                    mBind.imgBtnIsLiked.setImageResource(isLiked ? R.drawable.ic_favorite_48dp : R.drawable.ic_favorite_border_48dp);
                    mIsLiked = isLiked;
                    if (isLiked) mLikeCount++;
                    else mLikeCount--;
                    mBind.txtLikeCnt.setText(getString(R.string.likedCount, mLikeCount));
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_accord_try_again, Toast.LENGTH_LONG).show();
            }
        });
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

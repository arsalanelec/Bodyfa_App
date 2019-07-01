package com.example.arsalan.mygym.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.example.arsalan.mygym.models.NewsHead;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterNews;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.viewModels.NewsListViewModel;
import com.example.arsalan.mygym.webservice.WebServiceResultImplementation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class NewsListFragment extends Fragment implements WebServiceResultImplementation , Injectable, SwipeRefreshLayout.OnRefreshListener , AdapterNews.OnAdapterNewsEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "NewsListFragment";
    // TODO: Rename and change types of parameters
    private long mUserId;

    private OnFragmentInteractionListener mListener;
    private List<NewsHead> newsList;
    private AdapterNews adapter;

    private FrameLayout waitingFL;

    private NewsListViewModel newsListViewModel;

    @Inject
    MyViewModelFactory factory;
    private SwipeRefreshLayout mSwipeRefresh;
    private int mNewsType=1;
    private View detailContainer;

    public NewsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this mFactory method to create a new instance of
     * this fragment using the provided parameters.
     * @param userId Parameter 1.
     * @return A new instance of fragment NewsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsListFragment newInstance(long userId) {
        NewsListFragment fragment = new NewsListFragment();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newsListViewModel = ViewModelProviders.of(this, factory).get(NewsListViewModel.class);
        newsListViewModel.getNewsList().observe(this, newNewsList -> {
            Log.d("NewsListViewModel", "observe: cnt:"+newNewsList.size());
            newsList.clear();
            newsList.addAll(newNewsList);
            adapter.notifyDataSetChanged();
            waitingFL.setVisibility(View.GONE);
            mSwipeRefresh.setRefreshing(false);
        });
        newsListViewModel.init(0,mNewsType);

        Log.d(getClass().getSimpleName(), "onActivityCreated: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        final RecyclerView newsRV = v.findViewById(R.id.rv_news);
        newsList = new ArrayList<>();
        /*for (int i = 0; i < 20; i++)
            newsList.add(new NewsHead());*/

        adapter = new AdapterNews(newsList, this);
        newsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        // newsRV.setLayoutAnimation(animation);
        newsRV.setAdapter(adapter);
        waitingFL = v.findViewById(R.id.fl_waiting);

        final ToggleButton foodNewsTgl = v.findViewById(R.id.btnFoodNews);

        final ToggleButton fitnessNewsTgl = v.findViewById(R.id.btnFitnessNews);
        fitnessNewsTgl.setChecked(true);


        //MyWebService.getNewsWeb(0, 1, newsList, adapter, getContext(), newsRV, NewsListFragment.this);
        foodNewsTgl.setOnCheckedChangeListener((compoundButton, b) -> {
            compoundButton.setEnabled(!b);
            fitnessNewsTgl.setChecked(!b);
            mNewsType=b ? 2 : 1;
            newsListViewModel.init(0,mNewsType);
            mSwipeRefresh.setRefreshing(true);
            //waitingFL.setVisibility(View.VISIBLE);

            //   MyWebService.getNewsWeb(0, b ? 1 : 2, newsList, adapter, getContext(), newsRV, NewsListFragment.this);
        });
         detailContainer=v.findViewById(R.id.container);
        detailContainer.setVisibility(View.GONE);
        fitnessNewsTgl.setOnCheckedChangeListener((compoundButton, b) -> {
            foodNewsTgl.setChecked(!b);
            compoundButton.setEnabled(!b);
            Log.d(TAG, "fitnessNewsTgl onCheckedChanged: ");
        });
         mSwipeRefresh = v.findViewById(R.id.swipeLay);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setRefreshing(true);
        v.setRotation(180);
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
    //Pressed return button - returns to the results menu
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && detailContainer.getVisibility()==View.VISIBLE){
                detailContainer.setVisibility(View.GONE);
                //your code

                return true;
            }
            return false;
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void webServiceOnSuccess(Bundle bundle) {
        waitingFL.setVisibility(View.GONE);
    }

    @Override
    public void webServiceOnFail() {

    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.setRefreshing(true);

        newsListViewModel.init(0,mNewsType);
    }

    @Override
    public void onNewsHeadClick(long newsId,int catType) {
        Log.d(TAG, "onNewsHeadClick: ");
        getFragmentManager().beginTransaction().replace(R.id.container,NewsDetailFragment.newInstance(mUserId,newsId,catType),"").commit();
        detailContainer.setVisibility(View.VISIBLE);

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.example.arsalan.mygym.models.News;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterNews;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.viewModels.NewsListViewModel;
import com.example.arsalan.mygym.webservice.WebServiceResultImplementation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class NewsListFragment extends Fragment implements WebServiceResultImplementation , Injectable, SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private List<News> newsList;
    private AdapterNews adapter;

    private FrameLayout waitingFL;

    private NewsListViewModel viewModel;

    @Inject
    MyViewModelFactory factory;
    private SwipeRefreshLayout mSwipeRefresh;
    private int mNewsType=1;

    public NewsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsListFragment newInstance(String param1, String param2) {
        NewsListFragment fragment = new NewsListFragment();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(NewsListViewModel.class);
        viewModel.getNewsList().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> newNewsList) {
                Log.d("onActivityCreated", "observe: ");
                newsList.removeAll(newsList);
                newsList.addAll(newNewsList);
                adapter.notifyDataSetChanged();
                waitingFL.setVisibility(View.GONE);
                mSwipeRefresh.setRefreshing(false);
            }
        });
        viewModel.init(mNewsType);

        Log.d(getClass().getSimpleName(), "onActivityCreated: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        final RecyclerView newsRV = v.findViewById(R.id.rvNews);
        newsList = new ArrayList<>();
        /*for (int i = 0; i < 20; i++)
            newsList.add(new News());*/

        adapter = new AdapterNews(getActivity(), newsList);
        newsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        // newsRV.setLayoutAnimation(animation);
        newsRV.setAdapter(adapter);
        waitingFL = v.findViewById(R.id.flWaiting);

        final ToggleButton foodNewsTgl = v.findViewById(R.id.btnFoodNews);

        final ToggleButton fitnessNewsTgl = v.findViewById(R.id.btnFitnessNews);
        fitnessNewsTgl.setChecked(true);


        //MyWebService.getNewsWeb(0, 1, newsList, adapter, getContext(), newsRV, NewsListFragment.this);
        foodNewsTgl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compoundButton.setEnabled(!b);
                fitnessNewsTgl.setChecked(!b);
                mNewsType=b ? 2 : 1;
                viewModel.init(mNewsType);
                mSwipeRefresh.setRefreshing(true);
                //waitingFL.setVisibility(View.VISIBLE);


                //   MyWebService.getNewsWeb(0, b ? 1 : 2, newsList, adapter, getContext(), newsRV, NewsListFragment.this);
            }
        });
        fitnessNewsTgl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                foodNewsTgl.setChecked(!b);
                compoundButton.setEnabled(!b);
            }
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

        viewModel.init(mNewsType);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

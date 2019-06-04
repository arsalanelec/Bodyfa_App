package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.activities.MessageRoomActivity;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.InboxItem;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.RetInboxList;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.viewModels.InboxItemListViewModel;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.EXTRA_PARTY_ID;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PARTY_NAME;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PARTY_THUMB;
import static com.example.arsalan.mygym.MyKeys.EXTRA_USER_ID;


public class InboxFragment extends Fragment implements Injectable {

    // the fragment initialization parameters
    private static final String ARG_USER = "param1";
    @Inject
    MyViewModelFactory factory;
    private List<InboxItem> privateMessageList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private ProgressBar waitingPB;
    private AdapterPm adapter;
    private InboxItemListViewModel viewModel;
    private long mUserId;

    public InboxFragment() {
        // Required empty public constructor
    }

    public static InboxFragment newInstance(long userId) {
        InboxFragment fragment = new InboxFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USER, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getLong(ARG_USER);
        } else {
            throw new RuntimeException("InboxFragment: Argument User Id Is Empty");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inbox, container, false);
        waitingPB = v.findViewById(R.id.pbWaiting);
        waitingPB.setVisibility(View.GONE);

        ListView listView = v.findViewById(R.id.listView);
        adapter = new AdapterPm();
        listView.setAdapter(adapter);
        //  getInboxList(mCurrentUser.getId());
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(InboxItemListViewModel.class);
        viewModel.init("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mUserId);

        viewModel.getInboxItemList().observe(this, inboxItems -> {
            Log.d("onActivityCreated", "observe: ");
            privateMessageList.removeAll(privateMessageList);
            privateMessageList.addAll(inboxItems);
            adapter.notifyDataSetChanged();
            waitingPB.setVisibility(View.GONE);
        });

        Log.d(getClass().getSimpleName(), "onActivityCreated: ");
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

    private void getInboxList(long userId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
/*        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        waitingDialog.setMessage("در حال دریافت لیست پیام ها...");
        waitingDialog.show();*/
        waitingPB.setVisibility(View.VISIBLE);
        Call<RetInboxList> call = apiService.getInboxList("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), userId, 0, 100);
        call.enqueue(new Callback<RetInboxList>() {
            @Override
            public void onResponse(Call<RetInboxList> call, Response<RetInboxList> response) {
                waitingPB.setVisibility(View.GONE);
                privateMessageList.removeAll(privateMessageList);
                privateMessageList.addAll(response.body().getRecords());
                adapter.notifyDataSetChanged();

                Log.d(getClass().getSimpleName(), "getInboxList.onResponse: cnt:" + response.body().getRecordsCount());
            }

            @Override
            public void onFailure(Call<RetInboxList> call, Throwable t) {
                waitingPB.setVisibility(View.GONE);
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class AdapterPm extends BaseAdapter {


        @Override
        public int getCount() {
            return privateMessageList.size();
        }

        @Override
        public InboxItem getItem(int i) {
            return privateMessageList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return privateMessageList.get(i).getId();
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getContext(), R.layout.item_inbox, null);
            }
            ImageView thumbImg = view.findViewById(R.id.imgThumbParty);
            Glide.with(getContext())
                    .load(MyConst.BASE_CONTENT_URL + getItem(i).getPartyThumbUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.bodybuilder_place_holder).circleCrop())
                    .apply(RequestOptions.circleCropTransform())
                    .into(thumbImg);

            TextView message = view.findViewById(R.id.txtMessage);
            message.setText(getItem(i).getMessage());

            TextView sentDateTV = view.findViewById(R.id.txtDateSent);
            sentDateTV.setText(getItem(i).getSendDate());

            TextView partyNameTV = view.findViewById(R.id.txtPartyName);
            partyNameTV.setText(getItem(i).getPartyName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MessageRoomActivity.class);
                    intent.putExtra(EXTRA_USER_ID, mUserId);
                    intent.putExtra(EXTRA_PARTY_ID, getItem(i).getPartyId());
                    intent.putExtra(EXTRA_PARTY_NAME, getItem(i).getPartyName());
                    intent.putExtra(EXTRA_PARTY_THUMB, getItem(i).getPartyThumbUrl());

                    getActivity().startActivity(intent);

                }
            });
            return view;
        }
    }
}

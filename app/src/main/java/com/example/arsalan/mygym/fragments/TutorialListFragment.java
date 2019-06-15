package com.example.arsalan.mygym.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.activities.PostTutorialActivity;
import com.example.arsalan.mygym.databinding.FragmentTutorialListBinding;
import com.example.arsalan.mygym.dialog.TutorialVideoListDialog;
import com.example.arsalan.mygym.models.RetTutorialList;
import com.example.arsalan.mygym.models.Tutorial;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.EXTRA_TUTORIAL_CAT_ID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TutorialListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TutorialListFragment#newInstance} mFactory method to
 * create an instance of this fragment.
 */
public class TutorialListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "TutorialListFragment";
    // TODO: Rename and change types of parameters
    private int mCatId;

    private OnFragmentInteractionListener mListener;
    private List<Tutorial> tutorialList;
    private AdapterTutorialList adapter;
    private boolean mCanSendTutorial;
    private FragmentTutorialListBinding mBind;

    public TutorialListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this mFactory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param catId           Parameter 1.
     * @param canSendTutorial Parameter 2.
     * @return A new instance of fragment TutorialFragment.
     */
    public static TutorialListFragment newInstance(int catId, boolean canSendTutorial) {
        TutorialListFragment fragment = new TutorialListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, catId);
        args.putBoolean(ARG_PARAM2, canSendTutorial);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCatId = getArguments().getInt(ARG_PARAM1);
            mCanSendTutorial = getArguments().getBoolean(ARG_PARAM2);
            Log.d(TAG, "onCreate: catId:" + mCatId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_tutorial_list, container, false);
        tutorialList = new ArrayList<>();
        adapter = new AdapterTutorialList(tutorialList);
        mBind.lstTutorial.setAdapter(adapter);
        mBind.btnGeneral.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) mBind.btnVip.setChecked(false);
            }
        });
        mBind.btnVip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) mBind.btnGeneral.setChecked(false);
            }
        });
        if (mCatId != 0) getTutorialWeb(mCatId);

        mBind.fab.setVisibility((mCanSendTutorial) ? View.VISIBLE : View.GONE);
        mBind.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(getActivity(), PostTutorialActivity.class);
                i.putExtra(EXTRA_TUTORIAL_CAT_ID, mCatId);
                startActivity(i);
            }
        });

        //Back Button
        mBind.ibBack.setOnClickListener(b -> container.setVisibility(View.GONE));
        // v.setRotation(180);
        return mBind.getRoot();
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

    void getTutorialWeb(int catId) {
        final String TAG = "getTutorialWeb";
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final ProgressDialog waitingDialog = new ProgressDialog(getContext());
        waitingDialog.setMessage(getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        Call<RetTutorialList> call = apiService.getTutorialList("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), catId);
        call.enqueue(new Callback<RetTutorialList>() {
            @Override
            public void onResponse(Call<RetTutorialList> call, Response<RetTutorialList> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful() && response.body().getRecords().size() > 0) {
                    tutorialList.addAll(response.body().getRecords());
                    adapter.notifyDataSetChanged();
                    // mBind.lstTutorial.scheduleLayoutAnimation();
                    Log.d(TAG, "onResponse: recordCnt:" + response.body().getRecordsCount());
                } else {
                    Log.d(TAG, "onResponse: message:" + response.message());
                }
            }

            @Override
            public void onFailure(Call<RetTutorialList> call, Throwable t) {
                waitingDialog.dismiss();
                Log.d(TAG, "onFailure: message:" + t.getMessage());
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

    class AdapterTutorialList extends BaseAdapter {
        List<Tutorial> tutorials;

        public AdapterTutorialList(List<Tutorial> tutorialList) {
            this.tutorials = tutorialList;
        }

        @Override
        public int getCount() {
            return tutorials.size();
        }

        @Override
        public Object getItem(int i) {
            return tutorials.get(i);
        }

        @Override
        public long getItemId(int i) {
            return tutorials.get(i).getId();
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.item_tutorial, viewGroup, false);
            TextView nameTV = view.findViewById(R.id.txtName);
            nameTV.setText(tutorials.get(i).getTitle());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TutorialVideoListDialog dialog = TutorialVideoListDialog.newInstance(tutorialList.get(i).getId());
                    dialog.show(getFragmentManager(), "");
                }
            });
            return view;
        }
    }
}

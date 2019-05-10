package com.example.arsalan.mygym.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.models.RetTutorialList;
import com.example.arsalan.mygym.models.Tutorial;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.dialog.TutorialVideoListDialog;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TutorialListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TutorialListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorialListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mCatId;

    private OnFragmentInteractionListener mListener;
    private List<Tutorial> tutorialList;
    private AdapterTutorialList adapter;
    private ListView listView;

    public TutorialListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param catId Parameter 1.
     * @return A new instance of fragment TutorialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TutorialListFragment newInstance(int catId) {
        TutorialListFragment fragment = new TutorialListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, catId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCatId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tutorial_list, container, false);
         listView = v.findViewById(R.id.lstTutorial);
        tutorialList = new ArrayList<>();
        adapter = new AdapterTutorialList(tutorialList);
        listView.setAdapter(adapter);
        final ToggleButton generatlBtn = v.findViewById(R.id.btnGeneral);
        final ToggleButton vipBtn = v.findViewById(R.id.btnVip);
        generatlBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) vipBtn.setChecked(false);
            }
        });
        vipBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) generatlBtn.setChecked(false);
            }
        });
        if (mCatId != 0) getTutorialWeb(mCatId);
        // v.setRotation(180);
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
                    listView.scheduleLayoutAnimation();
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
                    TutorialVideoListDialog dialog =  TutorialVideoListDialog.newInstance(tutorialList.get(i).getId());
                    dialog.show(getFragmentManager(), "");
                }
            });
            return view;
        }
    }
}

package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.R;


public class SelectRoleFragment extends Fragment {

    private static final String ARG_PARAM1="ARG_PARAM1";
    private OnFragmentInteractionListener mListener;
    private EditText mobileET;
    private int selectedIndex;
    private String  choice="athlete";
    private User mUser;

    public SelectRoleFragment() {
        // Required empty public constructor
    }



    public static SelectRoleFragment newInstance(User user) {
        SelectRoleFragment fragment = new SelectRoleFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUser = getArguments().getParcelable(ARG_PARAM1);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_role, container, false);
        RadioGroup radioGroup = v.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            switch (i) {
                case R.id.rbAthlete:
                    choice= MyKeys.KEY_ROLE_ATHLETE;
                    return;
                case R.id.rbTrainer:
                    choice = MyKeys.KEY_ROLE_TRAINER;
                    return;
                case R.id.rbGym:
                    choice = MyKeys.KEY_ROLE_GYM;
                    return;
            }
        });

        Button sendBtn = v.findViewById(R.id.btnSend);
        sendBtn.setOnClickListener(view -> mListener.setRoleWeb(choice,mUser));
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void setRoleWeb(String  choice,User user);

    }
}

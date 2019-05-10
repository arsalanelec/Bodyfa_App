package com.example.arsalan.mygym.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.arsalan.mygym.R;


public class SendMobileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERNAME = "param1";
    private static final String ARG_PASSWORD = "param2";

    // TODO: Rename and change types of parameters
    private String mUsername;
    private String mPassword;

    private OnFragmentInteractionListener mListener;
    private EditText mobileET;
    private int selectedIndex;
    private CountDownTimer countTimer;

    public SendMobileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SendMobileFragment newInstance(String username, String password) {
        SendMobileFragment fragment = new SendMobileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_send_mobile, container, false);
        mobileET = v.findViewById(R.id.etMobile);
        final Button loginBtn = v.findViewById(R.id.btnSendMobile);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobileET.getText().length() != 11 || !mobileET.getText().toString().startsWith("09")) {
                    mobileET.setError(getString(R.string.wrong_mobile_number));
                    return;
                }
                mListener.sendMobileWeb(mobileET.getText().toString());
                countTimer = new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        loginBtn.setText(getString(R.string.submit_again_second, (int) (millisUntilFinished / 1000)));
                        loginBtn.setEnabled(false);
                        loginBtn.setTextColor(Color.GRAY);
                    }

                    public void onFinish() {
                        loginBtn.setText(getString(R.string.submit_again));
                        loginBtn.setEnabled(true);
                        loginBtn.setTextColor(Color.BLACK);
                    }
                };
                countTimer.start();

            }
        });

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
        if (countTimer != null)
            countTimer.cancel();
    }

    public interface OnFragmentInteractionListener {
        void sendMobileWeb(String mobile);

    }
}

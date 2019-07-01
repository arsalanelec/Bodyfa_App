package com.example.arsalan.mygym.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.arsalan.mygym.R;


public class SendActivationCodeFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private EditText activationCodeET;
    private int selectedIndex;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERID = "param1";
    private static final String ARG_MOBILE = "param2";

    private long mUserId;
    private String mMobile;

    public SendActivationCodeFragment() {
        // Required empty public constructor
    }

    public static SendActivationCodeFragment newInstance(long userId, String mobile) {
        SendActivationCodeFragment fragment = new SendActivationCodeFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USERID, userId);
        args.putString(ARG_MOBILE, mobile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getLong(ARG_USERID);
            mMobile = getArguments().getString(ARG_MOBILE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_send_activation_code, container, false);
        final Button sendBtn = v.findViewById(R.id.btnSendActivationCode);
        activationCodeET = v.findViewById(R.id.etCode);
        activationCodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() >= 5) {
                    sendBtn.setEnabled(true);
                } else {
                    sendBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        activationCodeET.requestFocus();

        final TextView errorTV = v.findViewById(R.id.txtError);

        TextView resendTV = v.findViewById(R.id.txtResend);
        resendTV.setOnClickListener(view -> mListener.resendActCode());

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBtn.setEnabled(false);
                sendBtn.setText(getString(R.string.sending));
                mListener.checkActivation(mUserId, mMobile, activationCodeET.getText().toString(), new OnCheckActivation() {

                    @Override
                    public void activationFailed() {
                        sendBtn.setText(getString(R.string.send));
                        sendBtn.setEnabled(true);
                        errorTV.animate()
                                .alpha(1.0f)
                                .setDuration(300)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        errorTV.setVisibility(View.VISIBLE);
                                    }
                                });
                    }
                });

              /*  new CountDownTimer(30000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        sendBtn.setText("ارسال مجدد(" + millisUntilFinished / 1000+")");
                        sendBtn.setEnabled(false);
                        sendBtn.setTextColor(Color.GRAY);
                    }

                    public void onFinish() {
                        sendBtn.setText(getString(R.string.submit_again));
                        sendBtn.setEnabled(true);
                        sendBtn.setTextColor(Color.BLACK);
                    }
                }.start();*/
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
    }

    public interface OnCheckActivation {
        void activationFailed();
    }

    public interface OnFragmentInteractionListener {
        void checkActivation(long userId, String mobile, String messageText, OnCheckActivation onCheckActivation);
        void resendActCode();
    }
}

package com.example.arsalan.mygym.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.arsalan.mygym.R;


public class LoginFragment extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_USERNAME = "param1";
        private static final String ARG_PASSWORD = "param2";

        // TODO: Rename and change types of parameters
        private String mUsername;
        private String mPassword;

    private OnFragmentInteractionListener mListener;
    private EditText username;
    private EditText password;
    private int selectedIndex;

    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String username, String password) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUsername = getArguments().getString(ARG_USERNAME);
            mPassword = getArguments().getString(ARG_PASSWORD);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        TextView registerTV = v.findViewById(R.id.txtRegister);
        selectedIndex = 0;
        final AlertDialog dialog1 = new AlertDialog.Builder(getContext())
                .setSingleChoiceItems(new String[]{getString(R.string.athlete), getString(R.string.trainer), getString(R.string.gym_owner)}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedIndex = i;
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.next), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            mListener.gotoRegistrationPage(selectedIndex);
                            dialogInterface.dismiss();

                    }
                }).create();

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.show();
            }
        });
        username = v.findViewById(R.id.txtUsername);
        password = v.findViewById(R.id.txtPassword);

        if(mUsername!=null)username.setText(mUsername);
        if(mPassword!=null) password.setText(mPassword);

        Button loginBtn = v.findViewById(R.id.btnSendMobile);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().length() == 0) {
                    username.setError(getString(R.string.empty_user_name));
                    return;
                } else {
                   // waitingDialog.show();
                    mListener.login(username.getText().toString(), password.getText().toString());
                }
                /*Intent i = new Intent();

                i.setClass(getActivity(), MainActivity.class);
                if (username.getText().toString().equals("o")) {
                    i.putExtra("KEY", MainActivity.KEY_OMOMI);
                } else if (username.getText().toString().equals("v")) {
                    i.putExtra("KEY", MainActivity.KEY_VARZESHKAR);
                }
                startVideoRecorderActivity(i);*/
            }
        });

        /*ImageButton switchBtn = v.findViewById(R.id.btnSwitch);
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(getActivity(), MainActivity.class);
                i.putExtra("KEY", MainActivity.KEY_OMOMI);
                startVideoRecorderActivity(i);
            }
        });*/
        return v;
    }
/*
    public void login(String userName, String password) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Token> call = apiService.getToken("password", password, userName);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("login.onResponse", "onResponse: " + response.body().getToken());

                        Intent i = new Intent();
                        i.setClass(getActivity(), MainActivity.class);
                        i.putExtra("KEY", MainActivity.KEY_VARZESHKAR);
                        startVideoRecorderActivity(i);
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Log.d("login.onResponse", "onResponse: not Success! " +
                                jsonObject.getString("error"));
                        if (jsonObject.getString("error") != null && jsonObject.getString("error").contains("invalid_grant")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("نام کاربری و یا رمز ورود اشتباه است!").create().show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                waitingDialog.dismiss();
                Log.d("login.onFailure", "onFailure ");
            }
        });

    }
*/


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
        void login(String username, String password);

        void gotoRegistrationPage(int choice);

    }
}

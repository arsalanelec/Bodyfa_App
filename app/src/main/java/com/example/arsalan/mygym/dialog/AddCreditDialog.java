package com.example.arsalan.mygym.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.arsalan.mygym.R;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddCreditDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddCreditDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCreditDialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String current;
    // TODO: Rename and change types of parameters
    private long mUserId;
    private int mCreditSt;
    private static final String TAG = "AddCreditDialog";
    private OnFragmentInteractionListener mListener;

    public AddCreditDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCreditDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCreditDialog newInstance(long userId, int param2) {
        AddCreditDialog fragment = new AddCreditDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, userId);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getLong(ARG_PARAM1);
            mCreditSt = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        current = "";
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dialog_add_credit, container, false);
        EditText amountEt = v.findViewById(R.id.etAmount);
        amountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0 && !s.toString().equals(current)) {
                    amountEt.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = Double.parseDouble(cleanString);

                    DecimalFormat formatter = new DecimalFormat("###,###");
                    String formatted =formatter.format(parsed);// NumberFormat.getCurrencyInstance().format(parsed);
                    current = formatted;
                    amountEt.setText(formatted);
                    amountEt.setSelection(formatted.length());

                    amountEt.addTextChangedListener(this);
                }
            }
        });
        TextView currentBalanceTv=v.findViewById(R.id.txtCurrentBalance);
        currentBalanceTv.setText(getString(R.string.your_balance_is,mCreditSt));
        TextInputLayout tl=v.findViewById(R.id.tlAmount);
        Button submitBtn=v.findViewById(R.id.btnSubmit);
        submitBtn.setOnClickListener(b->{
           int amount = 0;
            try {
                amount = Integer.parseInt(current.toString().replaceAll("[$,.]", ""));
            }catch (NumberFormatException e){
                Log.d(TAG, "onCreateView: Amount is not an integer!");
                tl.setError(getString(R.string.AMOUNT_IS_WRONG));
                amountEt.requestFocus();
                return;
            }
            if(amount<1000){

                tl.setError(getString(R.string.AMOUNT_IS_WRONG));
                amountEt.requestFocus();
                return;
            }
            String uri = Uri.parse("http://bodyfa.ir/users/PaymentsManagement/Payments/")
                    .buildUpon()
                    .appendQueryParameter("userId", String.valueOf(mUserId))
                    .appendQueryParameter("amount", String.valueOf(amount))
                    .build().toString();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(browserIntent);
            dismiss();
        });
        return v;
    }

    @Override
    public void onStart() {

        super.onStart();
       /* getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);*/
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog= super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
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

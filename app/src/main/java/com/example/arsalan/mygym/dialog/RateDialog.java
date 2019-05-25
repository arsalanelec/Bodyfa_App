package com.example.arsalan.mygym.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;

import com.example.arsalan.mygym.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RateDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RateDialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ATHLETE_ID = "param1";
    private static final String ARG_TRAINER_ID = "param2";
    private static final String ARG_RATE = "param3";

    // TODO: Rename and change types of parameters
    private long mRate;
    private long mAthleteId;
    private long mTrainerId;

    private OnFragmentInteractionListener mListener;


    public RateDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RateDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static RateDialog newInstance(long athleteId,long trainerId) {
        RateDialog fragment = new RateDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_ATHLETE_ID, athleteId);
        args.putLong(ARG_TRAINER_ID, trainerId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAthleteId = getArguments().getLong(ARG_ATHLETE_ID, 0);
            mTrainerId= getArguments().getLong(ARG_TRAINER_ID,0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dialog_rate, container, false);
        RatingBar ratingBar = v.findViewById(R.id.rateBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mListener.setTrainerRate(mAthleteId,mTrainerId,(int) v);
                dismiss();
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
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
        void setTrainerRate(long athleteId,long trainerId,int rate);
    }
}

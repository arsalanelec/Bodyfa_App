package com.example.arsalan.mygym.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.databinding.DialogSelectTrainerJoinTimeBinding;
import com.example.arsalan.mygym.models.RetResponseStatus;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.webservice.MyWebService;

import static com.example.arsalan.mygym.models.MyConst.BASE_CONTENT_URL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectTrainerJoinTimeDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectTrainerJoinTimeDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectTrainerJoinTimeDialog extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_AMOUNT = "amount";

    private Trainer mTrainer;
    private long mAthleteId;
    private OnFragmentInteractionListener mListener;
    private String mSelectedTimeline="";
    private int mAmount;

    public SelectTrainerJoinTimeDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param trainer Parameter 1.
     * @return A new instance of fragment SelectTrainerJoinTimeDialog.
     */
    public static SelectTrainerJoinTimeDialog newInstance(long athleteId,Trainer trainer) {
        SelectTrainerJoinTimeDialog fragment = new SelectTrainerJoinTimeDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, athleteId);
        args.putParcelable(ARG_PARAM2, trainer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAthleteId = getArguments().getLong(ARG_PARAM1);
            mTrainer = getArguments().getParcelable(ARG_PARAM2);
        }
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DialogSelectTrainerJoinTimeBinding bind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_select_trainer_join_time, container, false);

        bind.imgAvatar.setImageURI(BASE_CONTENT_URL + mTrainer.getThumbUrl());
        bind.setTrainer(mTrainer);
        bind.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            bind.btnSubmit.setEnabled(true);
            switch (checkedId){
                case R.id.rbOrderOneDay:
                    mSelectedTimeline = "oneday";
                    mAmount=mTrainer.getOneDayFee();
                    break;
                case R.id.rbOrderOneWeek:
                    mSelectedTimeline="weekly";
                    mAmount=mTrainer.getWeeklyFee();
                    break;
                case R.id.rbOrderTwelveDays:
                    mSelectedTimeline = "twelve";
                    mAmount=mTrainer.getTwelveDaysFee();
                    break;
                case R.id.rbOrderHalfMonth:
                    mSelectedTimeline="halfmonthly";
                    mAmount=mTrainer.getHalfMonthFee();
                    break;
                case R.id.rbOrderMonthly:
                    mSelectedTimeline="monthly";
                    mAmount=mTrainer.getMonthlyFee();
                    break;
            }

        });
        bind.btnCancel.setOnClickListener(b->dismiss());
        bind.btnSubmit.setOnClickListener(b->{
            ProgressDialog progress = new ProgressDialog(getContext());
            progress.setMessage(getString(R.string.please_wait_a_moment));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
            mListener.requestTrainerJoinPlanFromWeb(mAthleteId,mTrainer.getId(),mSelectedTimeline,mAmount).observe(this, status -> {
                if (status.getStatus() == MyWebService.STATUS_SUCCESS) {
                    progress.dismiss();
                    Toast.makeText(getContext(), R.string.done_successfully, Toast.LENGTH_SHORT).show();
                    dismiss();
                } else if (status.getStatus() == MyWebService.STATUS_FAIL) {
                    progress.dismiss();
                    if(status.getMessage()!=null || !status.getMessage().isEmpty()){
                        Toast.makeText(getContext(), R.string.error+": "+status.getMessage(), Toast.LENGTH_LONG).show();
                        dismiss();
                    }else {
                        Toast.makeText(getContext(), R.string.error_accord_try_again, Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                }
            });
        });
        return bind.getRoot();
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
        LiveData<RetResponseStatus> requestTrainerJoinPlanFromWeb(long athleteId, long trainerId, String selectedDuration, int amount);
    }
}

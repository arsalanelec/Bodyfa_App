package com.example.arsalan.mygym.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.UserCredit;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.UserCreditViewModel;
import com.example.arsalan.mygym.webservice.MyWebService;
import com.google.android.material.textfield.TextInputLayout;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequestWorkoutPlanDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequestWorkoutPlanDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestWorkoutPlanDialog extends DialogFragment implements Injectable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private long mUserId;
    private long mTrainerId;

    private int mCredit;
    private OnFragmentInteractionListener mListener;
    @Inject
    MyViewModelFactory mFactory;
    private UserCreditViewModel mCreditVm;
    private int mAmount;

    public RequestWorkoutPlanDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @return A new instance of fragment RequestWorkoutPlanDialog.
     */
    public static RequestWorkoutPlanDialog newInstance(long userId, long trainerId,int amount) {
        RequestWorkoutPlanDialog fragment = new RequestWorkoutPlanDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, userId);
        args.putLong(ARG_PARAM2, trainerId);
        args.putInt(ARG_PARAM3, amount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getLong(ARG_PARAM1);
            mTrainerId = getArguments().getLong(ARG_PARAM2);
            mAmount = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dialog_request_workout_plan, container, false);
        EditText titleEt = v.findViewById(R.id.etTitle);
        EditText descriptionEt = v.findViewById(R.id.etDescription);
        TextInputLayout titleLay = v.findViewById(R.id.layTitle);
        TextInputLayout descLay = v.findViewById(R.id.layDescription);
        titleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    titleLay.setError("");
                }
            }
        });
        descriptionEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    descLay.setError("");
                }
            }
        });
        Button subminBtn = v.findViewById(R.id.btnSubmit);
        subminBtn.setOnClickListener(b -> {
            boolean hasError = false;
            if (titleEt.getText().length() == 0) {
                titleLay.setError(getString(R.string.title_is_empty));
                hasError = true;
            }
            if (descriptionEt.getText().length() == 0) {
                descLay.setError(getString(R.string.field_cant_empty));

                hasError = true;
            }
            if (hasError) return;
            ProgressDialog progress = new ProgressDialog(getContext());
            progress.setMessage(getString(R.string.please_wait_a_moment));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
            mListener.requestWorkoutPlanFromWeb(mTrainerId, titleEt.getText().toString(), descriptionEt.getText().toString(),mAmount).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer status) {

                    if (status == MyWebService.STATUS_SUCCESS) {
                        progress.dismiss();
                        Toast.makeText(getContext(), R.string.done_successfully, Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else if (status == MyWebService.STATUS_FAIL) {
                        progress.dismiss();
                        Toast.makeText(getContext(), R.string.error_accord_try_again, Toast.LENGTH_LONG).show();
                    }
                }
            });
        });
        ImageButton cancelBtn = v.findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(b -> dismiss());
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
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
        LiveData<Integer> requestWorkoutPlanFromWeb(long trainedId, String title, String description,int amount);
    }
}

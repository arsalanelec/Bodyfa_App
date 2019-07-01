package com.example.arsalan.mygym.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.databinding.DialogRequestWorkoutPlanBinding;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.PlanProp;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.UserCreditViewModel;
import com.example.arsalan.mygym.webservice.MyWebService;
import com.google.gson.Gson;

import java.lang.reflect.Field;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequestWorkoutPlanDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequestWorkoutPlanDialog#newInstance} mFactory method to
 * create an instance of this fragment.
 */
public class RequestWorkoutPlanDialog extends DialogFragment implements Injectable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    @Inject
    MyViewModelFactory mFactory;
    private long mUserId;
    private long mTrainerId;
    private int mCredit;
    private OnFragmentInteractionListener mListener;
    private UserCreditViewModel mCreditVm;
    private int mAmount;

    public RequestWorkoutPlanDialog() {
        // Required empty public constructor
    }

    /**
     * Use this mFactory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @return A new instance of fragment RequestWorkoutPlanDialog.
     */
    public static RequestWorkoutPlanDialog newInstance(long userId, long trainerId, int amount) {
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
        DialogRequestWorkoutPlanBinding bind = DataBindingUtil.inflate(inflater, R.layout.dialog_request_workout_plan, container, false);

        bind.etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    bind.layTitle.setError("");
                }
            }
        });
        bind.npBelly.setVisibility(View.GONE);
        bind.npBelly.setFormatter(i -> i + " cm");
        bind.npBelly.setMaxValue(250);
        bind.npBelly.setMinValue(40);
        bind.npBelly.setValue(70);
        bind.npBelly.setVisibility(View.VISIBLE);

        setNumPickerFormat(bind.npBelly);
        setDividerColor(bind.npBelly, getResources().getColor(R.color.yellow));

        bind.npBlood.setMaxValue(7);
        bind.npBlood.setMinValue(0);
        bind.npBlood.setDisplayedValues(new String[]{"A+","A-","B+","B-","AB+","AB-","O-","O+"});
        setNumPickerFormat(bind.npBlood);
        setDividerColor(bind.npBlood, getResources().getColor(R.color.yellow));

        bind.npHeight.setMaxValue(240);
        bind.npHeight.setMinValue(80);
        bind.npHeight.setFormatter(i -> i + " cm");
        bind.npHeight.setValue(170);

        setNumPickerFormat(bind.npHeight);
        setDividerColor(bind.npHeight, getResources().getColor(R.color.yellow));

        bind.npWeight.setFormatter(i -> i + " kg");
        bind.npWeight.setMaxValue(250);
        bind.npWeight.setMinValue(25);
        bind.npWeight.setValue(65);
        setNumPickerFormat(bind.npWeight);
        setDividerColor(bind.npWeight, getResources().getColor(R.color.yellow));
        bind.btnSubmit.setOnClickListener(b -> {
            boolean hasError = false;
            if (bind.etTitle.getText().length() == 0) {
                bind.layTitle.setError(getString(R.string.title_is_empty));
                hasError = true;
            }

            if (hasError) return;
            ProgressDialog progress = new ProgressDialog(getContext());
            progress.setMessage(getString(R.string.please_wait_a_moment));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            PlanProp planProp=new PlanProp();
            planProp.setBloodType(bind.npBlood.getValue());
            planProp.setHeight(bind.npHeight.getValue());
            planProp.setWeight(bind.npWeight.getValue());
            planProp.setWaist(bind.npBelly.getValue());
            Gson gson=new Gson();

            mListener.requestWorkoutPlanFromWeb(mTrainerId, bind.etTitle.getText().toString(), gson.toJson(planProp), mAmount).observe(this, status -> {

                if (status == MyWebService.STATUS_SUCCESS) {
                    progress.dismiss();
                    Toast.makeText(getContext(), R.string.done_successfully, Toast.LENGTH_SHORT).show();
                    dismiss();
                } else if (status == MyWebService.STATUS_FAIL) {
                    progress.dismiss();
                    Toast.makeText(getContext(), R.string.error_accord_try_again, Toast.LENGTH_LONG).show();
                }
            });
        });


        bind.btnCancel.setOnClickListener(b -> dismiss());

        return bind.getRoot();
    }
    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    private void setNumPickerFormat(NumberPicker numberPicker) {
        try {
            Field f  = NumberPicker.class.getDeclaredField("mInputText");
            f.setAccessible(true);
            EditText inputText = (EditText) f.get( numberPicker);
            inputText.setFilters(new InputFilter[0]);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
        LiveData<Integer> requestWorkoutPlanFromWeb(long trainedId, String title, String description, int amount);
    }
}

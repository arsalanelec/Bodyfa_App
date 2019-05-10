package com.example.arsalan.mygym.dialog;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.arsalan.mygym.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPlanDialog extends DialogFragment {


    private OnFragmentInteractionListener mListener;

    public NewPlanDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_new_plan_dialog, container, false);
        final EditText nameET = v.findViewById(R.id.txtName);

        Button creatPlanBtn = v.findViewById(R.id.btnCreatPlan);
        creatPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameET.getText().toString().isEmpty()) {
                    nameET.setError(getString(R.string.plan_name_is_empty));
                    return;
                }
                int selectedPlan = 1;
                switch (((RadioGroup) v.findViewById(R.id.radioGroup)).getCheckedRadioButtonId()) {
                    case R.id.rbMeal:
                        selectedPlan = 1;
                        break;
                    case R.id.rbWorkout:
                        selectedPlan = 2;
                        break;
                }
                mListener.goToNewPlane(selectedPlan, nameET.getText().toString());
                getDialog().dismiss();

            }
        });
        return v;
    }

    @Override
    public void onStart() {

        super.onStart();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

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
        void goToNewPlane(int planType, String name);
    }
}

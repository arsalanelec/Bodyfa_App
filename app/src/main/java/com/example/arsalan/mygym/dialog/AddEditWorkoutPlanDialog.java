package com.example.arsalan.mygym.dialog;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.RetTutorialList;
import com.example.arsalan.mygym.models.Tutorial;
import com.example.arsalan.mygym.models.TutorialGroup;
import com.example.arsalan.mygym.models.WorkoutRow;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddEditWorkoutPlanDialog extends DialogFragment {

    private static final String ARG_INDEX = "ARG_INDEX";
    private static final String ARG_WORKOUT_ROW = "ARG_WORKOUT_ROW";

    private WorkoutRow mWorkoutRow;

    private int mIndex = 0;

    private boolean mIsNew = true;

    private Spinner workoutItemSpn;
    private ArrayList<Tutorial> tutorialList = new ArrayList<>();

    public AddEditWorkoutPlanDialog() {
        // Required empty public constructor
    }


    public static AddEditWorkoutPlanDialog newInstance(int index, WorkoutRow workoutRow) {
        AddEditWorkoutPlanDialog fragment = new AddEditWorkoutPlanDialog();
        Bundle args = new Bundle();

        args.putInt(ARG_INDEX, index);
        args.putParcelable(ARG_WORKOUT_ROW, workoutRow);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddEditWorkoutPlanDialog newInstance(int index) {
        AddEditWorkoutPlanDialog fragment = new AddEditWorkoutPlanDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWorkoutRow = getArguments().getParcelable(ARG_WORKOUT_ROW);
            mIndex = getArguments().getInt(ARG_INDEX, 0);
            mIsNew = false;
        } else {
            mWorkoutRow = new WorkoutRow();
            mIsNew = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_edit_workout_plan_dialog, container, false);
        final ProgressBar waitingPB = v.findViewById(R.id.pdWaitingWorkout);

        workoutItemSpn = v.findViewById(R.id.spnWorkoutItm);

        final Spinner workoutGrpSpn = v.findViewById(R.id.spnWorkoutGrp);
        workoutGrpSpn.setAdapter(new AdapterWorkoutGrpSpinner());

        for (int cnt = 0; cnt < workoutGrpSpn.getCount(); cnt++) {
            if (workoutGrpSpn.getItemIdAtPosition(cnt) == mWorkoutRow.getGroupId()) {
                workoutGrpSpn.setSelection(cnt);
                getTutorialWeb((int) workoutGrpSpn.getSelectedItemId(), (int) mWorkoutRow.getWorkoutId(), waitingPB, workoutItemSpn);

                break;
            }
        }
        workoutGrpSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int ii, long l) {
                mWorkoutRow.setGroupId((int) adapterView.getSelectedItemId());
                getTutorialWeb((int) workoutGrpSpn.getSelectedItemId(), (int) mWorkoutRow.getGroupId(), waitingPB, workoutItemSpn);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        workoutItemSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int ii, long l) {
                mWorkoutRow.setWorkoutId((int) adapterView.getSelectedItemId());
                mWorkoutRow.setWorkoutName(((Tutorial) adapterView.getSelectedItem()).getTitle());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final EditText setET = v.findViewById(R.id.etSet);
        setET.setText(String.valueOf(mWorkoutRow.getSet()));
        setET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int j, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int j, int i1, int i2) {
                if (charSequence.toString().isEmpty() || !TextUtils.isDigitsOnly(charSequence.toString()) || Integer.parseInt(charSequence.toString()) == 0) {
                    setET.setText("1");
                    setET.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty() && TextUtils.isDigitsOnly(editable.toString())) {
                    mWorkoutRow.setSet(Integer.parseInt(editable.toString()));
                }
            }
        });
        final EditText setDurET = v.findViewById(R.id.etSetDurationِ);
        setDurET.setText(String.valueOf(mWorkoutRow.getSetDuration()));
        setDurET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int j, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int j, int i1, int i2) {
                if (charSequence.toString().isEmpty() || !TextUtils.isDigitsOnly(charSequence.toString()) || Integer.parseInt(charSequence.toString()) == 0) {
                    setDurET.setText("1");
                    setDurET.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty() && TextUtils.isDigitsOnly(editable.toString())) {
                    mWorkoutRow.setSetDuration(Integer.parseInt(editable.toString()));
                }
            }
        });
        final EditText repET = v.findViewById(R.id.etRep);
        repET.setText(String.valueOf(mWorkoutRow.getRep()));
        repET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int j, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int j, int i1, int i2) {
                if (charSequence.toString().isEmpty() || !TextUtils.isDigitsOnly(charSequence.toString()) || Integer.parseInt(charSequence.toString()) == 0) {
                    repET.setText("1");
                    repET.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty() && TextUtils.isDigitsOnly(editable.toString())) {
                    mWorkoutRow.setRep(Integer.parseInt(editable.toString()));
                }
            }
        });

        final EditText restET = v.findViewById(R.id.etRest);
        restET.setText(String.valueOf(mWorkoutRow.getRest()));
        restET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int j, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int j, int i1, int i2) {
                if (charSequence.toString().isEmpty() || !TextUtils.isDigitsOnly(charSequence.toString()) || Integer.parseInt(charSequence.toString()) == 0) {
                    restET.setText("1");
                    restET.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty() && TextUtils.isDigitsOnly(editable.toString())) {
                    mWorkoutRow.setRest(Integer.parseInt(editable.toString()));
                }
            }
        });

        Button submitBtn = v.findViewById(R.id.btnSubmit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(MyKeys.EXTRA_PARCLABLE_OBJ, mWorkoutRow);
                intent.putExtra(Intent.EXTRA_INDEX, mIndex);
                getTargetFragment().onActivityResult(getTargetRequestCode(), mIsNew ? MyKeys.RESULT_NEW : MyKeys.RESULT_EDIT, intent);
                dismiss();
            }
        });
        return v;
    }


    private class AdapterWorkoutGrpSpinner extends BaseAdapter {
        List<TutorialGroup> tutorialGroupList = TutorialGroup.getList();

        @Override
        public int getCount() {
            return tutorialGroupList.size();
        }

        @Override
        public TutorialGroup getItem(int i) {
            return tutorialGroupList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return tutorialGroupList.get(i).getId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getContext(), R.layout.item_spinner_simple, null);
            }
            TextView textView = view.findViewById(R.id.textView);
            textView.setText(getItem(i).getName());
            return view;

        }
    }


    private class AdapterWorkoutItemSpinner implements SpinnerAdapter {


        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public int getCount() {
            return tutorialList.size();
        }

        @Override
        public Tutorial getItem(int i) {
            return tutorialList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return tutorialList.get(i).getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getContext(), R.layout.item_spinner_simple, null);
            }
            TextView textView = view.findViewById(R.id.textView);
            textView.setText(getItem(i).getTitle());
            return view;

        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getContext(), R.layout.item_spinner_dropdown_simple, null);
            }
            TextView textView = view.findViewById(R.id.textView);
            textView.setText(getItem(i).getTitle());
            return view;
        }
    }

    void getTutorialWeb(int catId, final int workoutId, final View waitingView, final View finalView) {
        final String TAG = "getTutorialWeb";
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RetTutorialList> call = apiService.getTutorialList("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), catId);
        call.enqueue(new Callback<RetTutorialList>() {
            @Override
            public void onResponse(Call<RetTutorialList> call, Response<RetTutorialList> response) {
                waitingView.setVisibility(View.GONE);
                finalView.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    tutorialList.removeAll(tutorialList);

                    tutorialList.addAll(response.body().getRecords());
                    workoutItemSpn.setAdapter(new AdapterWorkoutItemSpinner());
                    if (workoutId > 0) {
                        for (int cnt = 0; cnt < workoutItemSpn.getCount(); cnt++) {
                            if (workoutItemSpn.getItemIdAtPosition(cnt) == workoutId) {
                                workoutItemSpn.setSelection(cnt);
                                break;
                            }
                        }
                    }
                    Log.d(TAG, "onResponse: recordCnt:" + response.body().getRecordsCount());
                } else {
                    Log.d(TAG, "onResponse: not Success:" + response.message());
                }
            }

            @Override
            public void onFailure(Call<RetTutorialList> call, Throwable t) {
                waitingView.setVisibility(View.GONE);
                finalView.setVisibility(View.VISIBLE);
                Log.d(TAG, "onFailure: message:" + t.getMessage());
            }
        });
    }

}

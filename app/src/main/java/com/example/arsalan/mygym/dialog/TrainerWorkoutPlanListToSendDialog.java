package com.example.arsalan.mygym.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalan.interfaces.OnGetPlanFromWeb;
import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.AdapterTrainerWorkoutPlanListSimple;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.models.WorkoutPlan;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.TrainerWorkoutListViewModel;
import com.example.arsalan.mygym.webservice.MyWebService;
import com.example.arsalan.room.TrainerWorkoutPlanRequestDao;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.example.arsalan.mygym.MyKeys.EXTRA_ATHLETE_ID;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PLAN_BODY;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PLAN_ID;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PLAN_TITLE;
import static com.example.arsalan.mygym.models.MyConst.BASE_API_URL;
import static com.example.arsalan.mygym.webservice.MyWebService.getTrainerWorkoutPlanWeb;
import static com.example.arsalan.mygym.webservice.MyWebService.sendWokroutPlanToAthleteWeb;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TrainerWorkoutPlanListToSendDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainerWorkoutPlanListToSendDialog extends DialogFragment implements Injectable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_REQUEST_ID = "requestId";
    private static final String ARG_TRAINER_ID = "trainerid";
    private static final String ARG_ATHLETE_ID = "athleteid";
    private static final String ARG_ATHLETE_NAME = "athletename";
    private static final String ARG_ATHLETE_THUMB = "Athletethumb";


    private TrainerWorkoutListViewModel workoutListViewModel;

    @Inject
    MyViewModelFactory factory;
    @Inject
    TrainerWorkoutPlanRequestDao mPlanRequestDao;
    private long mTrainerId;
    private int mRequestId;
    private long mAthleteId;
    private String mAthleteName;
    private String mAthleteThumb;
    private WorkoutPlan mSelectedPlan;

    public TrainerWorkoutPlanListToSendDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TrainerWorkoutPlanListToSendDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static TrainerWorkoutPlanListToSendDialog newInstance(Long param1) {
        TrainerWorkoutPlanListToSendDialog fragment = new TrainerWorkoutPlanListToSendDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<WorkoutPlan> mWorkoutPlanList = new ArrayList<>();
    private AdapterTrainerWorkoutPlanListSimple mAdapter;

    /**
     *
     * @param requestId
     * @param trainerId
     * @param athleteId
     * @param athleteName
     * @param athleteThumbUrl
     * @return
     */
    public static TrainerWorkoutPlanListToSendDialog newInstance(int requestId, long trainerId, long athleteId, String athleteName, String athleteThumbUrl) {
        TrainerWorkoutPlanListToSendDialog fragment = new TrainerWorkoutPlanListToSendDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_ID, requestId);
        args.putLong(ARG_TRAINER_ID, trainerId);
        args.putLong(ARG_ATHLETE_ID, athleteId);
        args.putString(ARG_ATHLETE_NAME, athleteName);
        args.putString(ARG_ATHLETE_THUMB, athleteThumbUrl);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTrainerId = getArguments().getLong(ARG_TRAINER_ID);
            mRequestId = getArguments().getInt(ARG_REQUEST_ID);
            mAthleteId = getArguments().getLong(ARG_ATHLETE_ID);
            mAthleteName = getArguments().getString(ARG_ATHLETE_NAME);
            mAthleteThumb = getArguments().getString(ARG_ATHLETE_THUMB);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_trainer_workout_plan_list_to_send, container, false);
        TextView athleteTv = v.findViewById(R.id.txtAthleteName);
        athleteTv.setText(mAthleteName);
        SimpleDraweeView thumbView=v.findViewById(R.id.imgAvatar);
        thumbView.setImageURI(BASE_API_URL + mAthleteThumb);
        /*Glide.with(getContext())
                .load(BASE_API_URL + mAthleteThumb)
                .apply(new RequestOptions().placeholder(R.drawable.avatar).centerCrop())
                .into(thumbView);*/
        //cancel when cross button clicked
        ImageButton cancelBtn=v.findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(b->dismiss());
        Button submitBtn=v.findViewById(R.id.btnSubmit);
        if (mWorkoutPlanList != null) {
            mAdapter = new AdapterTrainerWorkoutPlanListSimple(getDialog().getContext(), mWorkoutPlanList, (workoutPlan, position) -> {
                /*Intent intent = new Intent();
                intent.putExtra(MyKeys.EXTRA_PLAN_ID, workoutPlan.getTrainerWorkoutPlanId());
                getTargetFragment().onActivityResult(getTargetRequestCode(), MyKeys.RESULT_SEND, intent);*/
                submitBtn.setEnabled(true);
                mSelectedPlan=workoutPlan;
            });
            ListView planLV = v.findViewById(R.id.listView);
            planLV.setAdapter(mAdapter);
        }

        submitBtn.setOnClickListener(b->{
            LiveData<Integer> status = getTrainerWorkoutPlanWeb(getActivity(), mSelectedPlan.getTrainerWorkoutPlanId(), new OnGetPlanFromWeb() {
                @Override
                public void onGetPlan(long id, String title, String body) {

                    LiveData<Integer> status = sendWokroutPlanToAthleteWeb(getActivity(), mAthleteId, mSelectedPlan.getAthleteWorkoutPlanId(),title, body,mRequestId);
                    final ProgressDialog waitingDialog = new ProgressDialog(getActivity());
                    waitingDialog.setMessage(getString(R.string.sending_plan));
                    waitingDialog.show();
                    status.observe(TrainerWorkoutPlanListToSendDialog.this, resultStatus -> {
                        if(resultStatus!= MyWebService.STATUS_WAITING) {
                            waitingDialog.dismiss();

                        }
                        if(resultStatus==MyWebService.STATUS_SUCCESS) {
                            mPlanRequestDao.updateStatus(mRequestId,"sent");
                            dismiss();
                            Toast.makeText(getContext(), getString(R.string.done_successfully), Toast.LENGTH_LONG).show();

                        }else if(resultStatus==MyWebService.STATUS_FAIL){
                            Toast.makeText(getContext(), getString(R.string.error_accord_try_again), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        workoutListViewModel = ViewModelProviders.of(this, factory).get(TrainerWorkoutListViewModel.class);
        workoutListViewModel.init("Bearer " + ((MyApplication) getActivity().getApplication()).getCurrentToken().getToken(), mTrainerId);
        workoutListViewModel.getWorkoutPlanItemList().observe(this, newWorkoutPlans -> {
            mWorkoutPlanList.removeAll(mWorkoutPlanList);
            mWorkoutPlanList.addAll(newWorkoutPlans);
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}

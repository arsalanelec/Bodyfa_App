package com.example.arsalan.mygym.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.dialog.AddCreditDialog;
import com.example.arsalan.mygym.dialog.RequestWorkoutPlanDialog;
import com.example.arsalan.mygym.dialog.SelectTrainerJoinTimeDialog;
import com.example.arsalan.mygym.fragments.MyTrainerFragment;
import com.example.arsalan.mygym.models.RetResponseStatus;
import com.example.arsalan.mygym.webservice.MyWebService;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.example.arsalan.mygym.MyKeys.EXTRA_CREDIT_AMOUNT;
import static com.example.arsalan.mygym.MyKeys.EXTRA_IS_MY_TRAINER;
import static com.example.arsalan.mygym.MyKeys.EXTRA_TRAINER_ID;
import static com.example.arsalan.mygym.MyKeys.EXTRA_USER_ID;
import static com.example.arsalan.mygym.webservice.MyWebService.STATUS_FAIL;

public class ProfileTrainerActivity extends AppCompatActivity implements Injectable, HasSupportFragmentInjector, SelectTrainerJoinTimeDialog.OnFragmentInteractionListener, RequestWorkoutPlanDialog.OnFragmentInteractionListener {
    private static final String TAG = "ProfileTrainerActivity";
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    private long mUserId;
    private int mCreditAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_trainer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //  supportPostponeEnterTransition();

        //ImageView imgTrainer = findViewById(R.id.imgTrainer);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            throw new RuntimeException(getLocalClassName()+" should have extras from intent");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //  String imageTransitionName = extras.getString(EXTRA_IMAGE_TRANSITION_NAME);
            //  imgTrainer.setTransitionName(imageTransitionName);
        }
        long trainerId = extras.getLong(EXTRA_TRAINER_ID);
         mUserId = extras.getLong(EXTRA_USER_ID);
         mCreditAmount=extras.getInt(EXTRA_CREDIT_AMOUNT);
        boolean isMyCurrentTrainer = extras.getBoolean(EXTRA_IS_MY_TRAINER);
        Log.d(TAG, "onCreate: mUserId:" + mUserId + " trainerId:" + trainerId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, MyTrainerFragment.newInstance(mUserId, trainerId, isMyCurrentTrainer))
                .commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public LiveData<RetResponseStatus> requestTrainerJoinPlanFromWeb(long athleteId, long trainerGymId, String selectedDuration, int amount) {
        //check for enough balance
        if (mCreditAmount < amount) {
            AddCreditDialog dialog = AddCreditDialog.newInstance(mUserId, mCreditAmount);
            dialog.show(getSupportFragmentManager(), "");
            MutableLiveData<RetResponseStatus> status = new MutableLiveData<>();
            status.setValue(new RetResponseStatus(STATUS_FAIL, ""));
            return status;
        }
        return MyWebService.athleteMembershipRequestFromWeb(this, athleteId, trainerGymId, selectedDuration);
    }

    @Override
    public LiveData<Integer> requestWorkoutPlanFromWeb(long trainedId, String title, String description, int amount) {
        //check for enough balance
        if (mCreditAmount < amount) {
            AddCreditDialog dialog = AddCreditDialog.newInstance(mUserId, mCreditAmount);
            dialog.show(getSupportFragmentManager(), "");
            MutableLiveData<Integer> status = new MutableLiveData<>(-1);
            return status;
        }
        return MyWebService.requestWorkoutPlanFromWeb(this, mUserId, trainedId, title, description);
    }
    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}

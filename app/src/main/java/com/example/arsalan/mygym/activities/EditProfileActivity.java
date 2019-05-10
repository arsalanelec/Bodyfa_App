package com.example.arsalan.mygym.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.Gym;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.fragments.EditProfileFragment;
import com.example.arsalan.mygym.fragments.GymEditProfileFragment;
import com.example.arsalan.mygym.fragments.TrainerEditProfileFragment;

import javax.inject.Inject;

import static com.example.arsalan.mygym.MyKeys.EXTRA_OBJ_GYM;
import static com.example.arsalan.mygym.MyKeys.EXTRA_OBJ_TRAINER;
import static com.example.arsalan.mygym.MyKeys.EXTRA_OBJ_USER;
import static com.example.arsalan.mygym.MyKeys.EXTRA_ROLE_CHOICE;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_ATHLETE;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_GYM;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_TRAINER;

public class EditProfileActivity extends AppCompatActivity implements

        EditProfileFragment.OnFragmentInteractionListener,
        GymEditProfileFragment.OnFragmentInteractionListener,
        TrainerEditProfileFragment.OnFragmentInteractionListener , HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getStringExtra(EXTRA_ROLE_CHOICE).equals(KEY_ROLE_ATHLETE)) {
            User user = getIntent().getParcelableExtra(EXTRA_OBJ_USER);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, EditProfileFragment.newInstance(user)).commit();
        } else if (getIntent().getStringExtra(EXTRA_ROLE_CHOICE).equals(KEY_ROLE_TRAINER)) {
            Trainer trainer = getIntent().getParcelableExtra(EXTRA_OBJ_TRAINER);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, TrainerEditProfileFragment.newInstance(trainer)).commit();
        } else if (getIntent().getStringExtra(EXTRA_ROLE_CHOICE).equals(KEY_ROLE_GYM)) {
            Gym gym = getIntent().getParcelableExtra(EXTRA_OBJ_GYM);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, GymEditProfileFragment.newInstance(gym)).commit();

        }
    }


    @Override
    public void onSuccessfulEdited(User user) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onSuccessfulGymEditProfile() {
        onBackPressed();
    }


    @Override
    public void onSuccessfulTrainerEditProfile() {
        onBackPressed();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}

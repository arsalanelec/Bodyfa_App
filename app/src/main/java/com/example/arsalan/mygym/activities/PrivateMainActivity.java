package com.example.arsalan.mygym.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.ViewPagerVarzeshkarAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalan.mygym.models.Gym;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.dialog.NewPlanDialog;
import com.example.arsalan.mygym.dialog.RateDialog;
import com.example.arsalan.mygym.fragments.AthleteMealPlanListFragment;
import com.example.arsalan.mygym.fragments.AthleteWorkoutPlanListFragment;
import com.example.arsalan.mygym.fragments.DashBoardAthleteFragment;
import com.example.arsalan.mygym.fragments.DashBoardTrainerFragment;
import com.example.arsalan.mygym.fragments.GymListFragment;
import com.example.arsalan.mygym.fragments.HomeFragment;
import com.example.arsalan.mygym.fragments.InboxFragment;
import com.example.arsalan.mygym.fragments.MyAthleteListFragment;
import com.example.arsalan.mygym.fragments.MyGymFragment;
import com.example.arsalan.mygym.fragments.MyTrainerFragment;
import com.example.arsalan.mygym.fragments.NewsListFragment;
import com.example.arsalan.mygym.fragments.TrainerListFragment;
import com.example.arsalan.mygym.fragments.TrainerPlansTabFragment;
import com.example.arsalan.mygym.fragments.TutorialFragment;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.EXTRA_CURRENT_LANG;
import static com.example.arsalan.mygym.MyKeys.EXTRA_EDIT_MODE;
import static com.example.arsalan.mygym.MyKeys.EXTRA_EXIT_ACCOUNT;
import static com.example.arsalan.mygym.MyKeys.EXTRA_OBJ_GYM;
import static com.example.arsalan.mygym.MyKeys.EXTRA_OBJ_TRAINER;
import static com.example.arsalan.mygym.MyKeys.EXTRA_OBJ_USER;
import static com.example.arsalan.mygym.MyKeys.EXTRA_ROLE_CHOICE;
import static com.example.arsalan.mygym.MyKeys.KEY_CURRENT_LANG;
import static com.example.arsalan.mygym.MyKeys.KEY_PLAN_BODY;
import static com.example.arsalan.mygym.MyKeys.KEY_PLAN_ID;
import static com.example.arsalan.mygym.MyKeys.KEY_PLAN_NAME;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_ATHLETE;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_GYM;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_TRAINER;

public class PrivateMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , TutorialFragment.OnFragmentInteractionListener
        , NewPlanDialog.OnFragmentInteractionListener
        , TrainerPlansTabFragment.OnFragmentInteractionListener
        , DashBoardAthleteFragment.OnFragmentInteractionListener
        , AthleteMealPlanListFragment.OnFragmentInteractionListener
        , AthleteWorkoutPlanListFragment.OnFragmentInteractionListener
        , RateDialog.OnFragmentInteractionListener, HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private String currentRole = KEY_ROLE_ATHLETE;
    private PagerAdapter viewPagerAdapter;
    private TrainerPlansTabFragment mPlanFragment;

    private static final int REQUEST_ACT_MEALPLAN = 110;
    private static final int REQUEST_ACT_WORKOUTPLAN = 111;
    private User mCurrentUser;
    private Trainer mCurrentTrainer;
    private Gym mCurrentGym;
    private static final int nav_trainer_account = 809;
    private static final int nav_trainer_honors = 450;
    private static final int nav_gym_account = 910;
    private Context mContext;

    public PrivateMainActivity() {
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_private_main_drawer);
        Menu menu = navigationView.getMenu();

        if (((MyApplication) getApplication()).getCurrentUser().getRoleId() == 2) { //مربی
            MenuItem item1 = menu.add(R.id.menuGroup1, nav_trainer_account, 3, getString(R.string.trainer_profile));
            item1.setIcon(R.drawable.ic_person); // add icon with drawable resource

            MenuItem item2 = menu.add(R.id.menuGroup1, nav_trainer_honors, 4, getString(R.string.medals));
            item2.setIcon(R.drawable.ic_person); // add icon with drawable resource


        } else if (((MyApplication) getApplication()).getCurrentUser().getRoleId() == 1) {
            MenuItem item1 = menu.add(R.id.menuGroup1, nav_gym_account, 3, getString(R.string.gym_profile));
            item1.setIcon(R.drawable.ic_person); // add icon with drawable resource
        }
        Bundle xBundle = getIntent().getExtras();
        currentRole = xBundle.getString(EXTRA_ROLE_CHOICE);
        mCurrentUser = xBundle.getParcelable(EXTRA_OBJ_USER);

        switch (currentRole) {
            case KEY_ROLE_ATHLETE:
                break;
            case KEY_ROLE_TRAINER:
                mCurrentTrainer = xBundle.getParcelable(EXTRA_OBJ_TRAINER);
                break;
            case KEY_ROLE_GYM:
                mCurrentGym = xBundle.getParcelable(EXTRA_OBJ_GYM);
                break;

        }
        final ViewPager viewPager = findViewById(R.id.viewpager);
        Button switchBtn = toolbar.findViewById(R.id.btnSwitch);
        switchBtn.setText(getString(R.string.general));
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_ROLE_CHOICE, currentRole);
                intent.putExtra(EXTRA_OBJ_USER, mCurrentUser);
                switch (currentRole) {
                    case KEY_ROLE_ATHLETE:
                        break;
                    case KEY_ROLE_TRAINER:
                        intent.putExtra(EXTRA_OBJ_TRAINER, mCurrentTrainer);
                        break;
                    case KEY_ROLE_GYM:
                        intent.putExtra(EXTRA_OBJ_GYM, mCurrentGym);
                        break;

                }
                intent.setClass(PrivateMainActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.card_flip_left_in, R.anim.card_flip_left_out);
                PrivateMainActivity.this.finish();
            }
        });

        if (currentRole != null && currentRole.equals(KEY_ROLE_TRAINER)) {
            viewPagerAdapter = new ViewPagerTrainerAdapter(getSupportFragmentManager());
        } else if (currentRole != null && currentRole.equals(KEY_ROLE_ATHLETE)) {
            viewPagerAdapter = new ViewPagerVarzeshkarAdapter(getSupportFragmentManager(),PrivateMainActivity.this,mCurrentUser);

        } else {
            viewPagerAdapter = new ViewPagerVarzeshkarAdapter(getSupportFragmentManager(),PrivateMainActivity.this,mCurrentUser);

        }

        viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabs = findViewById(R.id.tablayout);
        tabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary_2));
        tabs.setupWithViewPager(viewPager);

        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);

            if (currentRole.equals(KEY_ROLE_TRAINER))
                tab.setCustomView(((ViewPagerTrainerAdapter) viewPagerAdapter).getTabView(i));
            else// if (currentRole.equals(KEY_ROLE_TRAINER))
                tab.setCustomView(((ViewPagerVarzeshkarAdapter) viewPagerAdapter).getTabView(i));

            TextView tv = (TextView) tab.getCustomView().findViewById(R.id.textView);
            tv.setTextColor(ContextCompat.getColor(this, tab.isSelected() ? R.color.colorPrimary_2 : R.color.colorAccent));
            if (tab.isSelected())
                tab.getCustomView().setBackgroundResource(R.drawable.white_rect_back);
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    tab.getCustomView().setBackgroundResource(R.drawable.white_rect_back);
                    TextView tv = (TextView) tab.getCustomView().findViewById(R.id.textView);
                    tv.setTextColor(ContextCompat.getColor(PrivateMainActivity.this, R.color.colorPrimary_2));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    tab.getCustomView().setBackgroundColor(ContextCompat.getColor(PrivateMainActivity.this, R.color.colorPrimary_2));
                    TextView tv = (TextView) tab.getCustomView().findViewById(R.id.textView);
                    tv.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_user_account) {
            Intent i = new Intent();
            i.putExtra(EXTRA_OBJ_USER, mCurrentUser);
            i.putExtra(EXTRA_ROLE_CHOICE, MyKeys.KEY_ROLE_ATHLETE);
            i.setClass(PrivateMainActivity.this, EditProfileActivity.class);
            startActivity(i);
        } else if (id == nav_trainer_account) {
            Intent i = new Intent();
            i.putExtra(EXTRA_OBJ_TRAINER, mCurrentTrainer);
            i.putExtra(EXTRA_ROLE_CHOICE, MyKeys.KEY_ROLE_TRAINER);
            i.setClass(PrivateMainActivity.this, EditProfileActivity.class);
            startActivity(i);
        } else if (id == nav_gym_account) {
            Intent i = new Intent();
            i.putExtra(EXTRA_OBJ_GYM, mCurrentGym);
            i.putExtra(EXTRA_ROLE_CHOICE, MyKeys.KEY_ROLE_GYM);
            i.setClass(PrivateMainActivity.this, EditProfileActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_faq) {

        }else if (id == R.id.nav_change_lang) {

            new android.app.AlertDialog.Builder(this,R.style.AlertDialogCustomPrivate)
                    .setMessage("لطفا زبان خود را انتخاب نمایید.\nPlease Choose Your Language.")
                    .setPositiveButton("فارسی", (dialogInterface, i) -> setLocale("fa"))
                    .setNegativeButton("English", (dialogInterface, i) -> setLocale("en"))
                    .create().show();

        }
        else if (id == R.id.nav_eula) {
            new android.app.AlertDialog.Builder(this)
                    .setMessage(R.string.BODYFA_EULA)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                    .create()
                    .show();
        } else if (id == R.id.nav_contact_us) {

        } else if (id == R.id.nav_user_send_tutorial) {
            Intent i = new Intent();
            i.setClass(PrivateMainActivity.this, PostTutorialActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_exit_account) {
            Intent i = new Intent();
            i.putExtra(EXTRA_EXIT_ACCOUNT, true);
            i.setClass(mContext, LoginActivity.class);
            startActivity(i);
            finish();
        } else if (id == nav_trainer_honors) {
            Intent i = new Intent();
            i.putExtra(EXTRA_OBJ_USER, mCurrentUser);
            i.setClass(mContext, HonorListActivity.class);
            startActivity(i);
        }else if (id == R.id.nav_eula) {
            new android.app.AlertDialog.Builder(this,R.style.AlertDialogCustomPrivate)
                    .setMessage(R.string.BODYFA_EULA)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                    .create()
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //از لیست گروه های آموزشی به لیست آموزش ها
    @Override
    public void goToTutorialList(int catId, String title) {
        Intent i = new Intent();
        i.putExtra(TutorialListActivity.KEY_CAT_ID, catId);
        i.putExtra(TutorialListActivity.KEY_TITLE, title);
        i.setClass(this, TutorialListActivity.class);
        startActivity(i);
    }

    @Override
    public void goToNewPlane(int planType, String name) {
        Intent intent = new Intent();
        intent.putExtra(KEY_PLAN_NAME, name);
        Log.d("MainActivity", "goToNewPlane: type:" + planType);
        if (planType == MyConst.MEAL_PLAN) {
            intent.setClass(this, NewMealPlaneActivity.class);
            intent.putExtra(EXTRA_EDIT_MODE, true);
            startActivityForResult(intent, REQUEST_ACT_MEALPLAN);
        } else if (planType == MyConst.WORKOUT_PLAN) {
            intent.setClass(this, NewWorkoutPlanActivity.class);
            intent.putExtra(EXTRA_EDIT_MODE, true);
            startActivityForResult(intent, REQUEST_ACT_WORKOUTPLAN);
        }
    }

    private void refreshMealPlanList() {
        if (mPlanFragment != null) {
            mPlanFragment.refreshMealPlanList();
        }
    }

    private void refreshWorkoutPlanList() {
        if (mPlanFragment != null) {
            mPlanFragment.refreshWorkoutPlanList();
        }
    }

    @Override
    public void addEditMealPlan(long planId, String planName, String planBody, boolean editMode) {
        Intent intent = new Intent();
        intent.setClass(this, NewMealPlaneActivity.class);
        intent.putExtra(KEY_PLAN_ID, planId);
        intent.putExtra(KEY_PLAN_BODY, planBody);
        intent.putExtra(KEY_PLAN_NAME, planName);
        intent.putExtra(EXTRA_EDIT_MODE, editMode);
        startActivity(intent);
    }

    @Override
    public void addEditWorkoutPlan(long planId, String planName, String planBody, boolean isEditable) {
        Intent intent = new Intent();

        intent.setClass(this, NewWorkoutPlanActivity.class);
        intent.putExtra(KEY_PLAN_ID, planId);
        intent.putExtra(KEY_PLAN_BODY, planBody);
        intent.putExtra(KEY_PLAN_NAME, planName);
        intent.putExtra(EXTRA_EDIT_MODE, isEditable);
        startActivity(intent);
    }

    @Override
    public void changeUserTrainer(long trainerId) {
        mCurrentUser.setTrainerId(trainerId);
    }

    private class ViewPagerOmoomiAdapter extends FragmentStatePagerAdapter {
        String[] titles = {getString(R.string.home), getString(R.string.newsHead), getString(R.string.training_tutorial), getString(R.string.trainers), getString(R.string.gyms)};

        public ViewPagerOmoomiAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            tv.setText(titles[position]);
            Typeface typeface = ResourcesCompat.getFont(PrivateMainActivity.this, R.font.iran_sans_mobile);
            tv.setTypeface(typeface);
            return v;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 1) return new NewsListFragment();
            if (position == 4) return new GymListFragment();
            if (position == 3) return new TrainerListFragment();
            if (position == 2) return new TutorialFragment();

            return new HomeFragment();
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

/*
    private class ViewPagerVarzeshkarAdapter extends FragmentStatePagerAdapter {
        String[] titles = {getString(R.string.my_dashboard), getString(R.string.my_gym), getString(R.string.my_trainer), getString(R.string.meal_plan), getString(R.string.workout_plan), getString(R.string.messages)};

        public ViewPagerVarzeshkarAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            tv.setText(titles[position]);
            Typeface typeface = ResourcesCompat.getFont(PrivateMainActivity.this, R.font.iran_sans_mobile);
            tv.setTypeface(typeface);
            return v;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return DashBoardAthleteFragment.newInstance(mCurrentUser);
                case 1:
                    return new MyGymFragment();
                case 2:
                    return MyTrainerFragment.newInstance(mCurrentUser);
                case 3:
                    return AthleteMealPlanListFragment.newInstance(mCurrentUser);
                case 4:
                    return AthleteWorkoutPlanListFragment.newInstance(mCurrentUser);
                case 5:
                    return InboxFragment.newInstance(mCurrentUser);
            }

            return new HomeFragment();
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
*/


    private class ViewPagerTrainerAdapter extends FragmentStatePagerAdapter {
        String[] titles = {getString(R.string.dashboard), getString(R.string.my_athleths), getString(R.string.plans), getString(R.string.orders), getString(R.string.transactions)};//, getString(R.string.messages)};

        public ViewPagerTrainerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            tv.setText(titles[position]);
            Typeface typeface = ResourcesCompat.getFont(PrivateMainActivity.this, R.font.iran_sans_mobile);
            tv.setTypeface(typeface);
            return v;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return DashBoardTrainerFragment.newInstance(mCurrentUser);
                case 1:
                    return MyAthleteListFragment.newInstance(mCurrentUser, mCurrentTrainer);
                case 2:
                    return TrainerPlansTabFragment.newInstance(mCurrentTrainer);
                case 3:
                    return new TutorialFragment();
                case 4:
                    return new TrainerListFragment();
               // case 5:
               //     return InboxFragment.newInstance(mCurrentUser);

            }
            return new HomeFragment();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

            switch (position) {
                case 2:
                    mPlanFragment = (TrainerPlansTabFragment) createdFragment;
                    break;
            }
            return createdFragment;
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ACT_MEALPLAN) {
            switch (resultCode) {
                case MyKeys.RESULT_OK:
                    refreshMealPlanList();
                    break;
            }
        } else if (requestCode == REQUEST_ACT_WORKOUTPLAN) {
            switch (resultCode) {
                case MyKeys.RESULT_OK:
                    refreshWorkoutPlanList();
                    break;
            }
        }

    }

    @Override
    public void setTrainerRate(long athleteId, long trainerId, int rate) {
        setTrainerRateWeb(athleteId, trainerId, rate);
    }

    private void setTrainerRateWeb(long athleteId, long trainerId, int rate) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Log.d(getClass().getSimpleName(), "setTrainerRateWeb: id:" + trainerId);
        Call<RetroResult> call = apiService.AddRateToTrainer("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), trainerId, athleteId, rate);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                Log.d(getClass().getSimpleName(), "onResponse: "+response.body().getMessage());
                if(response.body().getMessage().contains("شما یک بار به این مربی امتیاز داده اید")){
                    Toast.makeText(mContext, "شما یک بار به این مربی امتیاز داده اید.", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, "امتیاز شما ثبت شد", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                Log.d(getClass().getSimpleName(), "onFailure: ");
            }
        });
    }

    public void setLocale(String localeName) {
        Locale myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, LoginActivity.class);
        refresh.putExtra(EXTRA_CURRENT_LANG, localeName);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(KEY_CURRENT_LANG, localeName).commit();
        startActivity(refresh);
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}

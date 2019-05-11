package com.example.arsalan.mygym.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.databinding.ActivityNewWorkoutPlanBinding;
import com.example.arsalan.mygym.fragments.NewWorkoutPlanFragment;
import com.example.arsalan.mygym.fragments.WorkoutPlanFragment;
import com.example.arsalan.mygym.models.NextPrev;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.WorkoutPlanDay;
import com.example.arsalan.mygym.models.WorkoutRow;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.viewModels.NextPrevVm;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.EXTRA_EDIT_MODE;
import static com.example.arsalan.mygym.MyKeys.KEY_PLAN_BODY;
import static com.example.arsalan.mygym.MyKeys.KEY_PLAN_ID;
import static com.example.arsalan.mygym.MyKeys.KEY_PLAN_NAME;
import static com.example.arsalan.mygym.MyUtil.getStringFormatOfTime;

public class NewWorkoutPlanActivity extends AppCompatActivity implements
        HasSupportFragmentInjector,
        NewWorkoutPlanFragment.OnFragmentInteractionListener,
        WorkoutPlanFragment.OnFragmentInteractionListener {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    private final String TAG = "NewWorkoutPlanActivity";
    private List<WorkoutPlanDay> workoutPlanDayList;
    private String mTitle = "";

    private int mCurrentDay;
    private long mPlanId = 0;
    private String mPlanBody;

    private boolean isNew = true;
    private boolean mIsEditable = false;
    Context mContext;
    private ActivityNewWorkoutPlanBinding bind;
    private CountDownTimer countOneItem;
    private CountDownTimer countTotalTime;
    private MutableLiveData<CurrentPlayPauseFragment> mPlayPause = new MutableLiveData<>();
    private int mCurrentStep;

    public NewWorkoutPlanActivity() {
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(NewWorkoutPlanActivity.this, R.layout.activity_new_workout_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            mTitle = getIntent().getStringExtra(KEY_PLAN_NAME);
            mPlanId = getIntent().getLongExtra(KEY_PLAN_ID, 0);
            mPlanBody = getIntent().getStringExtra(KEY_PLAN_BODY);
            mIsEditable = getIntent().getBooleanExtra(EXTRA_EDIT_MODE, false);
        }
        if (mIsEditable) bind.layoutRunningActions.setVisibility(View.GONE);
        Gson gson = new Gson();
        try {
            workoutPlanDayList = gson.fromJson(mPlanBody, new TypeToken<List<WorkoutPlanDay>>() {
            }.getType());
        } catch (Exception ex) {
        }
        NextPrevVm nextPrevVm= ViewModelProviders.of(this).get(NextPrevVm.class);

        if (workoutPlanDayList != null && workoutPlanDayList.size() > 0) {
            isNew = false;
        } else {
            workoutPlanDayList = new ArrayList<>();
            workoutPlanDayList.add(new WorkoutPlanDay(1, new ArrayList<WorkoutRow>()));
            workoutPlanDayList.add(new WorkoutPlanDay(2, new ArrayList<WorkoutRow>()));
            workoutPlanDayList.add(new WorkoutPlanDay(3, new ArrayList<WorkoutRow>()));
            workoutPlanDayList.add(new WorkoutPlanDay(4, new ArrayList<WorkoutRow>()));
            workoutPlanDayList.add(new WorkoutPlanDay(5, new ArrayList<WorkoutRow>()));
            workoutPlanDayList.add(new WorkoutPlanDay(6, new ArrayList<WorkoutRow>()));
            workoutPlanDayList.add(new WorkoutPlanDay(7, new ArrayList<WorkoutRow>()));
        }
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if (mTitle != null && !mTitle.isEmpty() && mIsEditable) {
            //set title
            ((TextView) toolbar.findViewById(R.id.title)).setText((isNew ? getString(R.string.new2) : getString(R.string.edit2)) + mTitle);
        } else if (!mIsEditable) {
            ((TextView) toolbar.findViewById(R.id.title)).setText(mTitle);
        }
        VPWeekDaysAdapter vpAdapter = new VPWeekDaysAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(vpAdapter);
        viewPager.setOffscreenPageLimit(7);
        TabLayout tabs = findViewById(R.id.tablayout);
        tabs.setupWithViewPager(viewPager);
        TabLayout.Tab today = tabs.getTabAt(dayOfWeek);
        if(today!=null){
            tabs.selectTab(today);
        }
        bind.btnPausePlay.setOnClickListener(b -> {
            if (mPlayPause != null && mPlayPause.getValue() != null) {
                if (mPlayPause.getValue().isPaused) {
                    bind.btnPausePlay.setImageResource(R.drawable.ic_pause_24dp);
                    mPlayPause.setValue(new CurrentPlayPauseFragment(false, mCurrentDay));

                } else {
                    bind.btnPausePlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    mPlayPause.setValue(new CurrentPlayPauseFragment(true, mCurrentDay));
                }
            }
        });
         mCurrentStep=0;
        bind.btnGoForward.setOnClickListener(b -> {
            nextPrevVm.setNextPrev(new NextPrev(mCurrentDay, ++mCurrentStep));
        });

        bind.btnGoPrev.setOnClickListener(b ->
                nextPrevVm.setNextPrev(new NextPrev(mCurrentDay, --mCurrentStep)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void createWorkoutPlan(int dayOfWeek, List<WorkoutRow> workoutRowList) {
        workoutPlanDayList.get(dayOfWeek - 1).setWorkoutRows((ArrayList<WorkoutRow>) workoutRowList);
        String json = new Gson().toJson(workoutPlanDayList);
        Log.d(TAG, "workoutPlanDayList:" + json);
    }

    @Override
    public void setWorkoutPlanRow(WorkoutRow workoutPlanRow) {

    }


    @Override
    public void updateTimesUi(WorkoutRow workoutPlanRow, long itemTime, long remainedItemTime, long totalTime, long remainedTotalTime, int dayOfWeek) {
        bind.setWorkoutPlan(workoutPlanRow);

        bind.circularprogressbar2.setTitle(getStringFormatOfTime(remainedItemTime));
        bind.circularprogressbar2.setMax((int) (itemTime / 1000));
        bind.circularprogressbar2.setProgress((int) (remainedItemTime / 1000));

        String dayName = "";
        switch (dayOfWeek) {
            case 1:
                dayName = getString(R.string.saturday);
                break;
            case 2:
                dayName = getString(R.string.sunday);
                break;
            case 3:
                dayName = getString(R.string.monday);
                break;
            case 4:
                dayName = getString(R.string.tuesday);
                break;
            case 5:
                dayName = getString(R.string.wednesday);
                break;
            case 6:
                dayName = getString(R.string.thursday);
                break;
            case 7:
                dayName = getString(R.string.friday);
        }

        bind.txtDayOfWeek.setText(dayName);
        bind.txtTotalRemainedTime.setText(getStringFormatOfTime(remainedTotalTime));
    }



    @Override
    public void hasNextPrev(boolean hasNext, boolean hasPrev) {
        if (hasNext) bind.btnGoForward.setEnabled(true);
        else bind.btnGoForward.setEnabled(false);
        if (hasPrev) bind.btnGoPrev.setEnabled(true);
        else bind.btnGoPrev.setEnabled(false);
    }

    @Override
    public void setCurrentWorkoutDay(int currentWorkoutDay) {
        if (currentWorkoutDay != mCurrentDay) {
            mCurrentDay = currentWorkoutDay;
            mPlayPause.setValue(new CurrentPlayPauseFragment(true, currentWorkoutDay));
            bind.btnPausePlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }


    @Override
    public MutableLiveData<CurrentPlayPauseFragment> getPlayPause() {
        return mPlayPause;
    }


    private class VPWeekDaysAdapter extends FragmentPagerAdapter {
        String[] titles = {getString(R.string.saturday), getString(R.string.sunday), getString(R.string.monday), getString(R.string.tuesday), getString(R.string.wednesday), getString(R.string.thursday), getString(R.string.friday)};

        public VPWeekDaysAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.textView);

            tv.setText(titles[position]);
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.iran_sans_mobile);
            tv.setTypeface(typeface);
            return v;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            if (mIsEditable) {
                for (WorkoutPlanDay wpd : workoutPlanDayList) {
                    if (wpd != null && wpd.getDayOfWeek() == (position + 1)) {
                        Log.d(TAG, "getItem: " + wpd.toString());
                        return NewWorkoutPlanFragment.newInstance(position + 1, wpd.getWorkoutRows(), mIsEditable);
                    }
                }
            } else {
                for (WorkoutPlanDay wpd : workoutPlanDayList) {
                    if (wpd != null && wpd.getDayOfWeek() == (position + 1)) {
                        Log.d(TAG, "getItem: " + wpd.toString());
                        return WorkoutPlanFragment.newInstance(position + 1, wpd.getWorkoutRows());
                    }
                }
            }
            return NewWorkoutPlanFragment.newInstance(position + 1, mIsEditable);
        }

    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meal_plan, menu);
        menu.getItem(0).setVisible(mIsEditable);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.miSubmit) {
            Toast.makeText(this, getString(R.string.created), Toast.LENGTH_LONG).show();
            editTrainerWorkoutPlanWeb();
        }
        return super.onOptionsItemSelected(item);
    }


    private void editTrainerWorkoutPlanWeb() {
        RequestBody mealPlanIdReq = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mPlanId));
        RequestBody titleReq = RequestBody.create(MediaType.parse("text/plain"), mTitle);
        RequestBody descReq = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(workoutPlanDayList));

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("TrainerWorkoutPlanId", mealPlanIdReq);
        requestBodyMap.put("Title", titleReq);
        requestBodyMap.put("Description", descReq);
        Log.d(TAG, "editTrainerWorkoutPlanWeb: planID:" + mPlanId);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<RetroResult> call = apiService.editTrainerWorkoutPlan("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), ((MyApplication) getApplication()).getCurrentUser().getId(), requestBodyMap);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "editTrainerWorkoutPlanWeb:onResponse: " + response.body().getMessage());
                    setResult(MyKeys.RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                Log.d(TAG, "editTrainerWorkoutPlanWeb:onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    public static class CurrentPlayPauseFragment {
        boolean isPaused;
        int dayOfWeek;

        public CurrentPlayPauseFragment(boolean isPaused, int dayOfWeek) {
            this.isPaused = isPaused;
            this.dayOfWeek = dayOfWeek;
        }

        public CurrentPlayPauseFragment() {
        }

        public boolean isPaused() {
            return isPaused;
        }

        public void setPaused(boolean paused) {
            isPaused = paused;
        }

        public int getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(int dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }
    }

    public static class ForwardBackward {
        int stat;
        int day;

        public ForwardBackward(int stat, int day) {
            this.stat = stat;
            this.day = day;
        }

        public final static int FORWARD = 1;
        public final static int BACKWARD = 2;

        public ForwardBackward() {
        }

        public int getStat() {
            return stat;
        }

        public void setStat(int stat) {
            this.stat = stat;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }
}

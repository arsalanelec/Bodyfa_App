package com.example.arsalan.mygym.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.example.arsalan.mygym.fragments.MealPlanFragment;
import com.example.arsalan.mygym.fragments.NewMealPlanFragment;
import com.example.arsalan.mygym.models.MealPlanDay;
import com.example.arsalan.mygym.models.MealRow;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.EXTRA_EDIT_MODE;
import static com.example.arsalan.mygym.MyKeys.KEY_PLAN_BODY;
import static com.example.arsalan.mygym.MyKeys.KEY_PLAN_ID;
import static com.example.arsalan.mygym.MyKeys.KEY_PLAN_NAME;

public class NewMealPlaneActivity extends AppCompatActivity implements NewMealPlanFragment.OnFragmentInteractionListener {
    private final String TAG = "NewMealPlaneActivity";
    private List<MealPlanDay> mealPlanDayList;
    private String mTitle = "";

    private long mPlanId = 0;
    private String mPlanBody;


    private boolean isNew = true;
    private boolean isEditable = false;
    final Context mContext;

    public NewMealPlaneActivity() {
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal_plane);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            mTitle = getIntent().getStringExtra(KEY_PLAN_NAME);
            mPlanId = getIntent().getLongExtra(KEY_PLAN_ID, 0);
            mPlanBody = getIntent().getStringExtra(KEY_PLAN_BODY);
            isEditable = getIntent().getBooleanExtra(EXTRA_EDIT_MODE, false);
        }

        Gson gson = new Gson();

        try {


            mealPlanDayList = gson.fromJson(mPlanBody, new TypeToken<List<MealPlanDay>>() {
            }.getType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (mealPlanDayList != null && mealPlanDayList.size() > 0) {
            isNew = false;
        } else {
            mealPlanDayList = new ArrayList<>();
            mealPlanDayList.add(new MealPlanDay(1, new ArrayList<>()));
            mealPlanDayList.add(new MealPlanDay(2, new ArrayList<>()));
            mealPlanDayList.add(new MealPlanDay(3, new ArrayList<>()));
            mealPlanDayList.add(new MealPlanDay(4, new ArrayList<>()));
            mealPlanDayList.add(new MealPlanDay(5, new ArrayList<>()));
            mealPlanDayList.add(new MealPlanDay(6, new ArrayList<>()));
            mealPlanDayList.add(new MealPlanDay(7, new ArrayList<>()));
        }
        if (mTitle != null && !mTitle.isEmpty() && isEditable) {
            //set title
            ((TextView) toolbar.findViewById(R.id.title)).setText((isNew ? getString(R.string.new2) : getString(R.string.edit2)) + mTitle);
        } else {
            ((TextView) toolbar.findViewById(R.id.title)).setText(mTitle);
        }
        VPWeekDaysAdapter vpAdapter = new VPWeekDaysAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(vpAdapter);
        viewPager.setOffscreenPageLimit(7);
        TabLayout tabs = findViewById(R.id.tablayout);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void setNewMeal(int dayOfWeek, List<MealRow> mealRowList) {
        mealPlanDayList.get(dayOfWeek - 1).setMealRowList((ArrayList<MealRow>) mealRowList);

        String json = new Gson().toJson(mealPlanDayList);
        Log.d(TAG, "mealPlanDayList:" + json);

    }

    private class VPWeekDaysAdapter extends FragmentStatePagerAdapter {
        final String[] titles = {getString(R.string.saturday), getString(R.string.sunday), getString(R.string.monday), getString(R.string.tuesday), getString(R.string.wednesday), getString(R.string.thursday), getString(R.string.friday)};

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
            if (isEditable) {
                for (MealPlanDay mpw : mealPlanDayList) {
                    if (mpw != null && mpw.getDayOfWeek() == (position + 1)) {
                        Log.d(TAG, "getItem: " + mpw.toString());
                        return NewMealPlanFragment.newInstance(position + 1, mpw.getMealRowList(), isEditable);
                    }
                }
            } else {
                for (MealPlanDay mpw : mealPlanDayList) {
                    if (mpw != null && mpw.getDayOfWeek() == (position + 1)) {
                        Log.d(TAG, "getItem: " + mpw.toString());
                        return MealPlanFragment.newInstance(position + 1, mpw.getMealRowList());
                    }
                }
            }
            return NewMealPlanFragment.newInstance(position + 1, isEditable);
        }
    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meal_plan, menu);
        menu.getItem(0).setVisible(isEditable);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.miSubmit) {
            Toast.makeText(this, R.string.created, Toast.LENGTH_LONG).show();
            editTrainerMealPlanWeb();
        }
        return super.onOptionsItemSelected(item);
    }


    private void editTrainerMealPlanWeb() {
        RequestBody mealPlanIdReq = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mPlanId));
        RequestBody titleReq = RequestBody.create(MediaType.parse("text/plain"), mTitle);
        RequestBody descReq = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(mealPlanDayList));

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("TrainerMealPlanId", mealPlanIdReq);
        requestBodyMap.put("Title", titleReq);
        requestBodyMap.put("Description", descReq);
        Log.d(TAG, "editTrainerMealPlanWeb: planID:" + mPlanId);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<RetroResult> call = apiService.editTrainerMealPlan("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), ((MyApplication) getApplication()).getCurrentUser().getId(), requestBodyMap);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "editTrainerMealPlanWeb:onResponse: " + response.body().getMessage());
                    setResult(MyKeys.RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                Log.d(TAG, "editTrainerMealPlanWeb:onFailure: " + t.getMessage());
            }
        });
    }


}

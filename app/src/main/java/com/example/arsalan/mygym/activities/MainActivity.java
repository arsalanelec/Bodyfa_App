package com.example.arsalan.mygym.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.adapters.ViewPagerTrainerAdapter;
import com.example.arsalan.mygym.adapters.ViewPagerVarzeshkarAdapter;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.dialog.AddCreditDialog;
import com.example.arsalan.mygym.dialog.NewPlanDialog;
import com.example.arsalan.mygym.dialog.RateDialog;
import com.example.arsalan.mygym.dialog.RequestWorkoutPlanDialog;
import com.example.arsalan.mygym.dialog.SelectTrainerJoinTimeDialog;
import com.example.arsalan.mygym.fragments.AthleteMealPlanListFragment;
import com.example.arsalan.mygym.fragments.AthleteWorkoutPlanListFragment;
import com.example.arsalan.mygym.fragments.DashBoardAthleteFragment;
import com.example.arsalan.mygym.fragments.GymListFragment;
import com.example.arsalan.mygym.fragments.HomeFragment;
import com.example.arsalan.mygym.fragments.MyTrainerFragment;
import com.example.arsalan.mygym.fragments.MyTrainerMembershipFragment;
import com.example.arsalan.mygym.fragments.NewsListFragment;
import com.example.arsalan.mygym.fragments.TrainerListFragment;
import com.example.arsalan.mygym.fragments.TrainerOrderListFragment;
import com.example.arsalan.mygym.fragments.TrainerPlansTabFragment;
import com.example.arsalan.mygym.fragments.TutorialFragment;
import com.example.arsalan.mygym.models.Gym;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.RetResponseStatus;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.models.UserCredit;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.UserCreditViewModel;
import com.example.arsalan.mygym.webservice.MyWebService;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.stfalcon.swipeablebutton.SwipeableButton;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import co.ronash.pushe.Pushe;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.EXTRA_CURRENT_LANG;
import static com.example.arsalan.mygym.MyKeys.EXTRA_EDIT_MODE;
import static com.example.arsalan.mygym.MyKeys.EXTRA_EXIT_ACCOUNT;
import static com.example.arsalan.mygym.MyKeys.EXTRA_IS_MY_TRAINER;
import static com.example.arsalan.mygym.MyKeys.EXTRA_OBJ_GYM;
import static com.example.arsalan.mygym.MyKeys.EXTRA_OBJ_TRAINER;
import static com.example.arsalan.mygym.MyKeys.EXTRA_OBJ_USER;
import static com.example.arsalan.mygym.MyKeys.EXTRA_ROLE_CHOICE;
import static com.example.arsalan.mygym.MyKeys.EXTRA_TRAINER_ID;
import static com.example.arsalan.mygym.MyKeys.EXTRA_USER_ID;
import static com.example.arsalan.mygym.MyKeys.KEY_CURRENT_LANG;
import static com.example.arsalan.mygym.MyKeys.KEY_PLAN_BODY;
import static com.example.arsalan.mygym.MyKeys.KEY_PLAN_ID;
import static com.example.arsalan.mygym.MyKeys.KEY_PLAN_NAME;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_ATHLETE;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_GYM;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_TRAINER;
import static com.example.arsalan.mygym.webservice.MyWebService.STATUS_FAIL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , TutorialFragment.OnFragmentInteractionListener
        , HasSupportFragmentInjector
        , NewPlanDialog.OnFragmentInteractionListener
        , TrainerPlansTabFragment.OnFragmentInteractionListener
        , DashBoardAthleteFragment.OnFragmentInteractionListener
        , AthleteMealPlanListFragment.OnFragmentInteractionListener
        , AthleteWorkoutPlanListFragment.OnFragmentInteractionListener
        , RateDialog.OnFragmentInteractionListener
        , RequestWorkoutPlanDialog.OnFragmentInteractionListener
        , TrainerOrderListFragment.OnFragmentInteractionListener
        , SelectTrainerJoinTimeDialog.OnFragmentInteractionListener
,TrainerListFragment.OnFragmentInteractionListener
,MyTrainerMembershipFragment.OnFragmentInteractionListener
        , Injectable {
    private static final String KEY_THEME_ID = "key theme id";
    private static final String KEY_PIRVATE_VIEW = "key private view";
    private static final int nav_trainer_account = 399;
    private static final int nav_gym_account = 454;
    private static final int nav_trainer_honors = 976;
    private static final String TAG = "MainActivity";
    private static final int REQUEST_ACT_MEALPLAN = 1;
    private static final int REQUEST_ACT_WORKOUTPLAN = 2;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    MyViewModelFactory mFactory;
    private PagerAdapter mGeneratVpga;
    private User mCurrentUser;
    private Trainer mCurrentTrainer;
    private Gym mCurrentGym;
    private Context mContext;
    private int mThemeResId = R.style.AppTheme_NoActionBar;
    private boolean mPrivateView = false;
    private PagerAdapter mPrivateVpa;
    private Bundle eBundle;
    private UserCreditViewModel mUserCreditViewModel;
    private UserCredit mCredit;
    boolean doubleBackToExitPressedOnce=false;

    public MainActivity() {
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            eBundle = getIntent().getExtras();
        } else {

            throw new RuntimeException("the intent bundle should not be empty!");
        }
        final String roleName = eBundle.getString(EXTRA_ROLE_CHOICE);

        mPrivateView = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(KEY_PIRVATE_VIEW, false);
        setTheme(PreferenceManager.getDefaultSharedPreferences(this).getInt(KEY_THEME_ID, R.style.AppTheme_NoActionBar));

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate: theme:" + (mThemeResId == R.style.AppThemePrivate_NoActionBar ? "private" : "main"));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Menu menu = navigationView.getMenu();

        if (((MyApplication) getApplication()).getCurrentUser().getRoleId() == 2) { //مربی
            MenuItem item1 = menu.add(R.id.menuGroup1, nav_trainer_account, 3, getString(R.string.trainer_profile));
            item1.setIcon(R.drawable.ic_person_add_black_24dp); // add icon with drawable resource

            MenuItem item2 = menu.add(R.id.menuGroup1, nav_trainer_honors, 4, getString(R.string.medals));
            item2.setIcon(R.drawable.ic_person_add_black_24dp); // add icon with drawable resource


        } else if (((MyApplication) getApplication()).getCurrentUser().getRoleId() == 1) {
            MenuItem item1 = menu.add(R.id.menuGroup1, nav_gym_account, 3, getString(R.string.gym_profile));
            item1.setIcon(R.drawable.ic_person_add_black_24dp); // add icon with drawable resource
        }

        final ViewPager viewPager = findViewById(R.id.viewpager);

        mCurrentUser = eBundle.getParcelable(EXTRA_OBJ_USER);
        if (roleName.equals(KEY_ROLE_TRAINER)) {
            mCurrentTrainer = eBundle.getParcelable(EXTRA_OBJ_TRAINER);
        } else if (roleName.equals(KEY_ROLE_GYM)) {
            mCurrentGym = eBundle.getParcelable(EXTRA_OBJ_GYM);
        }

        Pushe.initialize(this, true);
        if (Pushe.isPusheInitialized(this)) {
            String pushehId = Pushe.getPusheId(this);
            addEditPushehId(pushehId);
            Toast.makeText(MainActivity.this, "Pusheh Id: " + pushehId, Toast.LENGTH_LONG).show();
            Log.d(getClass().getSimpleName(), "onCreate: pushe initialized!:" + pushehId);
            Pushe.subscribe(this, "Trainer");
            Pushe.subscribe(this, "Gym");
//            Pushe.sendSimpleNotifToUser(this, pushehId, "Hi", "It is a notification from app to itself");
            try {
                Pushe.sendCustomJsonToUser(this, pushehId, "{\"key\": \"It is a json from app to itself\"}");
            } catch (co.ronash.pushe.j.c c) {
                Log.e("Pushe-AS-Sample", c.getMessage());
                c.printStackTrace();
            }
        } else {
            Log.d(TAG, "onCreate: Pushe Not Initialized");
        }

        //جاگذاری نام و تصویر کاربر در هدر
        TextView userNameTV = navigationView.getHeaderView(0).findViewById(R.id.txtUserName);
        userNameTV.setText(mCurrentUser.getName());

        ImageView imgThumb = navigationView.getHeaderView(0).findViewById(R.id.imgThumb);

        Glide.with(this)
                .load(MyConst.BASE_CONTENT_URL + mCurrentUser.getThumbUrl())
                .apply(new RequestOptions().placeholder(R.drawable.bodybuilder_place_holder).circleCrop())
                .apply(RequestOptions.circleCropTransform())
                .into(imgThumb);
        //

        SwipeableButton switchBtn = toolbar.findViewById(R.id.btnSwitch);

        if(mPrivateView)switchBtn.setChecked(true);
        switchBtn.setOnSwipedListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                switchBtn.setEnabled(false);
                //swap view flag for private/general
                if (!mPrivateView) {
                    mPrivateView = true;
                    mThemeResId = R.style.AppThemePrivate_NoActionBar;

                } else {
                    mPrivateView = false;
                    mThemeResId = R.style.AppTheme_NoActionBar;

                }
                removeShortcut();
                addShortcut(mPrivateView);
                switch (mCurrentUser.getRoleName()) {
                    case KEY_ROLE_TRAINER: {
                        if (!mCurrentTrainer.isConfirmed()) {
                            final View titleView = View.inflate(mContext, R.layout.dialog_title, null);
                            TextView title = titleView.findViewById(R.id.textView);
                            title.setText(getString(R.string.incomplete_profile));
                            final View bodyView = View.inflate(mContext, R.layout.dialog_text, null);
                            TextView bodyText = bodyView.findViewById(R.id.textView);
                            bodyText.setText(getString(R.string.trainer_incomplete_profile_dialog));
                            final Dialog dialog = new AlertDialog.Builder(mContext, R.style.AlertDialogCustom)
                                    .setView(bodyView)
                                    .setCustomTitle(titleView)
                                    .setPositiveButton(getString(R.string.completing_profile), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int ii) {
                                            Intent i = new Intent();
                                            i.setClass(mContext, EditProfileActivity.class);
                                            i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, MyKeys.KEY_ROLE_TRAINER);
                                            i.putExtra(EXTRA_OBJ_USER, mCurrentUser);
                                            startActivity(i);
                                        }
                                    })
                                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    })
                                    .create();
                            dialog.setOnShowListener(dialog1 ->
                                    switchBtn.setEnabled(true));
                            dialog.show();
                        } else {
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean(KEY_PIRVATE_VIEW, mPrivateView).commit();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt(KEY_THEME_ID, mThemeResId).commit();
                            Intent intent = new Intent();
                            intent.setClass(mContext, MainActivity.class);
                            intent.putExtra(EXTRA_ROLE_CHOICE, KEY_ROLE_TRAINER);
                            intent.putExtra(EXTRA_OBJ_USER, mCurrentUser);
                            intent.putExtra(EXTRA_OBJ_TRAINER, mCurrentTrainer);
                            finish();
                            startActivity(intent);
                            // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            // overridePendingTransition(R.anim.card_flip_left_in, R.anim.card_flip_left_out);
                        }
                    }
                    break;
                    case KEY_ROLE_ATHLETE: {
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean(KEY_PIRVATE_VIEW, mPrivateView).commit();
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt(KEY_THEME_ID, mThemeResId).commit();
                        Intent intent = new Intent();
                        intent.setClass(mContext, MainActivity.class);
                        intent.putExtra(EXTRA_ROLE_CHOICE, mCurrentUser.getRoleName());
                        intent.putExtra(EXTRA_OBJ_USER, mCurrentUser);
                        finish();
                        startActivity(intent);
                    }
                    break;

                    case KEY_ROLE_GYM: {
                        if (!mCurrentGym.isConfirmed()) {
                            final View titleView = View.inflate(mContext, R.layout.dialog_title, null);
                            TextView title = titleView.findViewById(R.id.textView);
                            title.setText(R.string.incomplete_profile);
                            final View bodyView = View.inflate(mContext, R.layout.dialog_text, null);
                            TextView bodyText = bodyView.findViewById(R.id.textView);
                            bodyText.setText(getString(R.string.gym_incomplete_profile_dialog));
                            final Dialog dialog = new AlertDialog.Builder(mContext, R.style.AlertDialogCustom)
                                    .setView(bodyView)
                                    .setCustomTitle(titleView)
                                    .setPositiveButton(R.string.completing_profile, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int ii) {
                                            Intent i = new Intent();
                                            i.setClass(mContext, EditProfileActivity.class);
                                            i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, KEY_ROLE_GYM);
                                            i.putExtra(EXTRA_OBJ_USER, mCurrentUser);
                                            i.putExtra(EXTRA_OBJ_GYM, mCurrentGym);
                                            startActivity(i);
                                            overridePendingTransition(R.anim.card_flip_left_in, R.anim.card_flip_left_out);
                                        }
                                    })
                                    .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.cancel())
                                    .create();
                            dialog.setOnShowListener(dialog1 ->
                                    switchBtn.setEnabled(true));
                            dialog.show();
                        } else {
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean(KEY_PIRVATE_VIEW, mPrivateView).commit();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt(KEY_THEME_ID, mThemeResId).commit();
                            Intent intent = new Intent();
                            intent.setClass(mContext, MainActivity.class);
                            intent.putExtra(EXTRA_ROLE_CHOICE, KEY_ROLE_GYM);
                            intent.putExtra(EXTRA_OBJ_USER, mCurrentUser);
                            intent.putExtra(EXTRA_OBJ_GYM, mCurrentGym);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.card_flip_left_in, R.anim.card_flip_left_out);
                        }
                    }
                    break;
                }

                return null;
            }
        });


        ImageButton goToInboxBtn=findViewById(R.id.btnChatlist);
        if(!mCurrentUser.isConfirmed())goToInboxBtn.setVisibility(View.GONE);
        goToInboxBtn.setOnClickListener(b->{
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,ChatListActivity.class);
            intent.putExtra(EXTRA_USER_ID,mCurrentUser.getId());
            startActivity(intent);
        });
/*
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            }
        });
*/
        Log.d(TAG, "onCreate: stack count:" + getSupportFragmentManager().getBackStackEntryCount());
        //check and choose current view pager
        if (!mPrivateView) {
            if (mGeneratVpga == null) {
                mGeneratVpga = new ViewPagerOmoomiAdapter(getSupportFragmentManager());
                //switchBtn.setText(getText(R.string.dashboard));

            }
            viewPager.setAdapter(mGeneratVpga);
        } else if (roleName.equals(KEY_ROLE_ATHLETE)) {
           // switchBtn.setText(getText(R.string.general));
            if (mPrivateVpa == null) {
                mPrivateVpa = new ViewPagerVarzeshkarAdapter(getSupportFragmentManager(), MainActivity.this, mCurrentUser);
            }
            viewPager.setAdapter(mPrivateVpa);

        } else if (roleName.equals(KEY_ROLE_TRAINER)) {
           // switchBtn.setText(getText(R.string.general));
            if (mPrivateVpa == null) {
                mPrivateVpa = new ViewPagerTrainerAdapter(getSupportFragmentManager(), MainActivity.this, mCurrentUser, mCurrentTrainer);
            }
            viewPager.setAdapter(mPrivateVpa);
        }

        TabLayout tabs = findViewById(R.id.tablayout);
        if (tabs == null) {
            Log.d(TAG, "onCreate: tabs:Null");
            finish();
        }
        if (viewPager == null) {
            Log.d(TAG, "onCreate: viewPager:Null");
            finish();
        }
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled: page:" + position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
/*
        for (
                int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            tab.setCustomView(getCustomTabView(tab.getText()));
            TextView tv = (TextView) tab.getCustomView().findViewById(R.id.textView);
            tv.setTextColor(ContextCompat.getColor(this, tab.isSelected() ? R.color.colorPrimary : R.color.colorAccent));
            if (tab.isSelected())
                tab.getCustomView().setBackgroundResource(R.drawable.white_rect_back);
        }*/

/*        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    tab.getCustomView().setBackgroundResource(R.drawable.white_rect_back);
                    TextView tv = (TextView) tab.getCustomView().findViewById(R.id.textView);
                    tv.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    tab.getCustomView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    TextView tv = (TextView) tab.getCustomView().findViewById(R.id.textView);
                    tv.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
        mUserCreditViewModel = ViewModelProviders.of(this, mFactory).get(UserCreditViewModel.class);
        mUserCreditViewModel.init(mCurrentUser.getId());
        mUserCreditViewModel.getCredit().observe(this, credit -> {
            if (credit != null) {
                mCredit = credit;
                TextView creditTv = navigationView.getHeaderView(0).findViewById(R.id.txtUserCredit);
                creditTv.setText(credit.getCreditFromatted());
            }
        });
        navigationView.getHeaderView(0).findViewById(R.id.btnAddCredit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCreditDialog dialog = AddCreditDialog.newInstance(mCurrentUser.getId(), mCredit.getCredit());
                dialog.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void addShortcut(boolean addPrivateGeneralIcon) {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                LoginActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Bodyfa");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        addPrivateGeneralIcon?R.mipmap.ic_launcher_puprple:R.mipmap.ic_launcher_blue));

        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }
    private void removeShortcut() {

        //Deleting shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                LoginActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Bodyfa");

        addIntent
                .setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getData() != null) {
            String status = intent.getData().getQueryParameter("status");
            int messageRes = 0;
            if (status.equals("1")) {
                messageRes = R.string.PAYMENT_SUCCESS;
                mUserCreditViewModel.init(mCurrentUser.getId());
            } else {
                messageRes = R.string.PAYMENT_FAIL;
            }
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage(messageRes)
                    .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        }
        Log.d(TAG, "onNewIntent: intent " + (intent.getData() != null ? ("not null->" + intent.getData().toString()) : "is null"));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(mContext, R.string.press_back_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
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
            i.putExtra(EXTRA_ROLE_CHOICE, MyKeys.KEY_ROLE_ATHLETE);
            i.putExtra(EXTRA_OBJ_USER, mCurrentUser);
            i.setClass(mContext, EditProfileActivity.class);
            startActivity(i);
        } else if (id == nav_trainer_account) {
            Intent i = new Intent();
            i.putExtra(EXTRA_ROLE_CHOICE, MyKeys.KEY_ROLE_TRAINER);
            i.putExtra(EXTRA_OBJ_TRAINER, mCurrentTrainer);
            i.setClass(mContext, EditProfileActivity.class);
            startActivity(i);
        } else if (id == nav_trainer_honors) {
            Intent i = new Intent();
            i.putExtra(EXTRA_OBJ_USER, mCurrentUser);
            i.setClass(mContext, HonorListActivity.class);
            startActivity(i);
        } else if (id == nav_gym_account) {
            Intent i = new Intent();
            i.putExtra(EXTRA_ROLE_CHOICE, MyKeys.KEY_ROLE_GYM);
            i.putExtra(EXTRA_OBJ_GYM, mCurrentGym);
            i.setClass(mContext, EditProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_transactions) {
            Intent i = new Intent();
            i.setClass(MainActivity.this, TransactionListActivity.class);
            i.putExtra(EXTRA_USER_ID, mCurrentUser.getId());
            startActivity(i);
        } else if (id == R.id.nav_faq) {

        } else if (id == R.id.nav_change_lang) {

            new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                    .setMessage("لطفا زبان خود را انتخاب نمایید.\nPlease Choose Your Language.")
                    .setPositiveButton("فارسی", (dialogInterface, i) -> setLocale("fa"))
                    .setNegativeButton("English", (dialogInterface, i) -> setLocale("en"))
                    .create().show();

        } else if (id == R.id.nav_contact_us) {

        } else if (id == R.id.nav_exit_account) {
            Intent i = new Intent();
            i.putExtra(EXTRA_EXIT_ACCOUNT, true);
            i.setClass(mContext, LoginActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_eula) {
            new AlertDialog.Builder(this, R.style.AlertDialogCustom)
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

    ///////////////////////////////////////////////////////
    @Override
    public void goToNewPlane(int planType, String name) {
        Intent intent = new Intent();
        intent.putExtra(KEY_PLAN_NAME, name);
        Log.d(TAG, "goToNewPlane: type:" + planType);
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
                Log.d(getClass().getSimpleName(), "onResponse: " + response.body().getMessage());
                if (response.body().getMessage().contains("شما یک بار به این مربی امتیاز داده اید")) {
                    Toast.makeText(mContext, "شما یک بار به این مربی امتیاز داده اید.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "امتیاز شما ثبت شد", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                Log.d(getClass().getSimpleName(), "onFailure: ");
            }
        });
    }

    @Override
    public void changeUserTrainer(long trainerId) {
        mCurrentUser.setTrainerId(trainerId);
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
//////////////////////////////////////////////////////////////////////

    /**
     * send pushe id to server
     *
     * @param pushehId
     */
    private void addEditPushehId(final String pushehId) {
        MediaType plainMT = MediaType.parse("text/plain");
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("UserId", RequestBody.create(plainMT, String.valueOf(mCurrentUser.getId())));
        requestBodyMap.put("PublishId", RequestBody.create(plainMT, String.valueOf(pushehId)));
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<RetroResult> call = apiService.addEditPushehId("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), requestBodyMap);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                if (response.isSuccessful()) {
                    Log.d("addEditPushehId", "onResponse: on success: Userid:" + mCurrentUser.getId() + " pusheId:" + pushehId + " msg:" + response.message());
                } else {
                    Log.d("addEditPushehId", "onResponse: on Notsuccess code:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                Log.e("addEditPushehId", "onFailure: throws:" + t.getCause());
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
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(KEY_CURRENT_LANG, localeName).commit();
        refresh.putExtra(EXTRA_CURRENT_LANG, localeName);
        startActivity(refresh);
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onWorkoutPlanCancelRequest(long planId) {

    }

    @Override
    public LiveData<Integer> requestWorkoutPlanFromWeb(long trainedId, String title, String description, int amount) {
        //check for enough balance
        if (mCredit != null && mCredit.getCredit() < amount) {
            AddCreditDialog dialog = AddCreditDialog.newInstance(mCurrentUser.getId(), mCredit.getCredit());
            dialog.show(getSupportFragmentManager(), "");
            MutableLiveData<Integer> status = new MutableLiveData<>(-1);
            return status;
        }
        return MyWebService.requestWorkoutPlanFromWeb(this, mCurrentUser.getId(), trainedId, title, description);
    }

    @Override
    public LiveData<RetResponseStatus> requestTrainerJoinPlanFromWeb(long athleteId, long trainerGymId, String selectedDuration, int amount) {
        //check for enough balance
        if (mCredit != null && mCredit.getCredit() < amount) {
            AddCreditDialog dialog = AddCreditDialog.newInstance(mCurrentUser.getId(), mCredit.getCredit());
            dialog.show(getSupportFragmentManager(), "");
            MutableLiveData<RetResponseStatus> status = new MutableLiveData<>();
            status.setValue(new RetResponseStatus(STATUS_FAIL, ""));
            return status;
        }
        return MyWebService.athleteMembershipRequestFromWeb(this, athleteId, trainerGymId, selectedDuration);
    }

    @Override
    public void onGoToTrainerPage(long trainerId, boolean isMyTrainer) {
        Log.d(TAG, "onGoToTrainerPage: userId:"+mCurrentUser.getId());
        Intent i = new Intent();
        i.setClass(this, ProfileTrainerActivity.class);
        i.putExtra(EXTRA_TRAINER_ID, trainerId);
        i.putExtra(EXTRA_USER_ID, mCurrentUser.getId());
        i.putExtra(MyKeys.EXTRA_CREDIT_AMOUNT, mCredit.getCredit());
        i.putExtra(EXTRA_IS_MY_TRAINER,true);
        startActivity(i);
    }


    /**
     * view pager adapter for general view
     */
    private class ViewPagerOmoomiAdapter extends FragmentStatePagerAdapter {
        private final String[] titles = {getString(R.string.meal_and_workout_news), getString(R.string.tutorials), getString(R.string.trainers), getString(R.string.gyms)};


        public ViewPagerOmoomiAdapter(FragmentManager fm) {
            super(fm);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) return NewsListFragment.newInstance(mCurrentUser.getId());
            if (position == 1) return TutorialFragment.newInstance("", "");
            if (position == 2)
                return TrainerListFragment.newInstance(mCurrentUser.getId(), mCurrentUser.getTrainerId());
            if (position == 3) return GymListFragment.newInstance("", "");

            return new HomeFragment();
        }

        @Override
        public int getCount() {
            return titles.length;
        }


    }
}

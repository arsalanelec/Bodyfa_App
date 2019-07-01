package com.example.arsalan.mygym.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.SMSReceiver;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.fragments.EditProfileFragment;
import com.example.arsalan.mygym.fragments.SelectRoleFragment;
import com.example.arsalan.mygym.fragments.SendActivationCodeFragment;
import com.example.arsalan.mygym.fragments.SendMobileFragment;
import com.example.arsalan.mygym.models.Gym;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.RetroResultActivation;
import com.example.arsalan.mygym.models.Token;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.webservice.InternetCheck;
import com.example.arsalan.mygym.webservice.MyWebService;
import com.example.arsalan.mygym.webservice.WebServiceResultImplementation;
import com.example.arsalan.room.GymDao;
import com.example.arsalan.room.TrainerDao;
import com.example.arsalan.room.UserDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.EXTRA_CURRENT_LANG;
import static com.example.arsalan.mygym.MyKeys.EXTRA_EXIT_ACCOUNT;
import static com.example.arsalan.mygym.MyKeys.EXTRA_OBJ_USER;
import static com.example.arsalan.mygym.MyKeys.EXTRA_ROLE_CHOICE;
import static com.example.arsalan.mygym.MyKeys.EXTRA_USER_NAME;
import static com.example.arsalan.mygym.MyKeys.KEY_BUNDLE_OBJ;
import static com.example.arsalan.mygym.MyKeys.KEY_CURRENT_LANG;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_ATHLETE;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_GYM;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_TRAINER;

public class LoginActivity extends AppCompatActivity implements
        EditProfileFragment.OnFragmentInteractionListener
        , SendMobileFragment.OnFragmentInteractionListener
        , SelectRoleFragment.OnFragmentInteractionListener
        , SendActivationCodeFragment.OnFragmentInteractionListener, HasSupportFragmentInjector, Injectable {

    private static final int REQ_EDIT_USER_PROFILE = 1000;
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 110;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 111;
    private final String KEY_USERNAME = "myGymUserNameKey";
    private final String KEY_PASSWORD = "myGymPassword";
    private final String KEY_USER_ID = "myGymUserId";
    private final String TAG = "LoginActivity";
    private final Context mContext;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    SharedPreferences spf;
    @Inject
    Token mToken;
    @Inject
    UserDao userDao;
    @Inject
    TrainerDao trainerDao;
    @Inject
    GymDao gymDao;
    private ImageView splashImg;
    private SMSReceiver smsBroadcast;
    private String mUsername;
    private String mPassword;
    private long mUserId;
    private Locale myLocale;
    private String currentLanguage;

    public LoginActivity() {
        this.mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        splashImg = findViewById(R.id.imgSplash);


        currentLanguage = spf.getString(KEY_CURRENT_LANG, "");
        Toast.makeText(this, "lang:" + currentLanguage, Toast.LENGTH_LONG).show();
        if (getIntent().getStringExtra(EXTRA_CURRENT_LANG) != null) {
            setLocale(getIntent().getStringExtra(EXTRA_CURRENT_LANG));
        }
        if (currentLanguage == null || currentLanguage.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage("لطفا زبان خود را انتخاب نمایید.\nPlease Choose Your Language.")
                    .setPositiveButton("فارسی", (dialogInterface, i) -> setLocale("fa"))
                    .setNegativeButton("English", (dialogInterface, i) -> setLocale("en"))
                    .create().show();
        } else {
            setLocale(currentLanguage);
        }
        View mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        /*Executors.newFixedThreadPool(3).execute(new Runnable() {
            @Override
            public void run() {
                //ذخیره در دیتابیس
                User user1=new User();
                user1.setName("Test Name2");
                user1.setId(10);
                userDao.saveUser(user1);
                List<User> users = new ArrayList<>();

                for(int i=0;i<10;i++){
                    User user=new User();
                    user.setId(i+1);
                    users.add(user);
                }
                userDao.saveList(users);
                Log.d(TAG, "run: save:" + userDao.loadAllWaitingList().getValue() + " usename:" + user1.getName());
            }
        });*/
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.RECEIVE_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        MY_PERMISSIONS_REQUEST_RECEIVE_SMS);

                // MY_PERMISSIONS_REQUEST_READ_SMS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                        Manifest.permission.READ_SMS)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{Manifest.permission.READ_SMS},
                            MY_PERMISSIONS_REQUEST_READ_SMS);

                    // MY_PERMISSIONS_REQUEST_READ_SMS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                //     registerListenerForSms();
            }
        }


        // intent=new Intent(this,Registration.class);

//        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        //       startActivityForResult(intent,ACTIVITY_REGISTRATION1);

        smsBroadcast = new SMSReceiver();

        new InternetCheck(internet -> Toast.makeText(LoginActivity.this, "you are " + (internet ? "" : "NOT ") + "Connected to Internet!", Toast.LENGTH_LONG).show());

        if (getIntent().getBooleanExtra(EXTRA_EXIT_ACCOUNT, false)) {
            spf.edit().putString(KEY_USERNAME, "").commit();
            spf.edit().putString(KEY_PASSWORD, "").commit();
            spf.edit().putLong(KEY_USER_ID, 0).commit();

        }
        mUsername = spf.getString(KEY_USERNAME, "");
        mPassword = spf.getString(KEY_PASSWORD, "");
        mUserId = spf.getLong(KEY_USER_ID, 0);

        if (mUsername.isEmpty() || mPassword.isEmpty()) {
            //اولین فرگمنت
            splashImg.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new SendMobileFragment()).commit();
        } /*else if (mToken.getToken() != null && mToken.getToken().length() > 0) {
            logingOffLine();
        }*/ else {
            login(mUsername, mPassword);
        }


    }

    private void registerListenerForSms(final long userId, final String mobile) {

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsBroadcast, filter);
        // Permission has already been granted
        smsBroadcast.bindListener(messageText -> {
            Toast.makeText(mContext, "OTP: " + messageText, Toast.LENGTH_LONG).show();
            Log.d("registerListenerForSms", "messageReceived: " + messageText);
            //برو به ورود
            checkActivation(userId, mobile, messageText, new SendActivationCodeFragment.OnCheckActivation() {


                @Override
                public void activationFailed() {

                }
            });
            /*getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, EditProfileFragment.newInstance(1))
                    .addToBackStack("1")
                    .commit();*/
        });

    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(smsBroadcast);

        } catch (IllegalArgumentException e) {
            Log.d(TAG, "checkActivation: IllegalArgumentException:" + e.getMessage());
        }
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

            }
            break;
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  registerListenerForSms();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    public void login(final String userName, final String password) {
        Log.d(TAG, "login: درحال ورود\nلطفا چند لحظه منتظر بمانید..." + userName + " pass:" + password);
        AlertDialog mErrorUserInfoDialog = new AlertDialog.Builder(mContext).setMessage(getString(R.string.error_in_user_info)).create();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Token> call = apiService.getToken("password",
                password,
                userName);
        //  RequestBody.create(MediaType.parse("text/plain"), password)
        //  , RequestBody.create(MediaType.parse("text/plain"), userName));
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: isSuccessful");
                    if (response.body() != null) {
                        mToken = response.body();
                        Log.d("login.onResponse", "onResponse: " + mToken.getToken());
                        ((MyApplication) getApplication()).setCurrentToken(mToken);
                        spf.edit().putString(MyConst.KEY_TOKEN, mToken.getToken()).apply();

                        MyWebService.getProfileDetail(userName, response.body().getToken(), LoginActivity.this, new WebServiceResultImplementation() {
                            @Override
                            public void webServiceOnSuccess(final Bundle bundle) {

                                final User user = bundle.getParcelable(MyKeys.KEY_BUNDLE_OBJ);
                                Log.d(TAG, "webServiceOnSuccess: role:" + bundle.getString(MyKeys.KEY_BUNDLE_ROLE) + " userId:" + user.getId());

                                spf.edit().putLong(KEY_USER_ID, user.getId()).apply();
                                userDao.saveUser(user);

                                if (bundle.getString(MyKeys.KEY_BUNDLE_ROLE).equals(MyKeys.KEY_ROLE_NA)) { // پروفایل هنوز مشخص نشده
                                    splashImg.setVisibility(View.GONE);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content, SelectRoleFragment.newInstance(user)).commit();
                                } else if (user.getName() == null || user.getName().isEmpty()) { //مشخصات کاربر وارد نشده
                                    new AlertDialog.Builder(mContext)
                                            .setMessage(getString(R.string.complete_your_profile))
                                            .setPositiveButton(R.string.edit, (dialogInterface, i) -> {
                                                Intent intent = new Intent();
                                                intent.putExtra(EXTRA_OBJ_USER, user);
                                                intent.putExtra(EXTRA_ROLE_CHOICE, KEY_ROLE_ATHLETE);
                                                intent.setClass(mContext, EditProfileActivity.class);
                                                startActivityForResult(intent, REQ_EDIT_USER_PROFILE);
                                            })
                                            .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
                                                dialogInterface.cancel();
                                                Intent intent = new Intent();
                                                intent.setClass(mContext, MainActivity.class);
                                                intent.putExtra(EXTRA_ROLE_CHOICE, KEY_ROLE_ATHLETE);
                                                intent.putExtra(EXTRA_USER_NAME, user.getUserName());
                                                startActivity(intent);
                                            })
                                            .show();
                                } else if (bundle.getString(MyKeys.KEY_BUNDLE_ROLE).equals(MyKeys.KEY_ROLE_TRAINER)) { //پروفایل مربی می باشد
                                    MyWebService.getTrainerProfileDetail(mContext, user.getId(), new WebServiceResultImplementation() {
                                        @Override
                                        public void webServiceOnSuccess(Bundle bundle1) {

                                            Trainer trainer = bundle1.getParcelable(KEY_BUNDLE_OBJ);
                                            AsyncTask.execute(() -> {
                                                trainerDao.save(trainer); //ذخیره در دیتابیس
                                            });
                                            ((MyApplication) getApplication()).setCurrentTrainer(trainer);

                                            Intent i = new Intent();
                                            i.setClass(mContext, MainActivity.class);
                                            i.putExtra(EXTRA_USER_NAME, user.getUserName());
                                            i.putExtra(MyKeys.EXTRA_OBJ_TRAINER, trainer);
                                            i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, KEY_ROLE_TRAINER);
                                            startActivity(i);
                                            finish();
                                        }

                                        @Override
                                        public void webServiceOnFail() {
                                            Log.d("getTrainerProfileDetail", "webServiceOnFail: ");


                                        }
                                    });

                                } else if (bundle.getString(MyKeys.KEY_BUNDLE_ROLE).equals(MyKeys.KEY_ROLE_GYM)) { //پروفایل باشگاه می باشد
                                    Log.d(TAG, "webServiceOnSuccess: Role is GYM");
                                    MyWebService.getGymProfile(mContext
                                            , "Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken()
                                            , user.getId()
                                            , new WebServiceResultImplementation() {
                                                @Override
                                                public void webServiceOnSuccess(Bundle bundle1) {

                                                    Gym gym = bundle1.getParcelable(KEY_BUNDLE_OBJ);
                                                    AsyncTask.execute(() -> {
                                                        gymDao.save(gym); //ذخیره در دیتابیس
                                                    });
                                                    Intent i = new Intent();
                                                    i.setClass(mContext, MainActivity.class);
                                                    i.putExtra(EXTRA_USER_NAME, user.getUserName());
                                                    i.putExtra(MyKeys.EXTRA_OBJ_GYM, gym);
                                                    i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, KEY_ROLE_GYM);
                                                    startActivity(i);
                                                    finish();
                                                }

                                                @Override
                                                public void webServiceOnFail() {
                                                    Log.d("getTrainerProfileDetail", "webServiceOnFail: ");
                                                }
                                            });

                                } else if (bundle.getString(MyKeys.KEY_BUNDLE_ROLE).equals(MyKeys.KEY_ROLE_ATHLETE)) { //پروفایل ورزشکار
                                    Intent i = new Intent();
                                    i.setClass(mContext, MainActivity.class);
                                    i.putExtra(EXTRA_USER_NAME, user.getUserName());
                                    i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, KEY_ROLE_ATHLETE);
                                    startActivity(i);
                                    finish();
                                }
                            }


                            @Override
                            public void webServiceOnFail() {
                                LiveData<User> userLiveData = userDao.getUserById(mUserId);
                                if (userLiveData != null) {
                                    Intent i = new Intent();
                                    i.setClass(mContext, MainActivity.class);
                                    i.putExtra(EXTRA_USER_NAME, userLiveData.getValue().getUserName());
                                    switch (userLiveData.getValue().getRoleName()) {
                                        case MyKeys.KEY_ROLE_ATHLETE:
                                            i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, KEY_ROLE_ATHLETE);
                                            break;
                                        case MyKeys.KEY_ROLE_TRAINER:
                                            LiveData<Trainer> trainerLiveData = trainerDao.getTrainerById(userLiveData.getValue().getId());
                                            if (trainerLiveData != null) {
                                                i.putExtra(MyKeys.EXTRA_OBJ_TRAINER, trainerLiveData.getValue());
                                                i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, KEY_ROLE_TRAINER);
                                            }
                                            break;
                                        case MyKeys.KEY_ROLE_GYM:
                                            LiveData<Gym> gymLiveData = gymDao.getGymById(userLiveData.getValue().getId());
                                            if (gymLiveData != null) {
                                                i.putExtra(MyKeys.EXTRA_OBJ_GYM, gymLiveData.getValue());
                                                i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, KEY_ROLE_GYM);
                                            }
                                            break;
                                    }
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Log.d("login.onResponse", "onResponse: not Success! " +
                                jsonObject.getString("error"));
                        if (jsonObject.getString("error") != null && jsonObject.getString("error").contains("invalid_grant")) {
                            if (mErrorUserInfoDialog != null && !mErrorUserInfoDialog.isShowing()) {
                                mErrorUserInfoDialog.show();
                            }
                            spf.edit().putString(KEY_USERNAME, "").commit();
                            spf.edit().putString(KEY_PASSWORD, "").commit();
                            LoginActivity.this.finish();
                        }
                    } catch (Throwable t) {
                        Log.d(TAG, "onResponse: throws:" + t.getLocalizedMessage());
                        //LoginActivity.this.finish();
                        LiveData<User> userLiveData = userDao.getUserById(mUserId);
                        userLiveData.observe(LoginActivity.this, user -> {
                            if (user != null) {
                                Intent i = new Intent();
                                i.setClass(mContext, MainActivity.class);
                                i.putExtra(EXTRA_USER_NAME, user.getUserName());
                                switch (user.getRoleName()) {
                                    case MyKeys.KEY_ROLE_ATHLETE:
                                        i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, KEY_ROLE_ATHLETE);
                                        break;
                                    case MyKeys.KEY_ROLE_TRAINER:
                                        LiveData<Trainer> trainerLiveData = trainerDao.getTrainerById(user.getId());
                                        if (trainerLiveData != null) {
                                            i.putExtra(MyKeys.EXTRA_OBJ_TRAINER, trainerLiveData.getValue());
                                            i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, KEY_ROLE_TRAINER);
                                        }
                                        break;
                                    case MyKeys.KEY_ROLE_GYM:
                                        LiveData<Gym> gymLiveData = gymDao.getGymById(user.getId());
                                        if (gymLiveData != null) {
                                            i.putExtra(MyKeys.EXTRA_OBJ_GYM, gymLiveData.getValue());
                                            i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, KEY_ROLE_GYM);
                                        }
                                        break;
                                }
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, R.string.error_accord_try_again, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d("login.onFailure", "onFailure " + t.getLocalizedMessage());
                Toast.makeText(LoginActivity.this, getString(R.string.cant_access_server), Toast.LENGTH_LONG).show();
                logingOffLine();

            }
        });

    }

    private void logingOffLine() {
        LiveData<User> userLiveData = userDao.getUserById(mUserId);
        userLiveData.observe(LoginActivity.this, user -> {
            if (user != null) {
                Intent i = new Intent();
                i.setClass(mContext, MainActivity.class);
                i.putExtra(EXTRA_USER_NAME, user.getUserName());
                switch (user.getRoleName()) {
                    case MyKeys.KEY_ROLE_ATHLETE:
                        i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, KEY_ROLE_ATHLETE);
                        break;
                    case MyKeys.KEY_ROLE_TRAINER:
                        Trainer trainer = trainerDao.getTrainerByIdMain(user.getId());
                        if (trainer != null) {
                            i.putExtra(MyKeys.EXTRA_OBJ_TRAINER, trainer);
                            i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, KEY_ROLE_TRAINER);
                        }
                        break;
                    case MyKeys.KEY_ROLE_GYM:
                        Gym gym = gymDao.getGymByIdMain(user.getId());
                        if (gym != null) {
                            i.putExtra(MyKeys.EXTRA_OBJ_GYM, gym);
                            i.putExtra(MyKeys.EXTRA_ROLE_CHOICE, KEY_ROLE_GYM);
                        }
                        break;
                }
                startActivity(i);
                finish();
            } else {
                Log.d(TAG, "onFailure: users:" + userDao.loadAllList());
            }
        });
    }

    @Override
    public void checkActivation(final long userId, final String mobile, final String password, final SendActivationCodeFragment.OnCheckActivation onCheckActivation) {
        try {
            unregisterReceiver(smsBroadcast);

        } catch (IllegalArgumentException e) {
            Log.d(TAG, "checkActivation: IllegalArgumentException:" + e.getMessage());
        }
        Log.d(TAG, "checkActivation: منتظر تایید!\nلطفا چند لحظه منتظر بمانید...");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<RetroResult> call = apiService.checkActivationCode(userId, RequestBody.create(MediaType.parse("text/plain"), mobile),
                RequestBody.create(MediaType.parse("text/plain"), password));
        Log.d("LoginActivity", "checkActivation: userid:" + userId + " mobile:" + mobile + " mPassword:" + password);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResult().equalsIgnoreCase("OK")) {
                            ((MyApplication) getApplication()).setCurrentUser(userId, mobile, password);

                            spf.edit().putString(KEY_USERNAME, mobile).apply();
                            spf.edit().putString(KEY_PASSWORD, password).apply();
                            // prefs.edit().putString(KEY_USER_ID, password).apply();

                            mUsername = mobile;
                            mPassword = password;
                            mUserId = userId;
                            login(mobile, password);
                            Log.d("ckActivation.onResponse", "onResponse:" + response.message());

                        } else {
                            onCheckActivation.activationFailed();
                            Log.d("ckActivation.onResponse", "error: " + response.message() + "result:" + response.body().getResult());

                        }
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        onCheckActivation.activationFailed();
                        Log.d("cActivation.onResponse", "onResponse: not Success! " +
                                jsonObject.getString("error"));
                        if (jsonObject.getString("error") != null && jsonObject.getString("error").contains("invalid_grant")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage(getString(R.string.wrong_username_password)).create().show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                Log.d("login.onFailure", "onFailure " + t.getLocalizedMessage());
                Toast.makeText(LoginActivity.this, getString(R.string.error_accord_try_again), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void resendActCode() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new SendMobileFragment()).commit();

    }

    @Override
    public void onSuccessfulEdited(User user) {

        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        intent.putExtra(EXTRA_USER_NAME, user.getUserName());
        switch (user.getRoleName()) {
            case MyKeys.KEY_ROLE_ATHLETE:
                intent.putExtra(EXTRA_ROLE_CHOICE, KEY_ROLE_ATHLETE);
                break;
            case MyKeys.KEY_ROLE_TRAINER:
                intent.putExtra(EXTRA_ROLE_CHOICE, KEY_ROLE_TRAINER);
                break;
            case MyKeys.KEY_ROLE_GYM:
                intent.putExtra(EXTRA_ROLE_CHOICE, KEY_ROLE_GYM);
                break;
        }
        startActivity(intent);
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    public void sendMobileWeb(final String mobile) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Log.d(TAG, "sendMobileWeb: در حال ارسال به سرویس..." + mobile);

        Call<RetroResultActivation> call = apiService.getActivationCode(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mobile)));

        call.enqueue(new Callback<RetroResultActivation>() {
            @Override
            public void onResponse(Call<RetroResultActivation> call, Response<RetroResultActivation> response) {
                if (response.isSuccessful()
                        && response.body().getResult() != null
                ) {
                    if (response.body().getResult().equalsIgnoreCase("OK")) {
                        //منتظر اس ام اس
                        registerListenerForSms(response.body().getUserId(), mobile);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, SendActivationCodeFragment.newInstance(response.body().getUserId(), mobile))
                                .commit();
                        Log.d("Activation", "onResponse: Code sent!");
                    } else {
                        Toast.makeText(LoginActivity.this, "خطا در سرور...", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Result:" + response.body().getResult());
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new SendMobileFragment()).commit();

                    }
                } else {
                    Toast.makeText(LoginActivity.this, "خطایی پیش آمده..." + response.message(), Toast.LENGTH_SHORT).show();

                    Log.d("Activation", "onResponse: error:" + response.message() + " code:" + response.code());
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new SendMobileFragment()).commit();
                }
            }

            @Override
            public void onFailure(Call<RetroResultActivation> call, Throwable t) {
                Log.d("Activation", "onFailure: " + t.getMessage());
                if (t.getMessage().contains("Unable to resolve host")) {
                    Toast.makeText(mContext, getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void setRoleWeb(final String choice, final User user) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        final ProgressDialog waitingDialog = new ProgressDialog(mContext);
        waitingDialog.setMessage(getString(R.string.sending_data));
        waitingDialog.show();
        Log.d(TAG, "setRoleWeb: role:" + choice + " id:" + user.getId());
        Call<RetroResult> call = apiService.addUserToRole("Bearer " + ((MyApplication) getApplication()).getCurrentToken().getToken(), ((MyApplication) getApplication()).getCurrentUser().getId(),
                RequestBody.create(MediaType.parse("text/plain"), choice));

        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful()
                        && response.body().getResult() != null
                        && response.body().getResult().equalsIgnoreCase("OK")) {
                    Log.d("setRoleWeb", "onResponse: " + response.message());
                    String name = ((MyApplication) getApplication()).getCurrentUser().getName();
                    //if (name != null && name.isEmpty()) {
                    new AlertDialog.Builder(mContext)
                            .setMessage(R.string.complete_your_profile)
                            .setPositiveButton(R.string.edit, (dialogInterface, i) -> {
                                Intent intent = new Intent();
                                intent.putExtra(EXTRA_ROLE_CHOICE, KEY_ROLE_ATHLETE);
                                intent.putExtra(EXTRA_OBJ_USER, user);
                                intent.setClass(LoginActivity.this, EditProfileActivity.class);
                                startActivityForResult(intent, REQ_EDIT_USER_PROFILE);
                            })
                            .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
                                dialogInterface.cancel();
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                intent.putExtra(EXTRA_ROLE_CHOICE, KEY_ROLE_ATHLETE);
                                intent.putExtra(EXTRA_USER_NAME, user.getUserName());
                                startActivity(intent);
                            })
                            .show();
                } else {
                    Log.d("setRoleWeb", "onResponse: error:" + response.code() + " msg:" + response.message());
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                waitingDialog.dismiss();
                Log.d("setRoleWeb", "onFailure: " + t.getMessage());

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT_USER_PROFILE) {
            if (resultCode == RESULT_OK) {
                login(mUsername, mPassword);
            }
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private void setLocale(String localeName) {
        myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        if (!currentLanguage.equals(localeName)) {
            Intent refresh = new Intent(this, LoginActivity.class);
            refresh.putExtra(EXTRA_CURRENT_LANG, localeName);
            spf.edit().putString(KEY_CURRENT_LANG, localeName).commit();
            startActivity(refresh);
        }
    }
}

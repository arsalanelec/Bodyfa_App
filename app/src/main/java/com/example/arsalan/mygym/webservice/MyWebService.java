package com.example.arsalan.mygym.webservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.arsalan.interfaces.OnGetPlanFromWeb;
import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.models.GalleryItem;
import com.example.arsalan.mygym.models.MealPlanDay;
import com.example.arsalan.mygym.models.News;
import com.example.arsalan.mygym.models.RetGalleryList;
import com.example.arsalan.mygym.models.RetGym;
import com.example.arsalan.mygym.models.RetNewsList;
import com.example.arsalan.mygym.models.RetResponseStatus;
import com.example.arsalan.mygym.models.RetTrainer;
import com.example.arsalan.mygym.models.RetUserProfile;
import com.example.arsalan.mygym.models.RetWorkoutPlan;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.KEY_BUNDLE_OBJ;
import static com.example.arsalan.mygym.MyKeys.KEY_BUNDLE_ROLE;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_ATHLETE;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_GYM;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_NA;
import static com.example.arsalan.mygym.MyKeys.KEY_ROLE_TRAINER;

public class MyWebService {
    public static final int STATUS_WAITING = 0;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAIL = -1;
    public static final int STATUS_ERROR = -2;

    public static void getNewsWeb(long publisherId, int typeId, final List<News> newsList, final RecyclerView.Adapter adapter, Context context, final RecyclerView recyclerView, final WebServiceResultImplementation WebServiceResultImplementation) {
        ApiInterface apiService =
                ApiClient
                        .getClient().create(ApiInterface.class);

        Call<RetNewsList> call = apiService.getNewsList(0, 10, typeId, publisherId);
        call.enqueue(new Callback<RetNewsList>() {
            @Override
            public void onResponse(Call<RetNewsList> call, Response<RetNewsList> response) {

                if (response.isSuccessful()) {
                    WebServiceResultImplementation.webServiceOnSuccess(null);
                    Log.d("getNewsWeb", "onResponse: records:" + response.body().getRecordsCount());
                    newsList.removeAll(newsList);
                    newsList.addAll(response.body().getRecords());

                    adapter.notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();
                }
            }

            @Override
            public void onFailure(Call<RetNewsList> call, Throwable t) {

                WebServiceResultImplementation.webServiceOnFail();

            }
        });

    }

    public static void editProfile(final Activity context, Map<String, RequestBody> requestBodyMap, final WebServiceResultImplementation WebServiceResultImplementation, MultipartBody.Part image, MultipartBody.Part thumb) {
        final ProgressDialog waitingDialog = new ProgressDialog(context);
        waitingDialog.setMessage(context.getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<RetroResult> call = apiService.editProfile("Bearer " + ((MyApplication) context.getApplication()).getCurrentToken().getToken(), requestBodyMap, image, thumb);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful()) {
                    WebServiceResultImplementation.webServiceOnSuccess(null);
                    switch (response.body().getResult()) {
                        case "OK":
                            Toast.makeText(context, R.string.edited_successfully, Toast.LENGTH_LONG).show();
                            break;
                        case "ERROR":
                            Log.d("editProfile", "onResponse: Error");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                waitingDialog.dismiss();
                WebServiceResultImplementation.webServiceOnFail();
            }
        });
    }


    public static void editTrainerProfile(final Activity context, Map<String, RequestBody> requestBodyMap, final WebServiceResultImplementation WebServiceResultImplementation, MultipartBody.Part docImage, MultipartBody.Part docThumb, MultipartBody.Part natCardImage, MultipartBody.Part natCardThumb) {
        final ProgressDialog waitingDialog = new ProgressDialog(context);
        waitingDialog.setMessage(context.getString(R.string.please_wait_a_moment));
        waitingDialog.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<RetroResult> call = apiService.editTrainerDetail("Bearer " + ((MyApplication) context.getApplication()).getCurrentToken().getToken(), requestBodyMap, docImage, docThumb, natCardImage, natCardThumb);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful()) {
                    WebServiceResultImplementation.webServiceOnSuccess(null);
                    switch (response.body().getResult()) {
                        case "OK":
                            Toast.makeText(context, R.string.edited_successfully, Toast.LENGTH_LONG).show();
                            break;
                        case "ERROR":

                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                waitingDialog.dismiss();
                WebServiceResultImplementation.webServiceOnFail();
            }
        });
    }


    public static void getProfileDetail(final String userName, String token, final AppCompatActivity activity, final WebServiceResultImplementation WebServiceResultImplementation) {
        final String TAG = "getProfileDetail";
        Log.d(TAG, "getProfileDetail: username:" + userName);
        Log.d(TAG, "getProfileDetail: token:" + token);
        final ProgressDialog waitingDialog = new ProgressDialog(activity);
        waitingDialog.setMessage(activity.getString(R.string.getting_user_info));
        waitingDialog.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RetUserProfile> call = apiService.getProfile("Bearer " + token, RequestBody.create(MediaType.parse("text/plain"), userName));
        call.enqueue(new Callback<RetUserProfile>() {
            @Override
            public void onResponse(Call<RetUserProfile> call, Response<RetUserProfile> response) {
                waitingDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("getProfileD", "onResponse: name:" + response.body().getRecord().getName() + " phone:" + response.body().getRecord().getUserName() + " id:" + response.body().getRecord().getId());

                        ((MyApplication) activity.getApplication()).setCurrentUser(response.body().getRecord());

                        Bundle bundle = new Bundle();
                        bundle.putParcelable(KEY_BUNDLE_OBJ, response.body().getRecord());

                        if (response.body().getRecord().getRoleId() == 0) {
                            bundle.putString(KEY_BUNDLE_ROLE, KEY_ROLE_NA);
                        } else if (response.body().getRecord().getRoleId() == 2) { //مربی
                            bundle.putString(KEY_BUNDLE_ROLE, KEY_ROLE_TRAINER);


                        } else if (response.body().getRecord().getRoleId() == 1) { //باشگاه
                            bundle.putString(KEY_BUNDLE_ROLE, KEY_ROLE_GYM);

                        } else if (response.body().getRecord().getRoleId() == 3) { //ورزشگار
                            bundle.putString(KEY_BUNDLE_ROLE, KEY_ROLE_ATHLETE);
                        }
                        WebServiceResultImplementation.webServiceOnSuccess(bundle);

                    }
                } else {


                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Log.d("login.onResponse", "onResponse: not Success! " +
                                jsonObject.getString("error"));
                        if (jsonObject.getString("error") != null && jsonObject.getString("error").contains("invalid_grant")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setMessage(activity.getString(R.string.wrong_username_password)).create().show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RetUserProfile> call, Throwable t) {
                waitingDialog.dismiss();
                Log.d("login.onFailure", "onFailure " + t.getLocalizedMessage());
                if (t.getMessage().contains("No address associated with hostname"))
                    Toast.makeText(activity, activity.getString(R.string.error_connect_to_internet), Toast.LENGTH_LONG).show();

                Toast.makeText(activity, activity.getString(R.string.error_accord_try_again), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void getTrainerProfileDetail(final Context context, long userId,
                                               final WebServiceResultImplementation WebServiceResultImplementation) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RetTrainer> call = apiService.getTrainerDetail(userId);
        call.enqueue(new Callback<RetTrainer>() {
            @Override
            public void onResponse(Call<RetTrainer> call, Response<RetTrainer> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("getTrainerProfileDetail", "onResponse: name:" + response.body().getRecord().getName() + " id:" + response.body().getRecord().getId());
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(KEY_BUNDLE_OBJ, response.body().getRecord());
                        WebServiceResultImplementation.webServiceOnSuccess(bundle);
                    }

                } else {
                    WebServiceResultImplementation.webServiceOnFail();
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Log.d("getTrainerProfile.onRes", "onResponse: not Success! " +
                                jsonObject.getString("error"));
                        if (jsonObject.getString("error") != null && jsonObject.getString("error").contains("invalid_grant")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(context.getString(R.string.error_please_enter_again)).create().show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RetTrainer> call, Throwable t) {
                Log.d("login.onFailure", "onFailure " + t.getLocalizedMessage());
                if (t.getMessage().contains("No address associated with hostname"))
                    Toast.makeText(context, context.getText(R.string.error_connect_to_internet), Toast.LENGTH_LONG).show();

                Toast.makeText(context, context.getString(R.string.error_accord_try_again), Toast.LENGTH_LONG).show();
            }
        });
    }


    public static void getGymDetail(final Context context, long userId,
                                    final WebServiceResultImplementation WebServiceResultImplementation) {
        final String TAG = MyWebService.class.getSimpleName();

        Log.d(TAG, "getGymDetail: ");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RetGym> call = apiService.getGymDetail(userId);
        call.enqueue(new Callback<RetGym>() {
            @Override
            public void onResponse(Call<RetGym> call, Response<RetGym> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("GymProfileDet.Response", "onResponse: title:" + response.body().getRecord().getTitle() + " id:" + response.body().getRecord().getId());
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(KEY_BUNDLE_OBJ, response.body().getRecord());
                        WebServiceResultImplementation.webServiceOnSuccess(bundle);
                    }

                } else {
                    WebServiceResultImplementation.webServiceOnFail();
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Log.d("login.onResponse", "onResponse: not Success! " +
                                jsonObject.getString("error"));
                        if (jsonObject.getString("error") != null && jsonObject.getString("error").contains("invalid_grant")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(context.getString(R.string.wrong_username_password)).create().show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RetGym> call, Throwable t) {
                Log.d("login.onFailure", "onFailure " + t.getLocalizedMessage());
                if (t.getMessage().contains("No address associated with hostname"))
                    Toast.makeText(context, context.getString(R.string.error_connect_to_internet), Toast.LENGTH_LONG).show();

                Toast.makeText(context, context.getString(R.string.error_accord_try_again), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void getGymProfile(final Context context, String token, long userId,
                                     final WebServiceResultImplementation WebServiceResultImplementation) {
        final String TAG = MyWebService.class.getSimpleName();

        Log.d(TAG, "getGymDetail: ");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RetGym> call = apiService.getGymProfile(token, userId);
        call.enqueue(new Callback<RetGym>() {
            @Override
            public void onResponse(Call<RetGym> call, Response<RetGym> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("GymProfileDet.Response", "onResponse: title:" + response.body().getRecord().getTitle() + " id:" + response.body().getRecord().getId());
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(KEY_BUNDLE_OBJ, response.body().getRecord());
                        WebServiceResultImplementation.webServiceOnSuccess(bundle);
                    }

                } else {
                    WebServiceResultImplementation.webServiceOnFail();
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Log.d("login.onResponse", "onResponse: not Success! " +
                                jsonObject.getString("error"));
                        if (jsonObject.getString("error") != null && jsonObject.getString("error").contains("invalid_grant")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(context.getString(R.string.wrong_username_password)).create().show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RetGym> call, Throwable t) {
                Log.d("login.onFailure", "onFailure " + t.getLocalizedMessage());
                if (t.getMessage().contains("No address associated with hostname"))
                    Toast.makeText(context, context.getString(R.string.error_connect_to_internet), Toast.LENGTH_LONG).show();

                Toast.makeText(context, context.getString(R.string.error_accord_try_again), Toast.LENGTH_LONG).show();
            }
        });
    }

    static public void getGalleryWeb(String token, long userId, final OnGalleryLoadListener onGalleryLoadListener) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Log.d("getGalleryWeb", "onClick: userId:" + userId);


        Call<RetGalleryList> call = apiService.getMyGallery(token, userId);
        call.enqueue(new Callback<RetGalleryList>() {
            @Override
            public void onResponse(Call<RetGalleryList> call, Response<RetGalleryList> response) {
                if (response.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "onResponse: gallery cnt:" + response.body().getRecordsCount());
                    onGalleryLoadListener.onGalleryLoad(response.body().getRecords());
                } else {
                    Log.d(getClass().getSimpleName(), "onResponse: error:" + response.message());

                }

            }

            @Override
            public void onFailure(Call<RetGalleryList> call, Throwable t) {
                Log.d(getClass().getSimpleName(), "onFailure: ");
            }
        });
    }

    public static void addToGalleryWeb(final Activity context, long userId, MultipartBody.Part image, MultipartBody.Part thumb, final WebServiceResultImplementation WebServiceResultImplementation) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<RetroResult> call = apiService.addToMyGallery("Bearer " + ((MyApplication) context.getApplication()).getCurrentToken().getToken(), userId, image, thumb);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                if (response.isSuccessful()) {
                    WebServiceResultImplementation.webServiceOnSuccess(null);
                    switch (response.body().getResult()) {
                        case "OK": //NON-NLS
                            Log.d(getClass().getSimpleName(), "onResponse: +  عکس اضافه شد");
                            break;
                        case "ERROR":
                            Log.d(getClass().getSimpleName(), "onResponse: +  خطا در ارسال عکس");

                            break;
                    }
                } else {
                    WebServiceResultImplementation.webServiceOnFail();
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                WebServiceResultImplementation.webServiceOnFail();
            }
        });
    }

    public static LiveData<Integer> sendWokroutPlanToAthleteWeb(Activity activity, long athleteId, long planId, String title, String body) {
        return sendWokroutPlanToAthleteWeb(activity, athleteId, planId, title, body, 0);

    }

    /**
     * ارسال برنامه تمرینی به ورزشکار
     *
     * @param activity
     * @param athleteId
     * @param planId
     * @param title
     * @param body
     * @param requestId
     * @return
     */
    public static LiveData<Integer> sendWokroutPlanToAthleteWeb(Activity activity, long athleteId, long planId, String title, String body, long requestId) {
        final String TAG = "sendWokroutPlanToAthlet";
        MutableLiveData<Integer> status = new MutableLiveData<>();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        status.setValue(STATUS_WAITING);
        Call<RetroResult> call = apiService.sendTrainerWorkoutPlan("Bearer " + ((MyApplication) activity.getApplication()).getCurrentToken().getToken()
                , planId
                , athleteId
                , RequestBody.create(MediaType.parse("text/plain"), title)
                , RequestBody.create(MediaType.parse("text/plain"), body)
                , requestId);

        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                status.postValue(STATUS_SUCCESS);
                Log.d(TAG, "onResponse: sent OK! reqId:" + requestId + " athleteId:" + athleteId);
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                status.postValue(STATUS_FAIL);
                Log.d(TAG, "onResponse: send failed!");

            }
        });
        return status;
    }

    public static LiveData<Integer> getTrainerWorkoutPlanWeb(Activity activity, final long planId, final OnGetPlanFromWeb OnGetPlanFromWeb) {
        MutableLiveData<Integer> status = new MutableLiveData<>();
        status.setValue(STATUS_WAITING);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RetWorkoutPlan> call = apiService.getTrainerWorkoutPlan("Bearer " + ((MyApplication) activity.getApplication()).getCurrentToken().getToken(), planId);
        call.enqueue(new Callback<RetWorkoutPlan>() {
            @Override
            public void onResponse(Call<RetWorkoutPlan> call, Response<RetWorkoutPlan> response) {

                if (response.isSuccessful()) {
                    Log.d("getTrainerMealPlanWeb", "onResponse: desc:" + response.body().getRecord().getDescription());
                    status.postValue(STATUS_SUCCESS);
                    try {
                        Gson gson = new Gson();
                        List<MealPlanDay> mealPlanList = gson.fromJson(response.body().getRecord().getDescription(), new TypeToken<List<MealPlanDay>>() {
                        }.getType());
                        Log.d("getTrainerMealPlanWeb", "onResponse: " + mealPlanList);

                    } catch (Exception e) {
                    }
                    Log.d("getTrainerMealPlanWeb", "onResponse: planId:" + planId);
                    //ویرایش برنامه
                    OnGetPlanFromWeb.onGetPlan(planId, response.body().getRecord().getTitle(), response.body().getRecord().getDescription());
                } else {
                    status.postValue(STATUS_FAIL);
                }
            }

            @Override
            public void onFailure(Call<RetWorkoutPlan> call, Throwable t) {
                status.postValue(STATUS_FAIL);
            }
        });
        return status;
    }

    /**
     * send request from athlete to trainer
     *
     * @param activity
     * @param athleteId
     * @param trainerId
     * @param title
     * @param description
     * @return
     */
    public static LiveData<Integer> requestWorkoutPlanFromWeb(Activity activity, long athleteId, long trainerId, String title, String description) {
        MutableLiveData<Integer> status = new MutableLiveData<>();
        status.setValue(STATUS_WAITING);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("AthleteUserId", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(athleteId)));
        requestBodyMap.put("ParentUserId", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(trainerId)));
        requestBodyMap.put("Title", RequestBody.create(MediaType.parse("text/plain"), title));
        requestBodyMap.put("Descriptions", RequestBody.create(MediaType.parse("text/plain"), description));
        Call<RetroResult> call = apiService.requestWorkoutPlan("Bearer " + ((MyApplication) activity.getApplication()).getCurrentToken().getToken(), requestBodyMap);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {

                if (response.isSuccessful()) {
                    Log.d("requestWorkoutPlan", "onResponse: success!");
                    status.postValue(STATUS_SUCCESS);
                } else {
                    status.postValue(STATUS_FAIL);
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                status.postValue(STATUS_FAIL);
            }
        });
        return status;
    }


    public static LiveData<RetResponseStatus> athleteMembershipRequestFromWeb(Activity activity, long athleteId, long trainerGymId, String membershipType) {
        MutableLiveData<RetResponseStatus> status = new MutableLiveData<>();
        RetResponseStatus responseStatus = new RetResponseStatus();
        responseStatus.setStatus(STATUS_WAITING);
        status.setValue(responseStatus);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("AthleteId", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(athleteId)));
        requestBodyMap.put("ParentId", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(trainerGymId)));
        requestBodyMap.put("MembershipType", RequestBody.create(MediaType.parse("text/plain"), membershipType));
        Call<RetroResult> call = apiService.athleteMembershipRequest("Bearer " + ((MyApplication) activity.getApplication()).getCurrentToken().getToken(), requestBodyMap);
        call.enqueue(new Callback<RetroResult>() {
            @Override
            public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {

                if (response.isSuccessful()) {
                    if (response.body().getResult().equalsIgnoreCase("ok")) {
                        Log.d("requestWorkoutPlan", "onResponse: success!");
                        responseStatus.setStatus(STATUS_SUCCESS);

                    } else {
                        responseStatus.setStatus(STATUS_FAIL);
                        responseStatus.setMessage(response.body().getMessage());
                    }
                    status.postValue(responseStatus);
                } else {
                    responseStatus.setStatus(STATUS_FAIL);
                    status.postValue(responseStatus);
                }
            }

            @Override
            public void onFailure(Call<RetroResult> call, Throwable t) {
                responseStatus.setStatus(STATUS_FAIL);
                status.postValue(responseStatus);
            }
        });
        return status;
    }


    public interface OnGalleryLoadListener {
        void onGalleryLoad(ArrayList<GalleryItem> galleryItems);
    }
}

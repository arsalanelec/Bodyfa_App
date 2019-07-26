package com.example.arsalan.mygym.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.arsalan.mygym.models.ProgressRequestBody;
import com.example.arsalan.mygym.models.RetStatusProgress;
import com.example.arsalan.mygym.models.RetUserProfile;
import com.example.arsalan.mygym.models.RetroResult;
import com.example.arsalan.mygym.models.Token;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.webservice.MyWebService;
import com.example.arsalan.room.UserDao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class UserRepository {
    private static final String TAG = "UserRepository";
    private final UserDao mUserDao;
    private final Executor mExecutor;
    private final Token mToken;

    @Inject
    public UserRepository(UserDao userDao, Executor executor, Token token) {
        this.mUserDao = userDao;
        this.mExecutor = executor;
        mToken = token;
    }

    public LiveData<User> getUser(String userName) {
        refreshUser(userName);
        return mUserDao.getUserByUserName(userName);
    }

    public LiveData<RetStatusProgress> uploadUser(User user, Uri resultUri, String imagePath) {
        Log.d(TAG, "uploadUser: ");
        RetStatusProgress statusProgress = new RetStatusProgress();
        MutableLiveData<RetStatusProgress> statusReturn = new MutableLiveData<>();
        MultipartBody.Part thumbBody = null;
        MultipartBody.Part imageBody = null;

        RequestBody userIdReq = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user.getId()));
        RequestBody nameReq = RequestBody.create(MediaType.parse("text/plain"), user.getName());
        RequestBody genderReq = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user.isGender()));
        RequestBody weightReq = RequestBody.create(MediaType.parse("text/plain"), user.getWeight());
        RequestBody cityReq = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user.getCityId()));
        RequestBody birthDayReq = RequestBody.create(MediaType.parse("text/plain"), user.getBirthdayDateFa());

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("UserId", userIdReq);
        requestBodyMap.put("name", nameReq);
        requestBodyMap.put("gender", genderReq);
        requestBodyMap.put("weight", weightReq);
        requestBodyMap.put("BirthDateFa", birthDayReq);
        requestBodyMap.put("cityId", cityReq);
        try {
            if (resultUri != null) {
                File imageFile = new File(resultUri.getPath());
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(resultUri.getPath()), 128, 128);
                //create a file to write bitmap data
                File thumbFile = new File(imagePath, "thumb.jpg");
                thumbFile.createNewFile();
                //Convert bitmap to byte array
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 60 /*ignored for PNG*/, bos);
                byte[] bitmapData = bos.toByteArray();
                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(thumbFile);
                fos.write(bitmapData);
                fos.flush();
                fos.close();
                final RequestBody requestThumbFile =
                        RequestBody.create(
                                MediaType.parse("image/jpg"),
                                thumbFile);
                thumbBody =
                        MultipartBody.Part.createFormData("ThumbUrl", thumbFile.getName(), requestThumbFile);


                // create RequestBody instance from file
                //Log.d(TAG, "onClick: mediatype:" + MimeTypeMap.getFileExtensionFromUrl(resultUri.getPath()));

                final ProgressRequestBody requestFile = new ProgressRequestBody(MediaType.parse("image/jpg"), imageFile, new ProgressRequestBody.UploadCallbacks() {
                    @Override
                    public void onProgressUpdate(int percentage, String tag) {
                        Log.d("Send file", "onProgressUpdate: completed:" + percentage + "%");
                        statusProgress.setProgress(percentage);
                        statusReturn.setValue(statusProgress);
                    }

                    @Override
                    public void onError(String tag) {
                        statusProgress.setStatus(MyWebService.STATUS_ERROR);
                        statusReturn.setValue(statusProgress);
                    }

                    @Override
                    public void onFinish(String tag) {
                        statusProgress.setProgress(100);
                        statusReturn.setValue(statusProgress);
                    }
                }
                        , "Video Clip");
                imageBody =
                        MultipartBody.Part.createFormData("PictureUrl", imageFile.getName(), requestFile);
            }

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<RetroResult> call = apiService.editProfile(mToken.getTokenBearer(), requestBodyMap, resultUri != null ? imageBody : null, resultUri != null ? thumbBody : null);
            call.enqueue(new Callback<RetroResult>() {
                @Override
                public void onResponse(Call<RetroResult> call, Response<RetroResult> response) {
                    if (response.isSuccessful()) {
                        switch (response.body().getResult()) {
                            case "OK":
                                statusProgress.setStatus(MyWebService.STATUS_SUCCESS);
                                statusReturn.setValue(statusProgress);
                                getUser(user.getUserName());
                                break;
                            case "ERROR":
                                Log.d("editProfile", "onResponse: Error");
                                statusProgress.setStatus(MyWebService.STATUS_ERROR);
                                statusReturn.setValue(statusProgress);
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<RetroResult> call, Throwable t) {
                    Log.d(TAG, "onFailure:"+t.getCause());
                    statusProgress.setStatus(MyWebService.STATUS_ERROR);
                    statusReturn.setValue(statusProgress);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    return statusReturn;
    }



    private void refreshUser(String userName) {
        mExecutor.execute(() -> {
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), userName);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<RetUserProfile> call = apiService.getProfile(mToken.getTokenBearer(), requestBody);
            try {
                Response<RetUserProfile> response = call.execute();
                if (response.isSuccessful()) {
                    Log.d("refreshUserListCity", "run: newDao save:" + mUserDao.saveUser(response.body().getRecord()));
                } else {
                    Log.d("refreshUserListCity", "run: response.error");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


}

package com.example.arsalan.mygym.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.arsalan.mygym.models.RetUserProfile;
import com.example.arsalan.mygym.models.Token;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.room.UserDao;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


@Singleton  // informs Dagger that this class should be constructed once
public class UserRepository {
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

    private void refreshUser(String userName) {
        mExecutor.execute(new Runnable() {

            @Override
            public void run() {
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
            }

        });

    }


}

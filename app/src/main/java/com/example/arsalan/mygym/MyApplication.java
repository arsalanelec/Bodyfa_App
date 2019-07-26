package com.example.arsalan.mygym;

import android.app.Activity;
import android.app.Service;

import androidx.multidex.MultiDexApplication;

import com.example.arsalan.mygym.di.AppInjector;
import com.example.arsalan.mygym.models.Token;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.User;
import com.facebook.drawee.backends.pipeline.Fresco;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;

/**
 * Created by Arsalan on 10-02-2018.
 */

public class MyApplication extends MultiDexApplication implements HasActivityInjector, HasServiceInjector {
    private static final String DATABASE_NAME = "my_db";
    // private MyDatabase myDatabase;
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject
    DispatchingAndroidInjector<Service> dispatchingAndroidServiceInjector;
    private Token currentToken;
    private User currentUser;
    private Trainer currentTrainer;

    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.init(this);
        Fresco.initialize(this);

        currentToken = new Token();
        currentUser = new User();
        currentTrainer = new Trainer();
        // myDatabase= Room.databaseBuilder(getApplicationContext(),MyDatabase.class,DATABASE_NAME).fallbackToDestructiveMigration().build();

    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    public Trainer getCurrentTrainer() {
        return currentTrainer;
    }

    public void setCurrentTrainer(Trainer currentTrainer) {
        this.currentTrainer = currentTrainer;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setCurrentUser(long id, String mobile, String password) {
        User user = new User();
        user.setId(id);
        user.setUserName(mobile);
        user.setPassword(password);
        this.currentUser = user;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(Token currentToken) {
        this.currentToken = currentToken;
    }


    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingAndroidServiceInjector;
    }
}

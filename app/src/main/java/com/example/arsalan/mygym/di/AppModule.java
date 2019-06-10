/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.arsalan.mygym.di;

import android.app.Application;

import com.example.arsalan.room.CreditDao;
import com.example.arsalan.room.GalleryItemDao;
import com.example.arsalan.room.GymDao;
import com.example.arsalan.room.HonorDao;
import com.example.arsalan.room.InboxItemDao;
import com.example.arsalan.room.MealPlanDao;
import com.example.arsalan.room.MyDatabase;
import com.example.arsalan.room.NewsDetailDao;
import com.example.arsalan.room.NewsHeadDao;
import com.example.arsalan.room.TrainerAthleteDao;
import com.example.arsalan.room.TrainerDao;
import com.example.arsalan.room.TrainerWorkoutPlanRequestDao;
import com.example.arsalan.room.TransactionDao;
import com.example.arsalan.room.TutorialVideoDao;
import com.example.arsalan.room.UserDao;
import com.example.arsalan.room.WorkoutPlanDao;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import androidx.room.Room;

import dagger.Module;
import dagger.Provides;

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton
    @Provides
    MyDatabase provideDb(Application app) {
        return Room
                .databaseBuilder(app, MyDatabase.class, "mydatabase.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Singleton
    @Provides
    NewsHeadDao provideNewsDao(MyDatabase db) {
        return db.newsHeadDao();
    }

    @Singleton
    @Provides
    NewsDetailDao provideNewsDetailDao(MyDatabase db) {
        return db.newsDetailDao();
    }

    @Singleton
    @Provides
    TrainerDao provideTrainerDao(MyDatabase db) {
        return db.trainerDao();
    }

    @Singleton
    @Provides
    GymDao provideGymDao(MyDatabase db) {
        return db.gymDao();
    }

    @Singleton
    @Provides
    InboxItemDao provideInboxListDao(MyDatabase db) {
        return db.inboxItemDao();
    }

    @Singleton
    @Provides
    GalleryItemDao provideGalleryItemDao(MyDatabase db) {
        return db.galleryItemDao();
    }

    @Singleton
    @Provides
    MealPlanDao provideMealPlanDao(MyDatabase db) {
        return db.mealPlanDao();
    }

    @Singleton
    @Provides
    WorkoutPlanDao provideWorkoutPlanDao(MyDatabase db) {
        return db.workoutPlanDao();
    }

    @Singleton
    @Provides
    UserDao provideUserDao(MyDatabase db) {
        return db.userDao();
    }

    @Singleton
    @Provides
    HonorDao provideHonorDao(MyDatabase db) {
        return db.honorDao();
    }

    @Singleton
    @Provides
    TutorialVideoDao provideTutorialVideoDao(MyDatabase db) {
        return db.tutorialVideoDao();
    }

    @Singleton
    @Provides
    CreditDao provideCreditDao(MyDatabase db) {
        return db.creditDao();
    }

    @Singleton
    @Provides
    TransactionDao provideTransactionDao(MyDatabase db) {
        return db.transactionDao();
    }

    @Singleton
    @Provides
    TrainerWorkoutPlanRequestDao provideTrainerWorkoutPlanRequestDao(MyDatabase db) {
        return db.trainerWorkoutPlanRequestDao();
    }

    @Singleton
    @Provides
    TrainerAthleteDao provideTrainerAthleteDao(MyDatabase db) {
        return db.trainerAthleteDao();
    }


    @Provides
    Executor provideExecutor() {
        return Executors.newFixedThreadPool(3);
    }
}

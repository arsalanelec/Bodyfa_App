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

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.di.modules.NetModule;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class
        , AppModule.class
        , NetModule.class
        , MainActivityModule.class
        , PrivateMainActivityModule.class
        , LoginActivityModule.class
        , TutorialListActivityModule.class
        , WorkoutPlanActivityModule.class
        , EditProfileActivityModule.class
        , TrancastionListActivityModule.class})

public interface AppComponent {
    void inject(MyApplication myApplication);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder baseUrl(@Named("baseUrl") String baseUrl);

        AppComponent build();
    }
}

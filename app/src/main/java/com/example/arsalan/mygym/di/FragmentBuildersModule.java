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

import com.example.arsalan.mygym.dialog.MyAthleteListDialog;
import com.example.arsalan.mygym.dialog.RequestWorkoutPlanDialog;
import com.example.arsalan.mygym.dialog.TrainerWorkoutPlanListToSendDialog;
import com.example.arsalan.mygym.dialog.TutorialVideoListDialog;
import com.example.arsalan.mygym.fragments.AthleteMealPlanListFragment;
import com.example.arsalan.mygym.fragments.AthleteWorkoutPlanListFragment;
import com.example.arsalan.mygym.fragments.DashBoardAthleteFragment;
import com.example.arsalan.mygym.fragments.DashBoardTrainerFragment;
import com.example.arsalan.mygym.fragments.EditProfileFragment;
import com.example.arsalan.mygym.fragments.GymListFragment;
import com.example.arsalan.mygym.fragments.InboxFragment;
import com.example.arsalan.mygym.fragments.MyAthleteListFragment;
import com.example.arsalan.mygym.fragments.MyTrainerFragment;
import com.example.arsalan.mygym.fragments.MyTrainerMembershipFragment;
import com.example.arsalan.mygym.fragments.NewsDetailFragment;
import com.example.arsalan.mygym.fragments.NewsListFragment;
import com.example.arsalan.mygym.fragments.TrainerListFragment;
import com.example.arsalan.mygym.fragments.TrainerOrderListFragment;
import com.example.arsalan.mygym.fragments.TrainerPlansTabFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract NewsListFragment contributeNewsListFragment();

    @ContributesAndroidInjector
    abstract TrainerListFragment contributeTrainerListFragment();

    @ContributesAndroidInjector
    abstract GymListFragment contributeGymListFragment();

    @ContributesAndroidInjector
    abstract InboxFragment contributeInboxListFragment();

    @ContributesAndroidInjector
    abstract DashBoardTrainerFragment contributeDashBoardTrainerFragment();

    @ContributesAndroidInjector
    abstract DashBoardAthleteFragment contributeDashBoardAthleteFragment();

    @ContributesAndroidInjector
    abstract TrainerPlansTabFragment contributeTrainerPlansTabFragment();

    @ContributesAndroidInjector
    abstract MyTrainerFragment contributeMyTrainerFragment();

    @ContributesAndroidInjector
    abstract AthleteMealPlanListFragment contributeAthleteMealPlanListFragment();

    @ContributesAndroidInjector
    abstract AthleteWorkoutPlanListFragment contributeAthleteWorkoutPlanListFragment();


    @ContributesAndroidInjector
    abstract TutorialVideoListDialog contributeTutorialVideoListDialog();

    @ContributesAndroidInjector
    abstract MyAthleteListFragment contributeMyAthleteListFragment();

    @ContributesAndroidInjector
    abstract EditProfileFragment contributeEditProfileFragment();

    @ContributesAndroidInjector
    abstract TrainerOrderListFragment contributeTrainerOrderListFragment();

    @ContributesAndroidInjector
    abstract MyAthleteListDialog contributeMyAthleteListDialog();

    @ContributesAndroidInjector
    abstract TrainerWorkoutPlanListToSendDialog contributeTrainerWorkoutPlanListToSendDialog();

    @ContributesAndroidInjector
    abstract RequestWorkoutPlanDialog contributeRequestWorkoutPlanDialog();

    @ContributesAndroidInjector
    abstract MyTrainerMembershipFragment contributeMyTrainerMembershipFragment();

    @ContributesAndroidInjector
    abstract NewsDetailFragment contributeNewsDetailFragment();


}

package com.example.arsalan.mygym.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.arsalan.mygym.viewModels.AthleteListViewModel;
import com.example.arsalan.mygym.viewModels.AthleteMealPlanListViewModel;
import com.example.arsalan.mygym.viewModels.AthleteWorkoutPlanListViewModel;
import com.example.arsalan.mygym.viewModels.GalleryViewModel;
import com.example.arsalan.mygym.viewModels.GymListViewModel;
import com.example.arsalan.mygym.viewModels.HonorViewModel;
import com.example.arsalan.mygym.viewModels.InboxItemListViewModel;
import com.example.arsalan.mygym.viewModels.MyViewModelFactory;
import com.example.arsalan.mygym.viewModels.NewsDetailViewModel;
import com.example.arsalan.mygym.viewModels.NewsListViewModel;
import com.example.arsalan.mygym.viewModels.TrainerAthleteViewModel;
import com.example.arsalan.mygym.viewModels.TrainerListViewModel;
import com.example.arsalan.mygym.viewModels.TrainerMealPlanListViewModel;
import com.example.arsalan.mygym.viewModels.TrainerViewModel;
import com.example.arsalan.mygym.viewModels.TrainerWorkoutListViewModel;
import com.example.arsalan.mygym.viewModels.TrainerWorkoutPlanReqVm;
import com.example.arsalan.mygym.viewModels.TransactionsViewModel;
import com.example.arsalan.mygym.viewModels.TutorialListViewModel;
import com.example.arsalan.mygym.viewModels.UserCreditViewModel;
import com.example.arsalan.mygym.viewModels.UserViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(NewsListViewModel.class)
    abstract ViewModel bindNewsListViewModel(NewsListViewModel newsListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TrainerListViewModel.class)
    abstract ViewModel bindTrainerListViewModel(TrainerListViewModel trainerListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GymListViewModel.class)
    abstract ViewModel bindGymListViewModel(GymListViewModel gymListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(InboxItemListViewModel.class)
    abstract ViewModel bindInboxListViewModel(InboxItemListViewModel inboxItemListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GalleryViewModel.class)
    abstract ViewModel bindGalleryListViewModel(GalleryViewModel galleryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TrainerMealPlanListViewModel.class)
    abstract ViewModel bindTrainerMealPlanListViewModel(TrainerMealPlanListViewModel trainerMealPlanListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TrainerWorkoutListViewModel.class)
    abstract ViewModel bindTrainerWorkoutPlanListViewModel(TrainerWorkoutListViewModel trainerWorkoutListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TrainerViewModel.class)
    abstract ViewModel bindTrainerViewModel(TrainerViewModel trainerViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AthleteMealPlanListViewModel.class)
    abstract ViewModel bindAthleteMealPlanListViewModel(AthleteMealPlanListViewModel athleteMealPlanListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AthleteWorkoutPlanListViewModel.class)
    abstract ViewModel bindAthleteWorkoutPlanListViewModel(AthleteWorkoutPlanListViewModel athleteWorkoutPlanListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TutorialListViewModel.class)
    abstract ViewModel bindTutorialListViewModel(TutorialListViewModel tutorialListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AthleteListViewModel.class)
    abstract ViewModel bindAthleteListViewModel(AthleteListViewModel athleteListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HonorViewModel.class)
    abstract ViewModel bindHonorViewModel(HonorViewModel honorViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserCreditViewModel.class)
    abstract ViewModel bindUserCreditViewModel(UserCreditViewModel userCreditViewModel);

   @Binds
    @IntoMap
    @ViewModelKey(TransactionsViewModel.class)
    abstract ViewModel bindTransactionsViewModel(TransactionsViewModel transactionsViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(TrainerWorkoutPlanReqVm.class)
    abstract ViewModel bindTrainerWorkoutPlanReqVm(TrainerWorkoutPlanReqVm trainerWorkoutPlanReqVm);

    @Binds
    @IntoMap
    @ViewModelKey(TrainerAthleteViewModel.class)
    abstract ViewModel bindTrainerAthleteViewModel(TrainerAthleteViewModel trainerAthleteViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewsDetailViewModel.class)
    abstract ViewModel bindNewsDetailViewModel(NewsDetailViewModel newsDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(MyViewModelFactory factory);
}

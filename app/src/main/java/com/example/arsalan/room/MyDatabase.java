package com.example.arsalan.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.arsalan.mygym.models.GalleryItem;
import com.example.arsalan.mygym.models.Gym;
import com.example.arsalan.mygym.models.Honor;
import com.example.arsalan.mygym.models.InboxItem;
import com.example.arsalan.mygym.models.MealPlan;
import com.example.arsalan.mygym.models.News;
import com.example.arsalan.mygym.models.NewsHead;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.TrainerAthlete;
import com.example.arsalan.mygym.models.Transaction;
import com.example.arsalan.mygym.models.TutorialVideo;
import com.example.arsalan.mygym.models.User;
import com.example.arsalan.mygym.models.UserCredit;
import com.example.arsalan.mygym.models.WorkoutPlan;
import com.example.arsalan.mygym.models.WorkoutPlanReq;

@Database(entities = {
        NewsHead.class,
        News.class,
        Trainer.class,
        Gym.class,
        InboxItem.class,
        GalleryItem.class,
        MealPlan.class,
        WorkoutPlan.class,
        User.class,
        TutorialVideo.class,
        Honor.class,
        UserCredit.class,
        Transaction.class,
        WorkoutPlanReq.class,
        TrainerAthlete.class

}, exportSchema = false
        , version = 16)
public abstract class MyDatabase extends RoomDatabase {

    abstract public NewsHeadDao newsHeadDao();

    abstract public NewsDetailDao newsDetailDao();

    abstract public TrainerDao trainerDao();

    abstract public GymDao gymDao();

    abstract public InboxItemDao inboxItemDao();

    abstract public GalleryItemDao galleryItemDao();

    abstract public MealPlanDao mealPlanDao();

    abstract public WorkoutPlanDao workoutPlanDao();

    abstract public UserDao userDao();

    abstract public TutorialVideoDao tutorialVideoDao();

    abstract public HonorDao honorDao();

    abstract public CreditDao creditDao();

    abstract public TransactionDao transactionDao();

    abstract public TrainerWorkoutPlanRequestDao trainerWorkoutPlanRequestDao();

    abstract public TrainerAthleteDao trainerAthleteDao();

}


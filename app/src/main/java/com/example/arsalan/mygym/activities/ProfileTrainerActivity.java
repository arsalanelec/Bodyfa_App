package com.example.arsalan.mygym.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.fragments.MyTrainerFragment;
import com.example.arsalan.mygym.fragments.TrainerListFragment;
import com.example.arsalan.mygym.models.GalleryItem;
import com.example.arsalan.mygym.models.Honor;
import com.example.arsalan.mygym.models.MyConst;
import com.example.arsalan.mygym.models.RetHonorList;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.retrofit.ApiClient;
import com.example.arsalan.mygym.retrofit.ApiInterface;
import com.example.arsalan.mygym.webservice.MyWebService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.arsalan.mygym.MyKeys.EXTRA_GALLERY_ARRAY;
import static com.example.arsalan.mygym.MyKeys.EXTRA_IS_MY_TRAINER;
import static com.example.arsalan.mygym.MyKeys.EXTRA_PARCLABLE_OBJ;
import static com.example.arsalan.mygym.MyKeys.EXTRA_POSITION;
import static com.example.arsalan.mygym.MyKeys.EXTRA_TRAINER_ID;
import static com.example.arsalan.mygym.models.MyConst.EXTRA_USER_ID;

public class ProfileTrainerActivity extends AppCompatActivity implements Injectable  , HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_trainer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //  supportPostponeEnterTransition();

        //ImageView imgTrainer = findViewById(R.id.imgTrainer);

        Bundle extras = getIntent().getExtras();
        if(extras==null){
            
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //  String imageTransitionName = extras.getString(EXTRA_IMAGE_TRANSITION_NAME);
            //  imgTrainer.setTransitionName(imageTransitionName);
        }
        long trainerId = extras.getLong(EXTRA_TRAINER_ID);
        long userId = extras.getLong(EXTRA_USER_ID);
        boolean isMyCurrentTrainer = extras.getBoolean(EXTRA_IS_MY_TRAINER);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, MyTrainerFragment.newInstance(userId,trainerId,isMyCurrentTrainer))
                .commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}

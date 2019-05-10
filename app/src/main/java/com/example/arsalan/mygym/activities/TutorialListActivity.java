package com.example.arsalan.mygym.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.arsalan.mygym.MyApplication;
import com.example.arsalan.mygym.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.example.arsalan.mygym.fragments.TutorialListFragment;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class TutorialListActivity extends AppCompatActivity implements HasSupportFragmentInjector{

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    public static final String KEY_CAT_ID = "cat id key";
    public static final String KEY_TUTORIAL_ID = "tutorial id key";

    public static final String KEY_TITLE = "title key";
    private FloatingActionButton fab;
    private int catId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!getIntent().getExtras().isEmpty()) {
            setTitle(getString(R.string.workout_list)+" " + getIntent().getStringExtra(KEY_TITLE));
            catId = getIntent().getIntExtra(KEY_CAT_ID, 1);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content, TutorialListFragment.newInstance(catId)).commit();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility((((MyApplication) getApplication()).getCurrentUser().getRoleId() == 2) ? View.VISIBLE : View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(TutorialListActivity.this, PostTutorialActivity.class);
                i.putExtra(KEY_CAT_ID, catId);
                startActivity(i);
            }
        });
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}

package com.example.arsalan.mygym.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.arsalan.mygym.MyKeys;
import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.di.Injectable;
import com.example.arsalan.mygym.fragments.InboxFragment;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class ChatListActivity extends AppCompatActivity implements Injectable, HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        setTitle(R.string.messages);
        if(getIntent().getExtras()==null){
            throw new RuntimeException(this.toString()+" should have UserId in intent");
        }
        Long userId = getIntent().getExtras().getLong(MyKeys.EXTRA_USER_ID);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, InboxFragment.newInstance(userId))
                .commit();
    }
    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}

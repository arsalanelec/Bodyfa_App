package com.example.arsalan.mygym.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.fragments.DashBoardTrainerFragment;
import com.example.arsalan.mygym.fragments.HomeFragment;
import com.example.arsalan.mygym.fragments.InboxFragment;
import com.example.arsalan.mygym.fragments.MyAthleteListFragment;
import com.example.arsalan.mygym.fragments.TrainerListFragment;
import com.example.arsalan.mygym.fragments.TrainerOrderListFragment;
import com.example.arsalan.mygym.fragments.TrainerPlansTabFragment;
import com.example.arsalan.mygym.fragments.TutorialFragment;
import com.example.arsalan.mygym.models.Trainer;
import com.example.arsalan.mygym.models.User;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerTrainerAdapter extends FragmentStatePagerAdapter {
    String[] titles;
    Context mContext;
    private User mCurrentUser;
    private Trainer mCurrentTrainer;

    public ViewPagerTrainerAdapter(FragmentManager fm, Context context,User user,Trainer trainer) {
        super(fm);
        mContext = context;
        titles = new String[]{mContext.getString(R.string.dashboard), mContext.getString(R.string.my_athleths), mContext.getString(R.string.plans), mContext.getString(R.string.orders), mContext.getString(R.string.transactions)};//, mContext.getString(R.string.messages)};
    mCurrentUser=user;
    mCurrentTrainer=trainer;
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(titles[position]);
        Typeface typeface = ResourcesCompat.getFont(mContext, R.font.iran_sans_mobile);
        tv.setTypeface(typeface);
        return v;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DashBoardTrainerFragment.newInstance(mCurrentUser);
            case 1:
                return MyAthleteListFragment.newInstance(mCurrentUser, mCurrentTrainer);
            case 2:
                return TrainerPlansTabFragment.newInstance(mCurrentTrainer);
            case 3:
                return  TrainerOrderListFragment.newInstance(mCurrentTrainer.getId());
            case 4:
                return new TrainerListFragment();
            //case 5:
            //    return InboxFragment.newInstance(mCurrentUser);

        }
        return new HomeFragment();
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}

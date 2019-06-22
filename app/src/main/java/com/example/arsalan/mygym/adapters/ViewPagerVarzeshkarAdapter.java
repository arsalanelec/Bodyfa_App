package com.example.arsalan.mygym.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.arsalan.mygym.R;
import com.example.arsalan.mygym.fragments.AthleteMealPlanListFragment;
import com.example.arsalan.mygym.fragments.AthleteWorkoutPlanListFragment;
import com.example.arsalan.mygym.fragments.HomeFragment;
import com.example.arsalan.mygym.fragments.MyGymFragment;
import com.example.arsalan.mygym.fragments.MyTrainerMembershipFragment;
import com.example.arsalan.mygym.models.User;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerVarzeshkarAdapter extends FragmentStatePagerAdapter {
    private final Context mContext;
    private final String[] titles;
    private final User mCurrentUser;
    public ViewPagerVarzeshkarAdapter(FragmentManager fm, Context context, User user) {
        super(fm);
        mContext = context;
        titles = new String[]{ mContext.getString(R.string.my_gym), mContext.getString(R.string.my_trainers), mContext.getString(R.string.meal_plan), mContext.getString(R.string.workout_plan)};//, mContext.getString(R.string.messages)};
        mCurrentUser = user;
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
                return new MyGymFragment();
            case 1:
                return MyTrainerMembershipFragment.newInstance(mCurrentUser.getId());//MyTrainerFragment.newInstance(mCurrentUser.getId(),mCurrentUser.getTrainerId(),true);
            case 2:
                return AthleteMealPlanListFragment.newInstance(mCurrentUser);
            case 3:
                return AthleteWorkoutPlanListFragment.newInstance(mCurrentUser);
           // case 5:
           //     return InboxFragment.newInstance(mCurrentUser);
        }

        return new HomeFragment();
    }

    @Override
    public int getCount() {
        return titles.length;
    }

}

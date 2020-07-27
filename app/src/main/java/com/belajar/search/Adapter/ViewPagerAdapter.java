package com.belajar.search.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.belajar.search.R;
import com.belajar.search.Ui.Fragment.FollowerFragment;
import com.belajar.search.Ui.Fragment.FollowingFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mContext = context;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[] {
            R.string.followers,
            R.string.following
    };

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FollowerFragment();
                break;
            case 1:
                fragment = new FollowingFragment();
                break;
        }
        assert fragment != null;
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}

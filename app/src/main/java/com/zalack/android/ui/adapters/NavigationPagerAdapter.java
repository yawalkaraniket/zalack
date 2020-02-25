package com.zalack.android.ui.adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.zalack.android.ui.fragment.ProjectTabHostFragment;
import com.zalack.android.ui.fragment.ProfileFragment;
import com.zalack.android.ui.fragment.TasksFragment;

public class NavigationPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private FragmentManager fragmentManager;

    public NavigationPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.fragmentManager = fm;
        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Class fragmentClass = null;
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                fragmentClass  = ProjectTabHostFragment.class;
                break;
            case 1:
                fragmentClass = TasksFragment.class;
                break;
            case 2:
                fragmentClass = ProfileFragment.class;
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(bundle);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

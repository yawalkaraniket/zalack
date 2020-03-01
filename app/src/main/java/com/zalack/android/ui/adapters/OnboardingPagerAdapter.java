package com.zalack.android.ui.adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.zalack.android.data.room.table.User;
import com.zalack.android.ui.fragment.UserOnboardingFragment;

public class OnboardingPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private FragmentManager fragmentManager;

    public OnboardingPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.fragmentManager = fm;
        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        Class fragmentClass = null;

        switch (position) {
            case 0:
                fragmentClass = UserOnboardingFragment.class;
                bundle.putCharSequence(UserOnboardingFragment.SCREEN, UserOnboardingFragment.TYPE_ADD_NAME);
                break;
            case 1:
                fragmentClass = UserOnboardingFragment.class;
                bundle.putCharSequence(UserOnboardingFragment.SCREEN, UserOnboardingFragment.TYPE_ADD_EMAIL);
                break;
            case 2:
                fragmentClass = UserOnboardingFragment.class;
                bundle.putCharSequence(UserOnboardingFragment.SCREEN, UserOnboardingFragment.TYPE_MOBILE_NUMBER);
                break;
            case 3:
                fragmentClass = UserOnboardingFragment.class;
                bundle.putCharSequence(UserOnboardingFragment.SCREEN, UserOnboardingFragment.TYPE_ADD_PASSWORD);

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
        return 4;
    }
}

package com.zalack.android.ui.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.zalack.android.R;

public class FragmentUtils {

    public static void replaceFragment(FragmentManager fragmentManager, Class fragmentClass, String tag) {
        replaceFragment(fragmentManager, fragmentClass,tag,null);
    }

    public static void replaceFragment(FragmentManager fragmentManager, Class fragmentClass, String tag, Bundle extras) {
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            if (extras != null) {
                fragment.setArguments(extras);
            }
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            fragmentManager.beginTransaction().replace(R.id.hosted_fragment, fragment, tag).addToBackStack(tag).commit();
        } catch (IllegalStateException i) {
            Log.e("error", i.getLocalizedMessage());
        }
    }
}

package com.zalack.android.ui.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zalack.android.R;
import com.zalack.android.ui.activity.NavigationActivity;

public class ProjectTabHostFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fragment_blank, container, false);

        FragmentUtils.replaceFragment(getFragmentManager(), ProjectsFragment.class, NavigationActivity.ADD_NEW_PROJECT);

        return view;
    }

}

package com.zalack.android.ui.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zalack.android.R;
import com.zalack.android.ui.activity.NavigationActivity;

public class TaskTabHostFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_task_tab_host, container, false);

        FragmentUtils.replaceFragment(getFragmentManager(), TasksFragment.class, NavigationActivity.ADD_NEW_TASK);

        return view;
    }

}

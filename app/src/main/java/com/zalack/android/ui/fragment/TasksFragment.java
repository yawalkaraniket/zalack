package com.zalack.android.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.models.project_tickets.Ticket;
import com.zalack.android.data.webservice.viewmodel.ProjectTicketsViewModel;
import com.zalack.android.ui.activity.NavigationActivity;
import com.zalack.android.ui.activity.UpdateProfileActivity;
import com.zalack.android.ui.common.FontTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.tabs_pager)
    ViewPager tabsPager;

    @BindView(R.id.create_new_ticket_circle_button)
    ImageView addTaskButton;

    @BindView(R.id.header_title)
    FontTextView title;

    @BindView(R.id.pager_layout)
    RelativeLayout pagerLayout;

    @BindView(R.id.not_tickets_layout)
    RelativeLayout noTicketsLayout;

    @BindView(R.id.title)
    FontTextView noTaskTitle;

    @BindView(R.id.create_new_ticket)
    Button createNewTicket;

    @Inject
    ZalckPreferences prefs;

    private FragmentActivity activity;
    private List<Ticket> tickets = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fragment_tasks, container, false);
        ButterKnife.bind(this, view);

        ((ZalckApp) this.getContext().getApplicationContext()).getMyComponent().inject(this);

        setupViewPager(tabsPager);
        tabLayout.setupWithViewPager(tabsPager);

        setLayoutAccordingToTickets();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.activity = (FragmentActivity) context;
        super.onAttach(context);

        IntentFilter filter = new IntentFilter();
        filter.addAction("task_status_changed");
        filter.addAction("project_status_changed");
        getActivity().registerReceiver(broadcastReceiver, filter);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null && broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.create_new_ticket_circle_button)
    void addTask() {
        if (prefs.getAllProjects() != null) {
            Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
            intent.putExtra(UpdateProfileActivity.SCREEN, UpdateProfileActivity.TYPE_CREATE_NEW_TICKET);
            getActivity().startActivity(intent);
        } else {
            ((NavigationActivity) getActivity()).navigateToProfileFragment();
        }
    }

    @OnClick(R.id.create_new_ticket)
    void createNewTicket() {
        addTask();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager());
        adapter.addFragment(new TodoListFragment(), "Todo");
        adapter.addFragment(new InProgressListFragment(), "In Progress");
        adapter.addFragment(new DoneListFragment(), "Done");
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    private void setLayoutAccordingToTickets() {
        ProjectTicketsViewModel projectTicketsViewModel = ViewModelProviders.of(this).get(ProjectTicketsViewModel.class);
        showProgress();
        projectTicketsViewModel.getProjectTickets(prefs.getToken(), prefs.getCurrentProjectId()).observe(getViewLifecycleOwner(), tickets -> {
            hideProgress();
            switch (tickets.getStatus()) {
                case SUCCESS:
                    this.tickets = tickets.getData().getData();
                    if (prefs.getAllProjects() == null) {
                        createNewTicket.setText(R.string.add_project);
                        noTaskTitle.setText(getResources().getString(R.string.add_first_project_title));
                    } else if (this.tickets.isEmpty()) {
                        noTicketsLayout.setVisibility(View.VISIBLE);
                        pagerLayout.setVisibility(View.GONE);
                    } else {
                        noTicketsLayout.setVisibility(View.GONE);
                        pagerLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                case ERROR:
                    noTicketsLayout.setVisibility(View.VISIBLE);
                    pagerLayout.setVisibility(View.GONE);
                    if (prefs.getAllProjects() == null) {
                        createNewTicket.setText(R.string.add_project);
                        noTaskTitle.setText(getResources().getString(R.string.add_first_project_title));
                    }
                    break;
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                switch (intent.getAction()) {
                    case "task_status_changed":
                    case "project_status_changed":
                        title.setText(prefs.getCurrentProjectName() + " " + "Tasks");
                        setLayoutAccordingToTickets();
                        createNewTicket.setText(getResources().getString(R.string.add_task));
                        noTaskTitle.setText(getResources().getString(R.string.create_a_task_n_succeed_a_project));
                        break;
                }
            }
        }
    };

    private void resetUI() {
        noTicketsLayout.setVisibility(View.GONE);
        pagerLayout.setVisibility(View.GONE);
    }
}

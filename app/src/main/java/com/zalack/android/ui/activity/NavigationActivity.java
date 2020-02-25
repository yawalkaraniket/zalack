package com.zalack.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.zalack.android.R;
import com.zalack.android.ui.adapters.NavigationPagerAdapter;
import com.zalack.android.ui.common.NavigationPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigationActivity extends AppCompatActivity {

    @BindView(R.id.projects_selection)
    LinearLayout navProjectSelection;

    @BindView(R.id.projects_selection_image)
    ImageView projectSelectionImage;

    @BindView(R.id.projects_selection_text)
    TextView projectSelectionText;

    @BindView(R.id.tasks_selection)
    LinearLayout navTasksSelection;

    @BindView(R.id.tasks_selection_image)
    ImageView taskSelectionImage;

    @BindView(R.id.tasks_selection_text)
    TextView tasksSelectionText;

    @BindView(R.id.profile_selection)
    LinearLayout navProfileSelection;

    @BindView(R.id.profile_selection_image)
    ImageView profileSelectionImage;

    @BindView(R.id.profile_selection_text)
    TextView profileSelectionText;

    @BindView(R.id.navigation_pager)
    NavigationPager navigationPager;

    @BindView(R.id.navigation_layout_container)
    LinearLayout navigationContainer;

    AlertDialog.Builder builder;
    AlertDialog alert;

    public static String ADD_NEW_PROJECT = "Add New Project";
    public static String ADD_NEW_TASK = "Add New Task";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);

        builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    this.finish();
                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                });
        // Creating dialog box
        alert = builder.create();

        initializePager();
    }

    @OnClick(R.id.projects_selection)
    public void navProjectSelection() {
        resetAllNavigation();
        setProjectSelection();
        navigationPager.setCurrentItem(0, true);
    }

    @OnClick(R.id.tasks_selection)
    public void navTasksSelection() {
        resetAllNavigation();
        setTasksSelection();
        navigationPager.setCurrentItem(1, true);
    }

    @OnClick(R.id.profile_selection)
    public void navProfileSelection() {
        resetAllNavigation();
        setProfileSelection();
        navigationPager.setCurrentItem(2, true);

    }

    private void initializePager() {
        navigationPager.setOffscreenPageLimit(3);
        navigationPager.setAdapter(new NavigationPagerAdapter(getSupportFragmentManager(), this));
        navigationPager.setCurrentItem(0, true);
        setProjectSelection();
    }

    private void resetAllNavigation() {
//        projectSelectionImage.setImageResource(R.color.black);
        projectSelectionText.setTextColor(getResources().getColor(R.color.black));

//        profileSelectionImage.setImageResource(R.color.black);
        profileSelectionText.setTextColor(getResources().getColor(R.color.black));

//        taskSelectionImage.setImageResource(R.color.black);
        tasksSelectionText.setTextColor(getResources().getColor(R.color.black));
    }

    private void setProjectSelection() {
//        projectSelectionImage.setImageResource(R.color.blue);
        projectSelectionText.setTextColor(getResources().getColor(R.color.blue));
    }

    private void setTasksSelection() {
//        taskSelectionImage.setImageResource(R.color.blue);
        tasksSelectionText.setTextColor(getResources().getColor(R.color.blue));
    }

    private void setProfileSelection() {
//        profileSelectionImage.setImageResource(R.color.blue);
        profileSelectionText.setTextColor(getResources().getColor(R.color.blue));
    }

    public void hideNavigation() {
        navigationContainer.setVisibility(View.GONE);
    }

    public void openTaskForProjectId() {
        navigationPager.setCurrentItem(1);
        resetAllNavigation();
        setTasksSelection();
        sendBroadcast(new Intent("task_status_changed"));
    }

    public void navigateToProfileFragment() {
    navigationPager.setCurrentItem(0);
    }

    public void navigateToTaskFragment() {
        navigationPager.setCurrentItem(1);
    }

    public void showNavigation() {
        navigationContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (navigationPager.getCurrentItem() != 0) {
            navigationPager.setCurrentItem(0, true);
            resetAllNavigation();
            setProjectSelection();
        } else {
            alert.show();
        }

        showNavigation();
    }
}

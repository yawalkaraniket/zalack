package com.zalack.android.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);

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
    }

    private void resetAllNavigation() {
        projectSelectionImage.setImageResource(R.color.black);
        projectSelectionText.setTextColor(getResources().getColor(R.color.black));

        profileSelectionImage.setImageResource(R.color.black);
        profileSelectionText.setTextColor(getResources().getColor(R.color.black));

        taskSelectionImage.setImageResource(R.color.black);
        tasksSelectionText.setTextColor(getResources().getColor(R.color.black));
    }

    private void setProjectSelection() {
        projectSelectionImage.setImageResource(R.color.blue);
        projectSelectionText.setTextColor(getResources().getColor(R.color.blue));
    }

    private void setTasksSelection() {
        taskSelectionImage.setImageResource(R.color.blue);
        tasksSelectionText.setTextColor(getResources().getColor(R.color.blue));
    }

    private void setProfileSelection() {
        profileSelectionImage.setImageResource(R.color.blue);
        profileSelectionText.setTextColor(getResources().getColor(R.color.blue));
    }

}

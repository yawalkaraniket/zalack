package com.zalack.android.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.models.all_projects.ProjectData;
import com.zalack.android.data.models.update_profile.UpdateProfile;
import com.zalack.android.data.webservice.viewmodel.AllProjectsViewModel;
import com.zalack.android.data.webservice.viewmodel.DeleteProjectViewModel;
import com.zalack.android.ui.activity.NavigationActivity;
import com.zalack.android.ui.activity.UpdateProfileActivity;
import com.zalack.android.ui.adapters.ProjectDetailsAdapter;
import com.zalack.android.ui.common.FontTextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zalack.android.ui.adapters.ProjectDetailsAdapter.OnProjectClickListener;

public class ProjectsFragment extends BaseFragment implements OnProjectClickListener {

    @BindView(R.id.header_title)
    FontTextView title;

    @BindView(R.id.not_projects)
    TextView noProjectsView;

    @BindView(R.id.projects_recycler_view)
    RecyclerView projectsRecyclerView;

    @BindView(R.id.add_project_button)
    ImageView addProjectButton;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layout_when_project_available)
    RelativeLayout layoutWhenProjectAvailable;

    @BindView(R.id.layout_when_project_unavailable)
    RelativeLayout getLayoutWhenProjectNotAvailable;

    @Inject
    ZalckPreferences prefs;

    AlertDialog.Builder builder;
    AlertDialog alert;

    private AllProjectsViewModel allProjectsViewModel;
    private DeleteProjectViewModel deleteProjectViewModel;
    private List<ProjectData> projectDataList;
    ProjectDetailsAdapter projectDetailsAdapter;
    private int deleteProjectId;
    private boolean isVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_projects, container, false);
        ButterKnife.bind(this, layout);
        setUpProgress(layout, this.getActivity());
        ((ZalckApp) this.getContext().getApplicationContext()).getMyComponent().inject(this);
        projectsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        projectDetailsAdapter = new ProjectDetailsAdapter(this.getContext(), this);
        allProjectsViewModel = ViewModelProviders.of(this).get(AllProjectsViewModel.class);
        deleteProjectViewModel = ViewModelProviders.of(this).get(DeleteProjectViewModel.class);

        builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Do you want to delete this project ?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    showProgress();
                    deleteProjectViewModel.deleteProject(prefs.getToken(), deleteProjectId).observe(getViewLifecycleOwner(), response -> {
                        switch (response.getStatus()) {
                            case SUCCESS:
                                Toast.makeText(this.getContext(), response.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                                showProgress();
                                getAllProjects();
                                getActivity().sendBroadcast(new Intent("project_status_changed"));
                                break;
                            case ERROR:
                                Toast.makeText(this.getContext(), "Unable to load data.", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    });
                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                });
        //Creating dialog box
        alert = builder.create();

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.black));
        swipeRefreshLayout.setOnRefreshListener(() -> getAllProjects());

        showProgress();
        getAllProjects();

        return layout;
    }

    private void setTaskDataAfterAppInstall() {
        if (!projectDataList.isEmpty() && prefs.getToken().isEmpty()) {
            prefs.setCurrentProjectId(projectDataList.get(0).getId());
            prefs.setProjectName(projectDataList.get(0).getName());
            this.getActivity().sendBroadcast(new Intent("task_status_changed"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.add_project_button)
    void addProjects() {

        Intent intent = new Intent(this.getActivity(), UpdateProfileActivity.class);
        intent.putExtra(UpdateProfileActivity.SCREEN, UpdateProfileActivity.TYPE_ADD_PROJECT);
        this.getActivity().startActivity(intent);
    }

    @OnClick(R.id.add_project_text_button)
    public void addProjectTextButton() {
        addProjects();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        IntentFilter filter = new IntentFilter();
        filter.addAction("task_status_changed");
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (getActivity() != null && broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    private void editProject(ProjectData projectData) {

        Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
        intent.putExtra(UpdateProfileActivity.SCREEN, UpdateProfileActivity.TYPE_UPDATE_PROJECT);
        intent.putExtra("project_data", projectData);
        getActivity().startActivity(intent);
    }

    private void getAllProjects() {
        resetUI();
        allProjectsViewModel.getProjects(prefs.getToken()).observe(getViewLifecycleOwner(), projects -> {
            hideProgress();
            swipeRefreshLayout.setRefreshing(false);
            switch (projects.getStatus()) {
                case SUCCESS:
                    projectDataList = projects.getData().getData();
                    if (projectDataList.isEmpty()) {
                        layoutWhenProjectAvailable.setVisibility(View.GONE);
                        getLayoutWhenProjectNotAvailable.setVisibility(View.VISIBLE);
                    } else {
                        layoutWhenProjectAvailable.setVisibility(View.VISIBLE);
                        getLayoutWhenProjectNotAvailable.setVisibility(View.GONE);
                    }
                    projectDetailsAdapter.setList(projectDataList);
                    projectsRecyclerView.setAdapter(projectDetailsAdapter);
                    setTaskDataAfterAppInstall();
                    break;
                case ERROR:
                    Toast.makeText(this.getContext(), "Unable to load data", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.delete_project:
                deleteProjectId = projectDataList.get(position).getId();
                alert.show();
                break;
            case R.id.edit_project:
                editProject(projectDataList.get(position));
                break;
            case R.id.project_parent_layout:
                prefs.setCurrentProjectId(projectDataList.get(position).getId());
                prefs.setProjectName(projectDataList.get(position).getName());
                ((NavigationActivity)getActivity()).openTaskForProjectId();
                break;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals("task_status_changed")) {
                    showProgress();
                    getAllProjects();
                }
            }
        }
    };

    private void resetUI() {
        layoutWhenProjectAvailable.setVisibility(View.GONE);
        getLayoutWhenProjectNotAvailable.setVisibility(View.GONE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
    }
}

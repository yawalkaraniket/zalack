package com.zalack.android.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;
import com.zalack.android.data.webservice.DataState;
import com.zalack.android.data.webservice.viewmodel.AllProjectsViewModel;
import com.zalack.android.ui.adapters.ProjectDetailsAdapter;
import com.zalack.android.ui.common.FontTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectsFragment extends BaseFragment {

    @BindView(R.id.header_title)
    FontTextView title;

    @BindView(R.id.projects_recycler_view)
    RecyclerView projectsRecyclerView;

    @Inject
    ZalckPreferences prefs;

    private AllProjectsViewModel allProjectsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_projects, container, false);
        ButterKnife.bind(this, layout);
        setUpProgress(layout, this.getActivity());
        ((ZalckApp) this.getContext().getApplicationContext()).getMyComponent().inject(this);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        getAllProjects();

    }

    private void getAllProjects() {
        projectsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        ProjectDetailsAdapter projectDetailsAdapter = new ProjectDetailsAdapter(this.getContext());
        allProjectsViewModel = ViewModelProviders.of(this).get(AllProjectsViewModel.class);
        showProgress();
        allProjectsViewModel.getProjects(prefs.getToken()).observe(getViewLifecycleOwner(), projects -> {
            hideProgress();
            switch (projects.getStatus()) {
                case SUCCESS:
                    projectDetailsAdapter.setList(projects.getData().getData());
                    projectsRecyclerView.setAdapter(projectDetailsAdapter);
                    break;
                case ERROR:
                    Toast.makeText(this.getContext(), "Unable to load data", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    public ProjectsFragment() {
        // Required empty public constructor
    }

}

package com.zalack.android.data.webservice.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.zalack.android.data.models.project_details.ProjectDetails;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.repository.ProjectDetailsRepository;

public class ProjectDetailsViewModel extends AndroidViewModel {

    private ProjectDetailsRepository projectDetailsRepository;
    Application application;

    public ProjectDetailsViewModel(Application application) {
        super(application);
        this.application = application;
    }

    public LiveDataState<ProjectDetails> getProjectTickets(String token, int id) {
        projectDetailsRepository = new ProjectDetailsRepository(application, id, token);
        return projectDetailsRepository.getMutableLiveData();
    }

    public void clear() {
        projectDetailsRepository.clear();
    }

}

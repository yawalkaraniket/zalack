package com.zalack.android.data.webservice.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.zalack.android.data.models.all_projects.AllProjects;
import com.zalack.android.data.models.login.Login;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.repository.AllProjectRepository;
import com.zalack.android.data.webservice.repository.LoginRepository;

import java.util.HashMap;

public class AllProjectsViewModel extends AndroidViewModel {

    private AllProjectRepository allProjectRepository;
    Application application;

    public AllProjectsViewModel(Application application) {
        super(application);
        this.application = application;
    }

    public LiveDataState<AllProjects> getProjects(String token) {
        allProjectRepository = new AllProjectRepository(application, token);
        return allProjectRepository.getMutableLiveData();
    }

    public void clear() {
        allProjectRepository.clear();
    }

}

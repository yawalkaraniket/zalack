package com.zalack.android.data.webservice.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.zalack.android.data.models.add_project.AddedProject;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.repository.AddProjectRepository;

import java.util.HashMap;

public class AddProjectViewModel extends AndroidViewModel {

    private AddProjectRepository signUpRepository;
    Application application;

    public AddProjectViewModel(Application application) {
        super(application);
        this.application = application;
    }

    public LiveDataState<AddedProject> addProject(String token, HashMap<String, String> user) {
        signUpRepository = new AddProjectRepository(application, token, user);
        return signUpRepository.getAddedProject();
    }

    public void clear() {
        signUpRepository.clear();
    }

}

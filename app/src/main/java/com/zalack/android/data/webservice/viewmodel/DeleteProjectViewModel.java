package com.zalack.android.data.webservice.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.zalack.android.data.models.delete_project.DeleteProject;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.repository.DeleteProjectRepository;

public class DeleteProjectViewModel extends AndroidViewModel {

    private DeleteProjectRepository deleteProjectRepository;
    Application application;

    public DeleteProjectViewModel(Application application) {
        super(application);
        this.application = application;
    }

    public LiveDataState<DeleteProject> deleteProject(String token, int projectId) {
        deleteProjectRepository = new DeleteProjectRepository(application, token, projectId);
        return deleteProjectRepository.getAddedProject();
    }

    public void clear() {
        deleteProjectRepository.clear();
    }

}


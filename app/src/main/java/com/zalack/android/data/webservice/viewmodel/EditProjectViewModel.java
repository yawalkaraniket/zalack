package com.zalack.android.data.webservice.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.zalack.android.data.models.edit_project.EditProjectDetails;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.repository.EditProjectRepository;

import java.util.HashMap;

public class EditProjectViewModel extends AndroidViewModel {

    private EditProjectRepository editProjectRepository;
    Application application;

    public EditProjectViewModel(Application application) {
        super(application);
        this.application = application;
    }

    public LiveDataState<EditProjectDetails> editProject(String token, int projectId, HashMap<String, String> user) {
        editProjectRepository = new EditProjectRepository(application, projectId,token, user);
        return editProjectRepository.getEditedProject();
    }

    public void clear() {
        editProjectRepository.clear();
    }

}


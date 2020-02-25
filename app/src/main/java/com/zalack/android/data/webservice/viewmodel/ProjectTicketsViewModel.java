package com.zalack.android.data.webservice.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.zalack.android.data.models.delet_ticket.DeleteTicket;
import com.zalack.android.data.models.edit_task.EditTask;
import com.zalack.android.data.models.project_tickets.ProjectTickets;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.repository.ProjectTicketsRepository;

import java.util.HashMap;

public class ProjectTicketsViewModel extends AndroidViewModel {

    private ProjectTicketsRepository projectTicketsRepository;
    Application application;

    public ProjectTicketsViewModel(Application application) {
        super(application);
        this.application = application;
    }

    public LiveDataState<ProjectTickets> getProjectTickets(String token, int id) {
        projectTicketsRepository = new ProjectTicketsRepository(application, id, token);
        return projectTicketsRepository.getAllTickets();
    }

    public LiveDataState<EditTask> updateTickets(String token, int id, HashMap<String, String> updatedData) {
        projectTicketsRepository = new ProjectTicketsRepository(application, id, token, updatedData);
        return projectTicketsRepository.getUpdatedTask();
    }

    public LiveDataState<DeleteTicket> deleteProject(String token, int id) {
        projectTicketsRepository = new ProjectTicketsRepository(application, id, token, "");
        return projectTicketsRepository.getStatus();
    }

    public void clear() {
        projectTicketsRepository.clear();
    }

}

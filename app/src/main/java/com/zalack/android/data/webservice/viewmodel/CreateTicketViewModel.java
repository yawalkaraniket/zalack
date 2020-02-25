package com.zalack.android.data.webservice.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.zalack.android.data.models.create_ticket.CreateTicket;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.repository.CreateTicketRepository;

import java.util.HashMap;

public class CreateTicketViewModel extends AndroidViewModel {

    private CreateTicketRepository signUpRepository;
    Application application;

    public CreateTicketViewModel(Application application) {
        super(application);
        this.application = application;
    }

    public LiveDataState<CreateTicket> createTicket(String token, HashMap<String, String> user) {
        signUpRepository = new CreateTicketRepository(application, token, user);
        return signUpRepository.getAddedProject();
    }

    public void clear() {
        signUpRepository.clear();
    }

}


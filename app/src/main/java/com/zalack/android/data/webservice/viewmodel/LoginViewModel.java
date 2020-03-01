package com.zalack.android.data.webservice.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zalack.android.data.models.login.Login;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.repository.LoginRepository;

import java.util.HashMap;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository loginRepository;
    Application application;

    public LoginViewModel(Application application) {
        super(application);
        this.application = application;
    }

    public LiveDataState<Login> getUser(HashMap<String, String> user) {
        loginRepository = new LoginRepository(application, user);
        return loginRepository.getMutableLiveData();
    }

    public void clear() {
        if (loginRepository!=null) {
            loginRepository.clear();
        }
    }

}

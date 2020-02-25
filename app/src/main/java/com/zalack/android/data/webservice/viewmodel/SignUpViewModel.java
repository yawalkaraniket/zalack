package com.zalack.android.data.webservice.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.zalack.android.data.models.signup.SignUp;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.repository.SignUpRepository;

import java.util.HashMap;

public class SignUpViewModel extends AndroidViewModel {

    private SignUpRepository signUpRepository;
    Application application;

    public SignUpViewModel(Application application) {
        super(application);
        this.application = application;
    }

    public LiveDataState<SignUp> registerUser(HashMap<String, String> user) {
        signUpRepository = new SignUpRepository(application, user);
        return signUpRepository.getMutableLiveData();
    }

    public void clear() {
        signUpRepository.clear();
    }

}

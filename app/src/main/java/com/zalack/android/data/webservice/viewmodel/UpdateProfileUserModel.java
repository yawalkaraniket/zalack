package com.zalack.android.data.webservice.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.zalack.android.data.models.update_profile.ChangePassword;
import com.zalack.android.data.models.update_profile.UpdateProfile;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.repository.UpdateUserRepository;

import java.util.HashMap;

public class UpdateProfileUserModel extends AndroidViewModel {

    private UpdateUserRepository updateUserRepository;
    Application application;

    public UpdateProfileUserModel(Application application) {
        super(application);
        this.application = application;
    }

    public LiveDataState<UpdateProfile> updateUser(String token, HashMap<String, String> user) {
        updateUserRepository = new UpdateUserRepository(application,token, user);
        return updateUserRepository.getMutableLiveData();
    }

    public LiveDataState<ChangePassword> changePassword(String token, HashMap<String, String> user) {
        updateUserRepository = new UpdateUserRepository();
        updateUserRepository.init(application,token, user);
        return updateUserRepository.getUpdatedData();
    }

    public void clear() {
        updateUserRepository.clear();
    }

}


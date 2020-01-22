package com.zalack.android.data.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.zalack.android.data.room.repository.UserRepository;
import com.zalack.android.data.room.table.User;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);

        userRepository = new UserRepository(application);
    }

    public LiveData<List<User>> getUsers() {
        return userRepository.getMutableLiveData();
    }

    public void create(String email, String password) {
        userRepository.createUser(email, password);
    }

    public void update(User user) {
        userRepository.updateUser(user);
    }

    public void delete(User user) {
        userRepository.deleteUser(user);
    }

    public void clear() {
        userRepository.clear();
    }
}

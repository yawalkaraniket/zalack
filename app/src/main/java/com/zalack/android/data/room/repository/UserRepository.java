package com.zalack.android.data.room.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.zalack.android.data.room.UserDatabase;
import com.zalack.android.data.room.table.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class UserRepository {

    private Application application;
    private UserDatabase userDatabase;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MutableLiveData<List<User>> mutableLiveData = new MutableLiveData<>();

    public UserRepository(Application application) {
        this.application = application;

        // Creating name for the database.
        userDatabase = Room.databaseBuilder(application.getApplicationContext(), UserDatabase.class, "UserData").allowMainThreadQueries().build();

        compositeDisposable.add(userDatabase.getUserDao().getUser()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {

                    mutableLiveData.postValue(users);

                }, throwable -> {

                }));
    }

    public void createUser(String email, String password) {

        Completable.fromAction(() -> {
            long rowNumber = userDatabase.getUserDao().addUser(new User(email, password));
            Log.e("Row Number", String.valueOf(rowNumber));
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(application.getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(application.getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteUser(User user) {

        Completable.fromAction(() -> {
            userDatabase.getUserDao().deleteUser(user);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(application.getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(application.getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateUser(User user) {

        Completable.fromAction(() -> {
            userDatabase.getUserDao().update(user);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(application.getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(application.getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public MutableLiveData<List<User>> getMutableLiveData() {
        return mutableLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }
}

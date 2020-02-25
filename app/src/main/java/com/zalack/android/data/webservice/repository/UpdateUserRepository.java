package com.zalack.android.data.webservice.repository;

import android.app.Application;

import com.zalack.android.data.models.signup.SignUp;
import com.zalack.android.data.models.update_profile.ChangePassword;
import com.zalack.android.data.models.update_profile.UpdateProfile;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.RetrofitInstance;
import com.zalack.android.data.webservice.ZalckService;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class UpdateUserRepository {

    private Application application;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveDataState<UpdateProfile> mutableLiveData = new LiveDataState<>();
    Observable<UpdateProfile> updateProfileObservable;

    LiveDataState<ChangePassword> changePasswordLiveDataState = new LiveDataState<>();
    Observable<ChangePassword> changePasswordObservable;

    public UpdateUserRepository() {

    }

    public UpdateUserRepository(Application application, String token, HashMap<String, String> values) {
        this.application = application;

        ZalckService service = RetrofitInstance.getService();
        updateProfileObservable = service.updateProfile(token, values);
        compositeDisposable.add(updateProfileObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UpdateProfile>(){

                    @Override
                    public void onNext(UpdateProfile updateProfile) {
                        mutableLiveData.postSuccess(updateProfile);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mutableLiveData.postError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void init(Application application, String token, HashMap<String, String> values) {
        this.application = application;

        ZalckService service = RetrofitInstance.getService();
        changePasswordObservable = service.changePassword(token, values);
        compositeDisposable.add(changePasswordObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ChangePassword>(){

                    @Override
                    public void onNext(ChangePassword changePassword) {
                        changePasswordLiveDataState.postSuccess(changePassword);
                    }

                    @Override
                    public void onError(Throwable e) {
                        changePasswordLiveDataState.postError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public LiveDataState<UpdateProfile> getMutableLiveData() {
        return mutableLiveData;
    }

    public LiveDataState<ChangePassword> getUpdatedData() {
        return changePasswordLiveDataState;
    }


    public void clear() {
        compositeDisposable.clear();
    }

}

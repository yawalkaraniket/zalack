package com.zalack.android.data.webservice.repository;

import android.app.Application;

import com.zalack.android.data.models.login.Login;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.RetrofitInstance;
import com.zalack.android.data.webservice.ZalckService;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginRepository {

    private Application application;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveDataState<Login> mutableLiveData = new LiveDataState<>();
    Observable<Login> loginResponceObserver;

    public LoginRepository(Application application, HashMap<String, String> user) {
        this.application = application;

        ZalckService service = RetrofitInstance.getService();
        loginResponceObserver = service.login(user);
        compositeDisposable.add(loginResponceObserver.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Login>() {
                    @Override
                    public void onNext(Login user) {
                        mutableLiveData.postSuccess(user);
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

    public LiveDataState<Login> getMutableLiveData() {
        return mutableLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }

}

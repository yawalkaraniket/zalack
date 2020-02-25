package com.zalack.android.data.webservice.repository;

import android.app.Application;

import com.zalack.android.data.models.all_projects.AllProjects;
import com.zalack.android.data.models.signup.SignUp;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.RetrofitInstance;
import com.zalack.android.data.webservice.ZalckService;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SignUpRepository {

    private Application application;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveDataState<SignUp> mutableLiveData = new LiveDataState<>();
    Observable<SignUp> signUpObservable;

    public SignUpRepository(Application application, HashMap<String, String> values) {
        this.application = application;

        ZalckService service = RetrofitInstance.getService();
        signUpObservable = service.signUp(values);
        compositeDisposable.add(signUpObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SignUp>(){

                    @Override
                    public void onNext(SignUp signUp) {
                        mutableLiveData.postSuccess(signUp);
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

    public LiveDataState<SignUp> getMutableLiveData() {
        return mutableLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }
}

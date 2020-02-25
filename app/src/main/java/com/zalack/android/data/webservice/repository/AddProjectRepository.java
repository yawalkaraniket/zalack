package com.zalack.android.data.webservice.repository;

import android.app.Application;

import com.zalack.android.data.models.add_project.AddedProject;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.RetrofitInstance;
import com.zalack.android.data.webservice.ZalckService;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class AddProjectRepository {

    private Application application;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveDataState<AddedProject> mutableLiveData = new LiveDataState<>();
    Observable<AddedProject> signUpObservable;

    public AddProjectRepository(Application application,String token, HashMap<String, String> values) {
        this.application = application;

        ZalckService service = RetrofitInstance.getService();
        signUpObservable = service.addProject(token,values);
        compositeDisposable.add(signUpObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<AddedProject>(){

                    @Override
                    public void onNext(AddedProject signUp) {
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

    public LiveDataState<AddedProject> getAddedProject() {
        return mutableLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }
}

package com.zalack.android.data.webservice.repository;

import android.app.Application;

import com.zalack.android.data.models.all_projects.AllProjects;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.RetrofitInstance;
import com.zalack.android.data.webservice.ZalckService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class AllProjectRepository {

    private Application application;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveDataState<AllProjects> mutableLiveData = new LiveDataState<>();
    Observable<AllProjects> allProjectsObservable;

    public AllProjectRepository(Application application, String token) {
        this.application = application;

        ZalckService service = RetrofitInstance.getService();
        allProjectsObservable = service.getAllProjects(token);
        compositeDisposable.add(allProjectsObservable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<AllProjects>(){

            @Override
            public void onNext(AllProjects allProjects) {
                mutableLiveData.postSuccess(allProjects);
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

    public LiveDataState<AllProjects> getMutableLiveData() {
        return mutableLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }
}

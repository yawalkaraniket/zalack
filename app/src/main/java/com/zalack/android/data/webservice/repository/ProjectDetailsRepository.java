package com.zalack.android.data.webservice.repository;

import android.app.Application;

import com.zalack.android.data.models.project_details.ProjectDetails;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.RetrofitInstance;
import com.zalack.android.data.webservice.ZalckService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ProjectDetailsRepository {

    private Application application;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveDataState<ProjectDetails> mutableLiveData = new LiveDataState<>();
    Observable<ProjectDetails> projectDetailsObservable;

    public ProjectDetailsRepository(Application application, int ticket_id, String token) {
        this.application = application;

        ZalckService service = RetrofitInstance.getService();
        projectDetailsObservable = service.getProject(ticket_id, token);

        compositeDisposable.add(projectDetailsObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ProjectDetails>(){

                    @Override
                    public void onNext(ProjectDetails projectTickets) {
                        mutableLiveData.postSuccess(projectTickets);
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

    public LiveDataState<ProjectDetails> getMutableLiveData() {
        return mutableLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }


}

package com.zalack.android.data.webservice.repository;

import android.app.Application;

import com.zalack.android.data.models.all_projects.AllProjects;
import com.zalack.android.data.models.project_tickets.ProjectTickets;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.RetrofitInstance;
import com.zalack.android.data.webservice.ZalckService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ProjectTicketsRepository {

    private Application application;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveDataState<ProjectTickets> mutableLiveData = new LiveDataState<>();
    LiveDataState<ProjectTickets> todoData = new LiveDataState<>();
    Observable<ProjectTickets> projectTicketsObservable;

    public ProjectTicketsRepository(Application application, int ticket_id, String token) {
        this.application = application;

        ZalckService service = RetrofitInstance.getService();
        projectTicketsObservable = service.getTickets(ticket_id, token);

        compositeDisposable.add(projectTicketsObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ProjectTickets>() {

                    @Override
                    public void onNext(ProjectTickets projectTickets) {
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

    public LiveDataState<ProjectTickets> getAllTickets() {
        return mutableLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }

}


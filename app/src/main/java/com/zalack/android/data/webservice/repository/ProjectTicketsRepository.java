package com.zalack.android.data.webservice.repository;

import android.app.Application;
import android.widget.EditText;

import com.zalack.android.data.models.all_projects.AllProjects;
import com.zalack.android.data.models.delet_ticket.DeleteTicket;
import com.zalack.android.data.models.edit_task.EditTask;
import com.zalack.android.data.models.project_tickets.ProjectTickets;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.RetrofitInstance;
import com.zalack.android.data.webservice.ZalckService;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ProjectTicketsRepository {

    private Application application;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveDataState<EditTask> mutableLiveDataForTask = new LiveDataState<>();
    LiveDataState<ProjectTickets> mutableLiveData = new LiveDataState<>();
    Observable<ProjectTickets> projectTicketsObservable;
    Observable<EditTask> editTaskObservable;

    LiveDataState<DeleteTicket> mutableDeleteData = new LiveDataState<>();
    Observable<DeleteTicket> deleteTicketObservable;


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

    public ProjectTicketsRepository(Application application, int ticket_id, String token, HashMap<String, String> updatedDetails) {
        this.application = application;

        ZalckService service = RetrofitInstance.getService();
        editTaskObservable = service.updateTicket(ticket_id, token, updatedDetails);

        compositeDisposable.add(editTaskObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<EditTask>() {

                    @Override
                    public void onNext(EditTask projectTickets) {
                        mutableLiveDataForTask.postSuccess(projectTickets);
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

    public ProjectTicketsRepository(Application application, int id, String token, String s) {
        this.application = application;

        ZalckService service = RetrofitInstance.getService();
        deleteTicketObservable = service.deleteTicket(id, token);

        compositeDisposable.add(deleteTicketObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<DeleteTicket>() {

                    @Override
                    public void onNext(DeleteTicket deleteTicket) {
                        mutableDeleteData.postSuccess(deleteTicket);
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

    public LiveDataState<EditTask> getUpdatedTask() {
        return mutableLiveDataForTask;
    }

    public LiveDataState<DeleteTicket> getStatus() {
        return mutableDeleteData;
    }

    public void clear() {
        compositeDisposable.clear();
    }

}


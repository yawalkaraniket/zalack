package com.zalack.android.data.webservice.repository;

import android.app.Application;

import com.zalack.android.data.models.add_project.AddedProject;
import com.zalack.android.data.models.delete_project.DeleteProject;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.RetrofitInstance;
import com.zalack.android.data.webservice.ZalckService;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DeleteProjectRepository {

    private Application application;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveDataState<DeleteProject> mutableLiveData = new LiveDataState<>();
    Observable<DeleteProject> deleteProjectObservable;

    public DeleteProjectRepository(Application application,String token, int projectId) {
        this.application = application;

        ZalckService service = RetrofitInstance.getService();
        deleteProjectObservable = service.deleteProject(projectId, token);
        compositeDisposable.add(deleteProjectObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<DeleteProject>(){

                    @Override
                    public void onNext(DeleteProject deleteProject) {
                        mutableLiveData.postSuccess(deleteProject);
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

    public LiveDataState<DeleteProject> getAddedProject() {
        return mutableLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }
}

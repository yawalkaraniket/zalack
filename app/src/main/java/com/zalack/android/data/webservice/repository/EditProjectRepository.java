package com.zalack.android.data.webservice.repository;

import android.app.Application;

import com.zalack.android.data.models.add_project.AddedProject;
import com.zalack.android.data.models.edit_project.EditProjectDetails;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.RetrofitInstance;
import com.zalack.android.data.webservice.ZalckService;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class EditProjectRepository {

    private Application application;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveDataState<EditProjectDetails> mutableLiveData = new LiveDataState<>();
    Observable<EditProjectDetails> signUpObservable;

    public EditProjectRepository(Application application, int projectId, String token, HashMap<String, String> values) {
        this.application = application;

        ZalckService service = RetrofitInstance.getService();
        signUpObservable = service.editProject(projectId, token, values);
        compositeDisposable.add(signUpObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<EditProjectDetails>(){

                    @Override
                    public void onNext(EditProjectDetails editProjectDetails) {
                        mutableLiveData.postSuccess(editProjectDetails);
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

    public LiveDataState<EditProjectDetails> getEditedProject() {
        return mutableLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }
}

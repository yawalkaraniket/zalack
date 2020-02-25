package com.zalack.android.data.webservice.repository;

import android.app.Application;

import com.zalack.android.data.models.add_project.AddedProject;
import com.zalack.android.data.models.create_ticket.CreateTicket;
import com.zalack.android.data.webservice.LiveDataState;
import com.zalack.android.data.webservice.RetrofitInstance;
import com.zalack.android.data.webservice.ZalckService;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CreateTicketRepository {
    private Application application;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveDataState<CreateTicket> mutableLiveData = new LiveDataState<>();
    Observable<CreateTicket> signUpObservable;

    public CreateTicketRepository(Application application,String token, HashMap<String, String> values) {
        this.application = application;

        ZalckService service = RetrofitInstance.getService();
        signUpObservable = service.createTicket(token,values);
        compositeDisposable.add(signUpObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CreateTicket>(){

                    @Override
                    public void onNext(CreateTicket signUp) {
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

    public LiveDataState<CreateTicket> getAddedProject() {
        return mutableLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }
}

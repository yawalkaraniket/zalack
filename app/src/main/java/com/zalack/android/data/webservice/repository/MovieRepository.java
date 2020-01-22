package com.zalack.android.data.webservice.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.zalack.android.R;
import com.zalack.android.data.models.MovieResponse;
import com.zalack.android.data.webservice.RetrofitInstance;
import com.zalack.android.data.webservice.ZalackService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieRepository {

    private Application application;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MutableLiveData<MovieResponse> mutableLiveData = new MutableLiveData<>();
    Observable<MovieResponse> movieResponceObservable;

    public MovieRepository(Application application) {
        this.application = application;

        ZalackService service = RetrofitInstance.getService();
        movieResponceObservable = service.getPopularMovies(application.getString(R.string.api_key));

        compositeDisposable.add(movieResponceObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MovieResponse>() {
                    @Override
                    public void onNext(MovieResponse movieResponse) {

                        mutableLiveData.postValue(movieResponse);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public MutableLiveData<MovieResponse> getMutableLiveData() {
        return mutableLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }
}

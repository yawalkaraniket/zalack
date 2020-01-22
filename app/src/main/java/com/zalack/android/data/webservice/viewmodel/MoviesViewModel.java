package com.zalack.android.data.webservice.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zalack.android.data.models.MovieResponse;
import com.zalack.android.data.webservice.repository.MovieRepository;

public class MoviesViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    public MoviesViewModel(@NonNull Application application) {
        super(application);

        movieRepository = new MovieRepository(application);
    }

    public MutableLiveData<MovieResponse> getAllMovies() {
        return movieRepository.getMutableLiveData();
    }

    public void clear() {
        movieRepository.clear();
    }
}

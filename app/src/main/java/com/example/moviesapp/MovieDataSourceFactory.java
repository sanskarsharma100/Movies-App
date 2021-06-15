package com.example.moviesapp;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.moviesapp.models.MovieModel;

import org.jetbrains.annotations.NotNull;

public class MovieDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, MovieModel>> mMovieLiveData = new MutableLiveData<>();
    ;
    private String sortCriteria;

    public MovieDataSourceFactory(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    @Override
    public @NotNull DataSource create() {
        MovieDataSource movieDataSource = new MovieDataSource(sortCriteria);
        mMovieLiveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, MovieModel>> getMovieLiveData() {
        return mMovieLiveData;
    }
}

package com.example.moviesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.data.MovieEntry;
import com.example.moviesapp.repository.MovieRepository;

public class FavouriteViewModel extends ViewModel {

    private final MovieRepository movieRepository;
    private LiveData<MovieEntry> movieEntryLiveData;

    public FavouriteViewModel(MovieRepository movieRepository, int movieId) {
        this.movieRepository = movieRepository;
        this.movieEntryLiveData = movieRepository.getFavouriteMovieFromId(movieId);
    }

    public LiveData<MovieEntry> getMovieEntry() {
        return movieEntryLiveData;
    }
}

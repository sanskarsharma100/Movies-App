package com.example.moviesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.models.MovieDetailsModel;
import com.example.moviesapp.repository.MovieRepository;

public class MovieDetailsViewModel extends ViewModel {

    private MovieRepository mMovieRepository;
    private LiveData<MovieDetailsModel> mMovieDetails;

    public MovieDetailsViewModel(MovieRepository repository, String movieId) {
        this.mMovieRepository = repository;
        this.mMovieDetails = mMovieRepository.requestMovieDetails(movieId);
    }

    public LiveData<MovieDetailsModel> getMovieDetails() {
        return mMovieDetails;
    }
}

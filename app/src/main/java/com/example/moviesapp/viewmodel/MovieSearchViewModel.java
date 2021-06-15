package com.example.moviesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.repository.MovieRepository;
import com.example.moviesapp.response.MovieResponse;

public class MovieSearchViewModel extends ViewModel {
    private MovieRepository movieRepository;
    private LiveData<MovieResponse> searchedMovieList;

    public MovieSearchViewModel(MovieRepository movieRepository, String query) {
        this.movieRepository = movieRepository;
        this.searchedMovieList = movieRepository.requestSearchedMovies(query);
    }

    public LiveData<MovieResponse> getSearchedMovieList() {
        return searchedMovieList;
    }
}

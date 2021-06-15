package com.example.moviesapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviesapp.repository.MovieRepository;

import org.jetbrains.annotations.NotNull;

public class MovieDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieRepository movieRepository;
    private final String movieId;


    public MovieDetailsViewModelFactory(MovieRepository movieRepository, String movieId) {
        this.movieRepository = movieRepository;
        this.movieId = movieId;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieDetailsViewModel(movieRepository, movieId);
    }
}

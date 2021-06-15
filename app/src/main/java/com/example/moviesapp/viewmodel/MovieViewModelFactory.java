package com.example.moviesapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviesapp.repository.MovieRepository;

import org.jetbrains.annotations.NotNull;

public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieRepository movieRepository;
    private final String sortCriteria;

    public MovieViewModelFactory(MovieRepository movieRepository, String sortCriteria) {
        this.movieRepository = movieRepository;
        this.sortCriteria = sortCriteria;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieViewModel(movieRepository, sortCriteria);
    }
}

package com.example.moviesapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviesapp.repository.MovieRepository;

import org.jetbrains.annotations.NotNull;

public class MovieSearchViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MovieRepository movieRepository;
    private final String query;

    public MovieSearchViewModelFactory(MovieRepository movieRepository, String query) {
        this.movieRepository = movieRepository;
        this.query = query;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieSearchViewModel(movieRepository, query);
    }
}

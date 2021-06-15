package com.example.moviesapp;

import android.util.Log;

import androidx.paging.PageKeyedDataSource;

import com.example.moviesapp.models.MovieModel;
import com.example.moviesapp.response.MovieResponse;
import com.example.moviesapp.util.Constant;
import com.example.moviesapp.util.Controller;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Integer, MovieModel> {

    private static final String LOG_TAG = MovieDataSource.class.getSimpleName();

    private final String sortBy;

    public MovieDataSource(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public void loadAfter(@NotNull LoadParams<Integer> loadParams, @NotNull LoadCallback<Integer, MovieModel> loadCallback) {
        int currentPage = loadParams.key;
        Controller.getMovieApi().getMovies(sortBy, Constant.API_KEY, currentPage)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<MovieResponse> call, @NotNull Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            int nextPage = currentPage + 1;
                            Log.i(LOG_TAG, String.valueOf(nextPage));
                            assert response.body() != null;
                            loadCallback.onResult(response.body().getMovies(), nextPage);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<MovieResponse> call, @NotNull Throwable t) {
                        Log.e(LOG_TAG, "Failed appending page: " + t.getMessage());
                    }
                });
    }

    @Override
    public void loadBefore(@NotNull LoadParams<Integer> loadParams, @NotNull LoadCallback<Integer, MovieModel> loadCallback) {

    }

    @Override
    public void loadInitial(@NotNull LoadInitialParams<Integer> loadInitialParams, @NotNull LoadInitialCallback<Integer, MovieModel> loadInitialCallback) {
        Controller.getMovieApi().getMovies(sortBy, Constant.API_KEY, Constant.FIRST_PAGE)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<MovieResponse> call, @NotNull Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            loadInitialCallback.onResult(response.body().getMovies(),
                                    Constant.PREVIOUS_PAGE, Constant.NEXT_PAGE);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<MovieResponse> call, @NotNull Throwable t) {
                        Log.e(LOG_TAG, "Failed initializing a PageList: " + t.getMessage());
                    }
                });
    }
}

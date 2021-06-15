package com.example.moviesapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviesapp.data.MovieDao;
import com.example.moviesapp.data.MovieDatabase;
import com.example.moviesapp.data.MovieEntry;
import com.example.moviesapp.models.MovieDetailsModel;
import com.example.moviesapp.response.MovieResponse;
import com.example.moviesapp.response.ReviewResponse;
import com.example.moviesapp.response.VideoResponse;
import com.example.moviesapp.util.Constant;
import com.example.moviesapp.util.Controller;
import com.example.moviesapp.util.MovieApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private static MovieRepository instance = null;
    private final MovieApi apiRequest;
    private final MovieDao movieDao;

    public MovieRepository(Application application) {
        MovieDatabase mDb = MovieDatabase.getInstance(application);
        this.movieDao = mDb.movieDao();
        this.apiRequest = Controller.getMovieApi();
    }

    public LiveData<MovieDetailsModel> requestMovieDetails(String movieId) {
        final MutableLiveData<MovieDetailsModel> mutableLiveData = new MutableLiveData<>();
        Call<MovieDetailsModel> call = apiRequest.getDetails(
                movieId,
                Constant.API_KEY,
                Constant.CREDITS
        );
        call.enqueue(new Callback<MovieDetailsModel>() {
            @Override
            public void onResponse(Call<MovieDetailsModel> call, Response<MovieDetailsModel> response) {
                if (response.body() != null) {
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieDetailsModel> call, Throwable t) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public LiveData<VideoResponse> requestVideos(String movieId) {
        final MutableLiveData<VideoResponse> mutableLiveData = new MutableLiveData<>();
        Call<VideoResponse> call = apiRequest.getVideos(
                movieId,
                Constant.API_KEY
        );
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                mutableLiveData.setValue(null);

            }
        });
        return mutableLiveData;
    }

    public LiveData<ReviewResponse> requestReviews(String movieId) {
        final MutableLiveData<ReviewResponse> mutableLiveData = new MutableLiveData<>();
        Call<ReviewResponse> call = apiRequest.getReviews(
                movieId,
                Constant.API_KEY
        );
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public LiveData<MovieResponse> requestSearchedMovies(String query) {
        final MutableLiveData<MovieResponse> mutableLiveData = new MutableLiveData<>();
        Call<MovieResponse> call = apiRequest.getSearchedMovies(
                Constant.API_KEY,
                query,
                Constant.FIRST_PAGE
        );
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public LiveData<List<MovieEntry>> getFavouriteMovie() {
        return movieDao.loadAllMovies();
    }

    public LiveData<MovieEntry> getFavouriteMovieFromId(int movieId) {
        return movieDao.loadMovieFromMovieId(movieId);
    }

}

package com.example.moviesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.repository.MovieRepository;
import com.example.moviesapp.response.ReviewResponse;

public class ReviewViewModel extends ViewModel {

    private final LiveData<ReviewResponse> reviewResponseLiveData;


    public ReviewViewModel(MovieRepository movieRepository, String movieId) {
        reviewResponseLiveData = movieRepository.requestReviews(movieId);
    }

    public LiveData<ReviewResponse> getReviewDetails() {
        return reviewResponseLiveData;
    }
}

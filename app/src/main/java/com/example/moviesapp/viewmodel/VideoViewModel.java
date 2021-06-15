package com.example.moviesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.repository.MovieRepository;
import com.example.moviesapp.response.VideoResponse;

public class VideoViewModel extends ViewModel {

    private final LiveData<VideoResponse> videoModelLiveData;

    public VideoViewModel(MovieRepository mMovieRepository, String movieId) {
        this.videoModelLiveData = mMovieRepository.requestVideos(movieId);
    }

    public LiveData<VideoResponse> getVideoDetails() {
        return videoModelLiveData;
    }
}

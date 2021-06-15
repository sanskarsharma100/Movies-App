package com.example.moviesapp.response;

import com.example.moviesapp.models.VideoModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VideoResponse {

    @SerializedName("id")
    @Expose
    private String movieId;

    @SerializedName("results")
    @Expose
    private List<VideoModel> videosList = new ArrayList<>();

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public List<VideoModel> getVideosList() {
        return videosList;
    }

    public void setVideosList(List<VideoModel> videosList) {
        this.videosList = videosList;
    }
}

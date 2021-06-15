package com.example.moviesapp.response;

import com.example.moviesapp.models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieResponse {

    @SerializedName("results")
    @Expose
    List<MovieModel> movieList = new ArrayList<>();

    public List<MovieModel> getMovies() {
        return movieList;
    }

    public void setMovies(List<MovieModel> movieList) {
        this.movieList = movieList;
    }
}

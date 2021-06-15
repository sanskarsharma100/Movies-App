package com.example.moviesapp.util;

import com.example.moviesapp.models.MovieDetailsModel;
import com.example.moviesapp.response.MovieResponse;
import com.example.moviesapp.response.ReviewResponse;
import com.example.moviesapp.response.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("movie/{sort_by}?")
    Call<MovieResponse> getMovies(
            @Path("sort_by") String sort_by,
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("movie/{movie_id}?")
    Call<MovieDetailsModel> getDetails(
            @Path("movie_id") String movie_id,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String credits
    );

    @GET("movie/{movie_id}/videos")
    Call<VideoResponse> getVideos(
            @Path("movie_id") String movie_id,
            @Query("api_key") String apiKey
    );

    //movie/791373/reviews?api_key=41e51cbf0e10b0fa778b49e1f9e24aa1
    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getReviews(
            @Path("movie_id") String movie_id,
            @Query("api_key") String apiKey
    );

    //https://api.themoviedb.org/3/search/movie
    @GET("search/movie")
    Call<MovieResponse> getSearchedMovies(
            @Query("api_key") String apiKey,
            @Query("query") String searchQuery,
            @Query("page") int page
    );
}

package com.example.moviesapp.models;

import com.google.gson.annotations.SerializedName;

public class AuthorDetailModel {

    @SerializedName("rating")
    private String userReviewRating;

    public AuthorDetailModel() {
    }

    public String getUserReviewRating() {
        return userReviewRating;
    }

    public void setUserReviewRating(String userReviewRating) {
        this.userReviewRating = userReviewRating;
    }
}

package com.example.moviesapp.response;

import com.example.moviesapp.models.ReviewModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResponse {

    @SerializedName("results")
    @Expose
    private List<ReviewModel> reviewList;

    public List<ReviewModel> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<ReviewModel> reviewList) {
        this.reviewList = reviewList;
    }
}

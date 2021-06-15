package com.example.moviesapp.models;

import com.google.gson.annotations.SerializedName;

public class ReviewModel {

    @SerializedName("author")
    private String ReviewAuthor;

    @SerializedName("content")
    private String reviewContent;

    @SerializedName("updated_at")
    private String reviewUpdateDate;

    @SerializedName("author_details")
    private AuthorDetailModel reviewAuthorDetails;

    public ReviewModel() {
    }

    public String getReviewAuthor() {
        return ReviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        ReviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getReviewUpdateDate() {
        return reviewUpdateDate;
    }

    public void setReviewUpdateDate(String reviewUpdateDate) {
        this.reviewUpdateDate = reviewUpdateDate;
    }

    public AuthorDetailModel getReviewAuthorDetails() {
        return reviewAuthorDetails;
    }

    public void setReviewAuthorDetails(AuthorDetailModel reviewAuthorDetails) {
        this.reviewAuthorDetails = reviewAuthorDetails;
    }
}

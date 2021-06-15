package com.example.moviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.moviesapp.util.FormatUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDetailsModel implements Parcelable {

    public static final Creator<MovieDetailsModel> CREATOR = new Creator<MovieDetailsModel>() {
        @Override
        public MovieDetailsModel createFromParcel(Parcel in) {
            return new MovieDetailsModel(in);
        }

        @Override
        public MovieDetailsModel[] newArray(int size) {
            return new MovieDetailsModel[size];
        }
    };
    @SerializedName("budget")
    private String movieBudget;
    @SerializedName("revenue")
    private String movieRevenue;
    @SerializedName("runtime")
    private String movieRuntime;
    @SerializedName("status")
    private String movieStatus;
    @SerializedName("original_title")
    private String movieOriginalTitle;
    @SerializedName("genres")
    private List<GenreModel> movieGenre = null;
    @SerializedName("credits")
    private CreditsModel movieCredits;

    public MovieDetailsModel(String movieBudget, String movieRevenue, String movieRuntime, String movieStatus, String movieOriginalTitle, List<GenreModel> movieGenre, CreditsModel movieCredits) {
        this.movieBudget = movieBudget;
        this.movieRevenue = movieRevenue;
        this.movieRuntime = movieRuntime;
        this.movieStatus = movieStatus;
        this.movieOriginalTitle = movieOriginalTitle;
        this.movieGenre = movieGenre;
        this.movieCredits = movieCredits;
    }

    protected MovieDetailsModel(Parcel in) {
        movieBudget = in.readString();
        movieRevenue = in.readString();
        movieRuntime = in.readString();
        movieStatus = in.readString();
        movieOriginalTitle = in.readString();
    }

    public String getMovieBudget() {
        return FormatUtils.formatCurrency(Integer.parseInt(movieBudget));
    }

    public String getMovieRevenue() {
        return FormatUtils.formatCurrency(Integer.parseInt(movieRevenue));
    }

    public String getMovieRuntime() {
        return movieRuntime;
    }

    public String getMovieStatus() {
        return movieStatus;
    }

    public List<GenreModel> getMovieGenre() {
        return movieGenre;
    }

    public String getMovieOriginalTitle() {
        return movieOriginalTitle;
    }

    public CreditsModel getMovieCredits() {
        return movieCredits;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieBudget);
        parcel.writeString(movieRevenue);
        parcel.writeString(movieRuntime);
        parcel.writeString(movieStatus);
        parcel.writeString(movieOriginalTitle);
    }
}

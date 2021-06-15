package com.example.moviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.moviesapp.util.Constant;
import com.google.gson.annotations.SerializedName;

public class MovieModel implements Parcelable {
    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
    @SerializedName("title")
    private String mMovieName;
    @SerializedName("original_title")
    private String mOriginalMovieTitle;
    @SerializedName("poster_path")
    private String mMovieImage;
    @SerializedName("overview")
    private String mMovieOverview;
    @SerializedName("release_date")
    private String mMovieReleaseDate;
    @SerializedName("vote_average")
    private String mMovieRating;
    @SerializedName("backdrop_path")
    private String mMovieHztImage;
    @SerializedName("id")
    private String mMovieId;
    @SerializedName("vote_count")
    private String mMovieVoters;

    public MovieModel(String MovieName, String originalMovieTitle, String movieImage, String MovieOverview,
                      String MovieReleaseDate, String MovieHztImage, String MovieRating,
                      String MovieId, String mMovieVoters) {
        this.mMovieName = MovieName;
        this.mOriginalMovieTitle = originalMovieTitle;
        this.mMovieImage = movieImage;
        this.mMovieOverview = MovieOverview;
        this.mMovieReleaseDate = MovieReleaseDate;
        this.mMovieRating = MovieRating;
        this.mMovieHztImage = MovieHztImage;
        this.mMovieId = MovieId;
        this.mMovieVoters = mMovieVoters;
    }

    protected MovieModel(Parcel in) {
        mMovieName = in.readString();
        mOriginalMovieTitle = in.readString();
        mMovieImage = in.readString();
        mMovieOverview = in.readString();
        mMovieReleaseDate = in.readString();
        mMovieRating = in.readString();
        mMovieHztImage = in.readString();
        mMovieId = in.readString();
        mMovieVoters = in.readString();
    }

    public String getMovieName() {
        return mMovieName;
    }

    public String getOriginalMovieTitle() {
        return mOriginalMovieTitle;
    }

    public String getMovieImage() {
        return Constant.MOVIE_POSTER_URL + mMovieImage;
    }

    public String getMovieOverview() {
        return mMovieOverview;
    }

    public String getMovieReleaseDate() {
        return mMovieReleaseDate;
    }

    public String getMovieRating() {
        return mMovieRating;
    }

    public String getMovieHztImage() {
        return Constant.MOVIE_POSTER_URL + mMovieHztImage;
    }

    public String getMovieId() {
        return mMovieId;
    }

    public String getMovieVoters() {
        return mMovieVoters;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mMovieName);
        parcel.writeString(mOriginalMovieTitle);
        parcel.writeString(mMovieImage);
        parcel.writeString(mMovieOverview);
        parcel.writeString(mMovieReleaseDate);
        parcel.writeString(mMovieRating);
        parcel.writeString(mMovieHztImage);
        parcel.writeString(mMovieId);
        parcel.writeString(mMovieVoters);
    }


}

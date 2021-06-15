package com.example.moviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.moviesapp.util.Constant;
import com.google.gson.annotations.SerializedName;

public class CastModel implements Parcelable {

    public static final Creator<CastModel> CREATOR = new Creator<CastModel>() {
        @Override
        public CastModel createFromParcel(Parcel in) {
            return new CastModel(in);
        }

        @Override
        public CastModel[] newArray(int size) {
            return new CastModel[size];
        }
    };
    @SerializedName("name")
    private String castName;
    @SerializedName("character")
    private String movieCharacter;
    @SerializedName("profile_path")
    private String castImage;

    protected CastModel(Parcel in) {
        castName = in.readString();
        movieCharacter = in.readString();
        castImage = in.readString();
    }

    public String getCastName() {
        return castName;
    }

    public void setCastName(String castName) {
        this.castName = castName;
    }

    public String getMovieCharacter() {
        return movieCharacter;
    }

    public void setMovieCharacter(String movieCharacter) {
        this.movieCharacter = movieCharacter;
    }

    public String getCastImage() {
        return Constant.MOVIE_POSTER_URL + castImage;
    }

    public void setCastImage(String castImage) {
        this.castImage = castImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(castName);
        parcel.writeString(movieCharacter);
        parcel.writeString(castImage);
    }
}

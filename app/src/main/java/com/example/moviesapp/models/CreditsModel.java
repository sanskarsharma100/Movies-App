package com.example.moviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditsModel implements Parcelable {

    public static final Creator<CreditsModel> CREATOR = new Creator<CreditsModel>() {
        @Override
        public CreditsModel createFromParcel(Parcel in) {
            return new CreditsModel(in);
        }

        @Override
        public CreditsModel[] newArray(int size) {
            return new CreditsModel[size];
        }
    };
    @SerializedName("cast")
    private List<CastModel> cast;
    @SerializedName("crew")
    private List<CrewModel> crew;

    protected CreditsModel(Parcel in) {
        cast = in.createTypedArrayList(CastModel.CREATOR);
        crew = in.createTypedArrayList(CrewModel.CREATOR);
    }

    public List<CastModel> getCast() {
        return cast;
    }

    public void setCast(List<CastModel> cast) {
        this.cast = cast;
    }

    public List<CrewModel> getCrew() {
        return crew;
    }

    public void setCrew(List<CrewModel> crew) {
        this.crew = crew;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(cast);
    }
}

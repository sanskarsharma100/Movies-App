package com.example.moviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CrewModel implements Parcelable {

    public static final Creator<CrewModel> CREATOR = new Creator<CrewModel>() {
        @Override
        public CrewModel createFromParcel(Parcel in) {
            return new CrewModel(in);
        }

        @Override
        public CrewModel[] newArray(int size) {
            return new CrewModel[size];
        }
    };
    @SerializedName("name")
    private String crewName;
    @SerializedName("job")
    private String crewJob;

    protected CrewModel(Parcel in) {
        crewName = in.readString();
        crewJob = in.readString();
    }

    public String getCrewName() {
        return crewName;
    }

    public void setCrewName(String crewName) {
        this.crewName = crewName;
    }

    public String getCrewJob() {
        return crewJob;
    }

    public void setCrewJob(String crewJob) {
        this.crewJob = crewJob;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(crewName);
        parcel.writeString(crewJob);
    }
}

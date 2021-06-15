package com.example.moviesapp.models;

import com.google.gson.annotations.SerializedName;

public class VideoModel {

    @SerializedName("id")
    private String videoId;

    @SerializedName("key")
    private String videoKey;

    @SerializedName("name")
    private String videoName;

    @SerializedName("site")
    private String videoSite;

    @SerializedName("size")
    private String videoSize;

    @SerializedName("type")
    private String videoType;

    public VideoModel() {
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoSite() {
        return videoSite;
    }

    public void setVideoSite(String videoSite) {
        this.videoSite = videoSite;
    }

    public String getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(String videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
}

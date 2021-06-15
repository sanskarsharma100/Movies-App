package com.example.moviesapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "movie")
public class MovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "movie_id")
    private int movieId;

    @ColumnInfo(name = "original_title")
    private String originalTitle;

    private String title;

    @ColumnInfo(name = "poster_path")
    private String posterPath;

    private String overview;

    @ColumnInfo(name = "vote_average")
    private double voteAverage;

    @ColumnInfo(name = "release_date")
    private String releaseDate;

    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;

    @ColumnInfo(name = "vote_count")
    private String voteCount;

    private Date date;

    private String runtime;

    private String genre;

    @Ignore
    public MovieEntry(int movieId, String originalTitle, String title,
                      String posterPath, String overview, double voteAverage,
                      String releaseDate, String backdropPath, Date date,
                      String runtime, String genre, String voteCount) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
        this.date = date;
        this.runtime = runtime;
        this.genre = genre;
        this.voteCount = voteCount;
    }

    // Constructor used by room
    public MovieEntry(int id, int movieId, String originalTitle, String title,
                      String posterPath, String overview, double voteAverage,
                      String releaseDate, String backdropPath, Date date,
                      String runtime, String genre, String voteCount) {
        this.id = id;
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
        this.date = date;
        this.runtime = runtime;
        this.genre = genre;
        this.voteCount = voteCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }
}

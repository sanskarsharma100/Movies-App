package com.example.moviesapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<MovieEntry>> loadAllMovies();

    @Insert
    void insertMovie(MovieEntry movieEntry);

    @Delete
    void deleteMovie(MovieEntry movieEntry);

    @Query("SELECT * FROM movie WHERE movie_id = :movieId")
    LiveData<MovieEntry> loadMovieFromMovieId(int movieId);
}

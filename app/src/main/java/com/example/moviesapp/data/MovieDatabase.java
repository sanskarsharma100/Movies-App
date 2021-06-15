package com.example.moviesapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.moviesapp.util.Constant;

@Database(entities = {MovieEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class MovieDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, Constant.DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();

}

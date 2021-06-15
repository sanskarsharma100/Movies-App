package com.example.moviesapp.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {
    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constant.MOVIE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static MovieApi movieApi = retrofit.create(MovieApi.class);

    public static MovieApi getMovieApi() {
        return movieApi;
    }
}

package com.example.moviesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.moviesapp.MovieDataSourceFactory;
import com.example.moviesapp.data.MovieEntry;
import com.example.moviesapp.models.MovieModel;
import com.example.moviesapp.repository.MovieRepository;
import com.example.moviesapp.util.Constant;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieViewModel extends ViewModel {

    private final MovieRepository movieRepository;
    private LiveData<PagedList<MovieModel>> moviePagedList;
    private LiveData<List<MovieEntry>> FavMovies;

    public MovieViewModel(MovieRepository movieRepository, String sortCriteria) {
        this.movieRepository = movieRepository;
        init(sortCriteria);
    }

    private void init(String sortCriteria) {
        Executor executor = Executors.newFixedThreadPool(3);
        MovieDataSourceFactory movieDataSourceFactory = new MovieDataSourceFactory(sortCriteria);

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(Constant.INITIAL_LOAD_SIZE_HINT)
                .setPageSize(Constant.SET_PAGE_SIZE)
                .setPrefetchDistance(Constant.SET_PREFETCH_DISTANCE)
                .build();

        moviePagedList = new LivePagedListBuilder<>(movieDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build();
    }

    public void setCriteria(String sortCriteria) {
        init(sortCriteria);
    }

    public LiveData<PagedList<MovieModel>> getMoviePagedList() {
        return moviePagedList;
    }

    public LiveData<List<MovieEntry>> getFavMovies() {
        return FavMovies;
    }

    public void setFavMovies() {
        FavMovies = movieRepository.getFavouriteMovie();
    }
}

package com.example.moviesapp.main;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviesapp.R;
import com.example.moviesapp.data.MovieEntry;
import com.example.moviesapp.databinding.FragmentFavoriteBinding;
import com.example.moviesapp.databinding.MovieTilesBinding;
import com.example.moviesapp.detailactivity.DetailActivity;
import com.example.moviesapp.models.MovieModel;
import com.example.moviesapp.repository.MovieRepository;
import com.example.moviesapp.util.Constant;
import com.example.moviesapp.viewmodel.MovieViewModel;
import com.example.moviesapp.viewmodel.MovieViewModelFactory;

import java.util.List;

public class FavoriteFragment extends Fragment implements FavouriteAdapter.FavouriteOnClickListener {

    private FavouriteAdapter favouriteAdapter;
    private MovieViewModel movieViewModel;
    private FragmentFavoriteBinding favoriteBinding;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(Constant.FAVORITES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        favoriteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        View rootView = favoriteBinding.getRoot();
        initAdapter();
        initViewModel();
        setupUI();
        return rootView;
    }

    private void initAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        favoriteBinding.moviesRecyclerView.setHasFixedSize(true);
        favoriteBinding.moviesRecyclerView.setLayoutManager(gridLayoutManager);
        favouriteAdapter = new FavouriteAdapter(this);
    }

    private void initViewModel() {
        MovieRepository movieRepository = new MovieRepository(getActivity().getApplication());
        movieViewModel =
                new ViewModelProvider(this, new MovieViewModelFactory(movieRepository, "popular"))
                        .get(MovieViewModel.class);
    }

    private void setupUI() {
        movieViewModel.setFavMovies();
        favoriteBinding.noFavouritesFound.setVisibility(View.INVISIBLE);
        favoriteBinding.noInternetView.setVisibility(View.INVISIBLE);
        favoriteBinding.moviesRecyclerView.setAdapter(favouriteAdapter);
        checkNetwork(isNetworkAvailable());
        loadFavMovies();
    }

    private void loadFavMovies() {
        movieViewModel.getFavMovies().observe(getViewLifecycleOwner(), new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(List<MovieEntry> movieEntries) {
                favouriteAdapter.setMovies(movieEntries);
                if (movieEntries == null || movieEntries.size() == 0) {
                    favoriteBinding.noFavouritesFound.setVisibility(View.VISIBLE);
                } else if (!isNetworkAvailable()) {
                    favoriteBinding.noInternetView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void checkNetwork(boolean networkAvailable) {
        if (networkAvailable) {
            favoriteBinding.moviesRecyclerView.setVisibility(View.VISIBLE);
            favoriteBinding.noInternetView.setVisibility(View.INVISIBLE);
        } else {
            favoriteBinding.noInternetView.setVisibility(View.VISIBLE);
            favoriteBinding.moviesRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Network nw = cm.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities nwcap = cm.getNetworkCapabilities(nw);
            return nwcap != null && nwcap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || nwcap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || nwcap.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
        } else {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }

    @Override
    public void FavouriteOnClick(MovieEntry movieEntry, MovieTilesBinding tilesBinding) {
        int movieId = movieEntry.getMovieId();
        String originalTitle = movieEntry.getOriginalTitle();
        String movieName = movieEntry.getTitle();
        String posterPath = movieEntry.getPosterPath();
        String overView = movieEntry.getOverview();
        String backDropPath = movieEntry.getBackdropPath();
        double voteAvg = movieEntry.getVoteAverage();
        String releaseDate = movieEntry.getReleaseDate();
        String voteCount = movieEntry.getVoteCount();

        MovieModel movieModel = new MovieModel(movieName, originalTitle, posterPath, overView, releaseDate, backDropPath
                , String.valueOf(voteAvg), String.valueOf(movieId), voteCount);
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(Constant.MOVIE_EXTRA, movieModel);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                getActivity(), (View) tilesBinding.moviePoster, tilesBinding.moviePoster.getTransitionName()).toBundle();
        startActivity(intent, bundle);
    }
}
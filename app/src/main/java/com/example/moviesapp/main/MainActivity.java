package com.example.moviesapp.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.moviesapp.R;
import com.example.moviesapp.data.MovieEntry;
import com.example.moviesapp.databinding.ActivityMainBinding;
import com.example.moviesapp.detailactivity.DetailActivity;
import com.example.moviesapp.models.MovieModel;
import com.example.moviesapp.repository.MovieRepository;
import com.example.moviesapp.settings.SettingsActivity;
import com.example.moviesapp.util.Constant;
import com.example.moviesapp.viewmodel.MovieViewModel;
import com.example.moviesapp.viewmodel.MovieViewModelFactory;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MoviePagedListAdapter.RecyclerViewClickListener,
        FavouriteAdapter.FavouriteOnClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private MoviePagedListAdapter moviePagedListAdapter;
    private FavouriteAdapter favouriteAdapter;
    private MovieViewModel movieViewModel;
    private String sortCriteria;
    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sortCriteria = prefs.getString(getString(R.string.pref_sort_by_title_value), getString(R.string.pref_sort_by_default));
        initAdapter();
        initViewModel();
        setupUI();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    private void initAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mainBinding.moviesRecyclerView.setHasFixedSize(true);
        mainBinding.moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviePagedListAdapter = new MoviePagedListAdapter(this);
        favouriteAdapter = new FavouriteAdapter(this);
    }

    private void setupUI() {
        getSupportActionBar().setTitle("Home");
        mainBinding.noFavouritesFound.setVisibility(View.INVISIBLE);
        mainBinding.noInternetView.setVisibility(View.INVISIBLE);
        mainBinding.moviesRecyclerView.setAdapter(moviePagedListAdapter);
        checkNetwork(isNetworkAvailable());
        loadMovieData();
    }

    private void initViewModel() {
        MovieRepository movieRepository = new MovieRepository(getApplication());
        movieViewModel =
                new ViewModelProvider(this, new MovieViewModelFactory(movieRepository, sortCriteria))
                        .get(MovieViewModel.class);
    }

    @Override
    public void onClick(View v, MovieModel movieModel) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Constant.MOVIE_EXTRA, movieModel);
        startActivity(intent);
    }

    private void loadMovieData() {
        movieViewModel.getMoviePagedList().observe(this, new Observer<PagedList<MovieModel>>() {
            @Override
            public void onChanged(PagedList<MovieModel> pagedList) {
                if (pagedList != null) {
                    moviePagedListAdapter.submitList(pagedList);
                    moviePagedListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void checkNetwork(boolean networkAvailable) {
        if (networkAvailable) {
            mainBinding.moviesRecyclerView.setVisibility(View.VISIBLE);
            mainBinding.noInternetView.setVisibility(View.INVISIBLE);
        } else {
            mainBinding.noInternetView.setVisibility(View.VISIBLE);
            mainBinding.moviesRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_favourites:
                movieViewModel.setFavMovies();
                mainBinding.moviesRecyclerView.setAdapter(favouriteAdapter);
                loadFavMovies();
                return true;

            case R.id.action_home:
                setupUI();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadFavMovies() {
        getSupportActionBar().setTitle("Favourites");
        movieViewModel.getFavMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(List<MovieEntry> movieEntries) {
                favouriteAdapter.setMovies(movieEntries);
                if (movieEntries == null || movieEntries.size() == 0) {
                    mainBinding.noFavouritesFound.setVisibility(View.VISIBLE);
                } else if (!isNetworkAvailable()) {
                    mainBinding.noInternetView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_by_title_value))) {
            sortCriteria = sharedPreferences.getString(key, getString(R.string.pref_sort_by_default));
        }
        movieViewModel.setCriteria(sortCriteria);
        setupUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void FavouriteOnClick(MovieEntry movieEntry) {
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
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Constant.MOVIE_EXTRA, movieModel);
        startActivity(intent);
    }

}
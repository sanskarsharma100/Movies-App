package com.example.moviesapp.main;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.moviesapp.R;
import com.example.moviesapp.databinding.FragmentHomeBinding;
import com.example.moviesapp.databinding.MovieTilesBinding;
import com.example.moviesapp.detailactivity.DetailActivity;
import com.example.moviesapp.models.MovieModel;
import com.example.moviesapp.repository.MovieRepository;
import com.example.moviesapp.settings.SettingsActivity;
import com.example.moviesapp.util.Constant;
import com.example.moviesapp.viewmodel.MovieViewModel;
import com.example.moviesapp.viewmodel.MovieViewModelFactory;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        MoviePagedListAdapter.RecyclerViewClickListener {

    private MoviePagedListAdapter moviePagedListAdapter;
    private MovieViewModel movieViewModel;
    private String sortCriteria;
    private FragmentHomeBinding mainBinding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(Constant.HOME);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View rootView = mainBinding.getRoot();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        sortCriteria = prefs.getString(getString(R.string.pref_sort_by_title_value), getString(R.string.pref_sort_by_default));
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
        setHasOptionsMenu(true);
        initAdapter();
        initViewModel();
        setupUI();
        return rootView;
    }

    private void initAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        mainBinding.moviesRecyclerView.setHasFixedSize(true);
        mainBinding.moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviePagedListAdapter = new MoviePagedListAdapter(this);
    }

    private void setupUI() {
        mainBinding.noInternetView.setVisibility(View.INVISIBLE);
        mainBinding.moviesRecyclerView.setAdapter(moviePagedListAdapter);
        checkNetwork(isNetworkAvailable());
        loadMovieData();
    }

    private void initViewModel() {
        MovieRepository movieRepository = new MovieRepository(getActivity().getApplication());
        movieViewModel =
                new ViewModelProvider(this, new MovieViewModelFactory(movieRepository, sortCriteria))
                        .get(MovieViewModel.class);
    }

    private void loadMovieData() {
        movieViewModel.getMoviePagedList().observe(getViewLifecycleOwner(), new Observer<PagedList<MovieModel>>() {
            @Override
            public void onChanged(PagedList<MovieModel> pagedList) {
                if (pagedList != null) {
                    moviePagedListAdapter.submitList(pagedList);
                    moviePagedListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_by_title_value))) {
            sortCriteria = sharedPreferences.getString(key, getString(R.string.pref_sort_by_default));
        }
        Log.d("SORTING", sortCriteria);
        movieViewModel.setCriteria(sortCriteria);
        setupUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
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
    public void onClick(MovieModel movieModel, MovieTilesBinding tilesBinding) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(Constant.MOVIE_EXTRA, movieModel);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                getActivity(), tilesBinding.moviePoster, tilesBinding.moviePoster.getTransitionName()).toBundle();
        startActivity(intent, bundle);
    }
}
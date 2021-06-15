package com.example.moviesapp.detailactivity.tabfragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviesapp.R;
import com.example.moviesapp.SimpleDividerItemDecoration;
import com.example.moviesapp.databinding.FragmentCastBinding;
import com.example.moviesapp.models.CastModel;
import com.example.moviesapp.models.CreditsModel;
import com.example.moviesapp.models.MovieDetailsModel;
import com.example.moviesapp.models.MovieModel;
import com.example.moviesapp.repository.MovieRepository;
import com.example.moviesapp.util.Constant;
import com.example.moviesapp.viewmodel.MovieDetailsViewModel;
import com.example.moviesapp.viewmodel.MovieDetailsViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CastFragment extends Fragment {

    private List<CastModel> mCastList;
    private CastAdapter castAdapter;
    private FragmentCastBinding fragmentCastBinding;
    private MovieModel mMovieModel;

    public CastFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentCastBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_cast, container, false);
        View rootView = fragmentCastBinding.getRoot();

        fragmentCastBinding.castRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        fragmentCastBinding.castRecyclerView.setLayoutManager(layoutManager);
        fragmentCastBinding.castRecyclerView.setHasFixedSize(true);
        mCastList = new ArrayList<>();
        castAdapter = new CastAdapter(mCastList);
        fragmentCastBinding.castRecyclerView.setAdapter(castAdapter);
        checkNetwork(isNetworkAvailable());
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovieModel = getMovieModel();
        setupMovieDetails(mMovieModel.getMovieId());
    }

    private void setupMovieDetails(String movieId) {
        MovieRepository movieRepository = new MovieRepository(getActivity().getApplication());
        MovieDetailsViewModel mMovieDetailsViewModel = new ViewModelProvider(this, new MovieDetailsViewModelFactory(movieRepository, movieId))
                .get(MovieDetailsViewModel.class);

        mMovieDetailsViewModel.getMovieDetails().observe(getViewLifecycleOwner(), new Observer<MovieDetailsModel>() {
            @Override
            public void onChanged(MovieDetailsModel movieDetailsModel) {
                if (movieDetailsModel != null) {
                    loadCastDetails(movieDetailsModel);
                }
            }
        });
    }

    private void loadCastDetails(MovieDetailsModel movieDetailsModel) {
        CreditsModel credits = movieDetailsModel.getMovieCredits();
        mCastList.clear();
        mCastList.addAll(credits.getCast());
        if (mCastList.isEmpty()) {
            castNotFound();
        } else {
            credits.setCast(mCastList);
            castAdapter.notifyDataSetChanged();
        }
    }

    private MovieModel getMovieModel() {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constant.MOVIE_EXTRA)) {
                mMovieModel = intent.getParcelableExtra(Constant.MOVIE_EXTRA);
            }
        }
        return mMovieModel;
    }

    private void castNotFound() {
        fragmentCastBinding.castRecyclerView.setVisibility(View.INVISIBLE);
        fragmentCastBinding.castNotAvailable.setVisibility(View.VISIBLE);
    }

    private void checkNetwork(boolean networkAvailable) {
        if (networkAvailable) {
            fragmentCastBinding.noInternetView.setVisibility(View.INVISIBLE);
            fragmentCastBinding.castRecyclerView.setVisibility(View.VISIBLE);
        } else {
            fragmentCastBinding.castRecyclerView.setVisibility(View.INVISIBLE);
            fragmentCastBinding.noInternetView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

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

}
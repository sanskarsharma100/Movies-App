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
import com.example.moviesapp.databinding.FragmentReviewBinding;
import com.example.moviesapp.models.MovieModel;
import com.example.moviesapp.models.ReviewModel;
import com.example.moviesapp.repository.MovieRepository;
import com.example.moviesapp.response.ReviewResponse;
import com.example.moviesapp.util.Constant;
import com.example.moviesapp.viewmodel.ReviewViewModel;
import com.example.moviesapp.viewmodel.ReviewViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment {

    private MovieModel movieModel;
    private List<ReviewModel> reviewList;
    private ReviewAdapter reviewAdapter;
    private FragmentReviewBinding fragmentReviewBinding;
    private ReviewViewModel reviewViewModel;

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movieModel = getMovieModel();
        setupReviewDetails(movieModel.getMovieId());
    }

    private void setupReviewDetails(String movieId) {
        MovieRepository movieRepository = new MovieRepository(getActivity().getApplication());
        reviewViewModel = new ViewModelProvider(this, new ReviewViewModelFactory(movieRepository, movieId))
                .get(ReviewViewModel.class);
        reviewViewModel.getReviewDetails().observe(getViewLifecycleOwner(), new Observer<ReviewResponse>() {
            @Override
            public void onChanged(ReviewResponse reviewResponse) {
                if (reviewResponse != null) {
                    reviewList = reviewResponse.getReviewList();
                    reviewResponse.setReviewList(reviewList);
                    if (!reviewList.isEmpty()) {
                        reviewAdapter.setReviewList(reviewList);
                        reviewAdapter.notifyDataSetChanged();
                    } else {
                        reviewsNotAvailable();
                    }
                }
            }
        });
    }

    private MovieModel getMovieModel() {

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constant.MOVIE_EXTRA)) {
                movieModel = intent.getParcelableExtra(Constant.MOVIE_EXTRA);
            }
        }
        return movieModel;
    }

    private void reviewsNotAvailable() {
        fragmentReviewBinding.reviewRecyclerview.setVisibility(View.INVISIBLE);
        fragmentReviewBinding.reviewsNotAvailable.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentReviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false);
        View rootView = fragmentReviewBinding.getRoot();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        fragmentReviewBinding.reviewRecyclerview.setLayoutManager(layoutManager);
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewList);
        fragmentReviewBinding.reviewRecyclerview.setAdapter(reviewAdapter);
        checkNetwork(isNetworkAvailable());
        return rootView;
    }

    private void checkNetwork(boolean networkAvailable) {
        if (networkAvailable) {
            fragmentReviewBinding.noInternetView.setVisibility(View.INVISIBLE);
            fragmentReviewBinding.reviewRecyclerview.setVisibility(View.VISIBLE);
        } else {
            fragmentReviewBinding.reviewRecyclerview.setVisibility(View.INVISIBLE);
            fragmentReviewBinding.noInternetView.setVisibility(View.VISIBLE);
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
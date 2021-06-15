package com.example.moviesapp.detailactivity.tabfragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
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
import com.example.moviesapp.databinding.FragmentVideoBinding;
import com.example.moviesapp.models.MovieModel;
import com.example.moviesapp.models.VideoModel;
import com.example.moviesapp.repository.MovieRepository;
import com.example.moviesapp.response.VideoResponse;
import com.example.moviesapp.util.Constant;
import com.example.moviesapp.viewmodel.VideoViewModel;
import com.example.moviesapp.viewmodel.VideoViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment implements VideoAdapter.OnVideoClickListener {

    private MovieModel movieModel;
    private List<VideoModel> videoList;
    private VideoAdapter videoAdapter;
    private VideoViewModel videoViewModel;
    private FragmentVideoBinding fragmentVideoBinding;

    public VideoFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movieModel = getMovieModel();
        setupVideoDetails(movieModel.getMovieId());
    }

    private void setupVideoDetails(String movieId) {

        MovieRepository movieRepository = new MovieRepository(getActivity().getApplication());
        videoViewModel = new ViewModelProvider(this, new VideoViewModelFactory(movieRepository, movieId))
                .get(VideoViewModel.class);

        videoViewModel.getVideoDetails().observe(getViewLifecycleOwner(), new Observer<VideoResponse>() {
            @Override
            public void onChanged(VideoResponse videoResponse) {
                if (videoResponse != null) {
                    videoList = videoResponse.getVideosList();
                    videoResponse.setVideosList(videoList);
                    if (!videoList.isEmpty()) {
                        videoAdapter.setVideosList(videoList);
                        videoAdapter.notifyDataSetChanged();
                    } else {
                        videosNotFound();
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

    private void videosNotFound() {
        fragmentVideoBinding.videoRecyclerview.setVisibility(View.INVISIBLE);
        fragmentVideoBinding.videosNotAvailable.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentVideoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);
        View rootView = fragmentVideoBinding.getRoot();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        fragmentVideoBinding.videoRecyclerview.setLayoutManager(layoutManager);
        fragmentVideoBinding.videoRecyclerview.setHasFixedSize(true);
        videoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(videoList, this);
        fragmentVideoBinding.videoRecyclerview.setAdapter(videoAdapter);
        checkNetwork(isNetworkAvailable());
        return rootView;
    }

    @Override
    public void onClick(String videoUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void checkNetwork(boolean networkAvailable) {
        if (networkAvailable) {
            fragmentVideoBinding.noInternetView.setVisibility(View.INVISIBLE);
            fragmentVideoBinding.videoRecyclerview.setVisibility(View.VISIBLE);
        } else {
            fragmentVideoBinding.videosNotAvailable.setVisibility(View.GONE);
            fragmentVideoBinding.noInternetView.setVisibility(View.VISIBLE);
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
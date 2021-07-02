package com.example.moviesapp.detailactivity.tabfragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviesapp.R;
import com.example.moviesapp.databinding.FragmentInformationBinding;
import com.example.moviesapp.models.CrewModel;
import com.example.moviesapp.models.MovieDetailsModel;
import com.example.moviesapp.models.MovieModel;
import com.example.moviesapp.repository.MovieRepository;
import com.example.moviesapp.util.Constant;
import com.example.moviesapp.util.FormatUtils;
import com.example.moviesapp.viewmodel.MovieDetailsViewModel;
import com.example.moviesapp.viewmodel.MovieDetailsViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InformationFragment extends Fragment {

    onInfoMoviesListener mCallback;
    private FragmentInformationBinding mInformationBinding;
    private MovieModel mMovieModel;
    private MovieDetailsViewModel mMovieDetailsViewModel;
    private MovieRepository movieRepository;

    public InformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovieModel = getMovieModel();
        setupMovieDetails(this.getActivity(), mMovieModel.getMovieId());
        loadDetails();

    }

    private void setupMovieDetails(Context context, String movieId) {
        movieRepository = new MovieRepository(getActivity().getApplication());
        mMovieDetailsViewModel =
                new ViewModelProvider(this, new MovieDetailsViewModelFactory(movieRepository, movieId))
                        .get(MovieDetailsViewModel.class);

        mMovieDetailsViewModel.getMovieDetails().observe(getViewLifecycleOwner(), new Observer<MovieDetailsModel>() {
            @Override
            public void onChanged(MovieDetailsModel movieDetailsModel) {
                if (movieDetailsModel != null) {
                    mCallback.onInfoMovies(movieDetailsModel);
                    loadMovieDetails(movieDetailsModel);
                }
            }
        });
    }

    private void loadMovieDetails(MovieDetailsModel detailsModel) {
        mInformationBinding.detailsOriginalMovieTitle.setText(detailsModel.getMovieOriginalTitle());
        getDirectorName(detailsModel);
        mInformationBinding.movieDetailStatus.setText(detailsModel.getMovieStatus());
        mInformationBinding.movieDetailBudget.setText(detailsModel.getMovieBudget());
        mInformationBinding.movieDetailRevenue.setText(detailsModel.getMovieRevenue());
    }

    private void getDirectorName(MovieDetailsModel detailsModel) {
        List<CrewModel> crews = detailsModel.getMovieCredits().getCrew();
        boolean directorNameNotFound = true;
        for (int i = 0; i < crews.size(); i++) {
            if (crews.get(i).getCrewJob().equals("Director")) {
                mInformationBinding.detailMovieDirectorName.setText(crews.get(i).getCrewName());
                directorNameNotFound = false;
                break;
            }
        }
        if (directorNameNotFound) {
            mInformationBinding.detailMovieDirectorName.setText(Constant.DIRECTOR_NOT_FOUND);
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

    private void loadDetails() {
        mInformationBinding.detailOverview.setText(mMovieModel.getMovieOverview());
        mInformationBinding.movieRatingValue.setText(mMovieModel.getMovieRating());
        float ratings = Float.parseFloat(mMovieModel.getMovieRating());
        mInformationBinding.reviewRatingBar.setRating(ratings);
        mInformationBinding.reviewRatingBar.setIndicator(true);
        String voters = FormatUtils.formatNumbers(Integer.parseInt(mMovieModel.getMovieVoters()));
        mInformationBinding.movieVoters.setText(voters);
        String formattedReleaseDate = FormatUtils.formatDate(mMovieModel.getMovieReleaseDate());
        mInformationBinding.detailMovieReleaseDate.setText(formattedReleaseDate);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mInformationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_information, container, false);
        return mInformationBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (onInfoMoviesListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement onInfoMoviesListener");
        }
    }

    public interface onInfoMoviesListener {
        void onInfoMovies(MovieDetailsModel movieDetailsModel);
    }
}
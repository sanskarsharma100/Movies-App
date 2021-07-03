package com.example.moviesapp.detailactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.moviesapp.AppExecutors;
import com.example.moviesapp.R;
import com.example.moviesapp.data.MovieDatabase;
import com.example.moviesapp.data.MovieEntry;
import com.example.moviesapp.databinding.ActivityDetailBinding;
import com.example.moviesapp.detailactivity.tabfragments.InformationFragment;
import com.example.moviesapp.models.GenreModel;
import com.example.moviesapp.models.MovieDetailsModel;
import com.example.moviesapp.models.MovieModel;
import com.example.moviesapp.repository.MovieRepository;
import com.example.moviesapp.util.Constant;
import com.example.moviesapp.util.FormatUtils;
import com.example.moviesapp.viewmodel.FavouriteViewModel;
import com.example.moviesapp.viewmodel.FavouriteViewModelFactory;
import com.example.moviesapp.viewmodel.MovieDetailsViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements InformationFragment.onInfoMoviesListener {

    private ActivityDetailBinding mDetailBinding;
    private MovieDetailsViewModel mMovieDetailsViewModel;
    private MovieRepository movieRepository;
    private FavouriteViewModel favouriteViewModel;
    private MovieDatabase mDb;
    private MovieEntry movieEntry;
    private boolean mIsInFavourites;
    private String mMovieId;
    private MovieModel movieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        if (getIntent().hasExtra(Constant.MOVIE_EXTRA)) {
            movieModel = getIntent().getParcelableExtra(Constant.MOVIE_EXTRA);
            mMovieId = (String) movieModel.getMovieId();
        }
        setOnFavouriteClickListener();
        mDb = MovieDatabase.getInstance(getApplicationContext());
        mIsInFavourites = isInFavourites();
        mDetailBinding.tabLayout.setupWithViewPager(mDetailBinding.viewPagerDetails.detailViewpager);
        mDetailBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        DetailPagerAdapter pagerAdapter = new DetailPagerAdapter(getSupportFragmentManager());
        mDetailBinding.viewPagerDetails.detailViewpager.setAdapter(pagerAdapter);
        setCollapsingToolbarTitle();
        loadMovieDetails();
    }

    private void loadMovieDetails() {
        showBackButton();
        Glide.with(this)
                .load(movieModel.getMovieHztImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.movie_image_not_found)
                .into(mDetailBinding.detailMovieBackImage);

        Glide.with(this)
                .load(movieModel.getMovieImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.movie_image_not_found)
                .into(mDetailBinding.detailMovieMainImage);

        mDetailBinding.detailMovieName.setText(movieModel.getMovieName());
    }

    private void showBackButton() {
        setSupportActionBar(mDetailBinding.detailToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_share) {
            shareIntent();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void shareIntent() {
        String shareMessage = getString(R.string.msg_sent_info) + movieModel.getMovieName()
                + getString(R.string.next_line) + Constant.MOVIE_SHARING_URL + movieModel.getMovieId();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(getString(R.string.share_intent_text_type));
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    private void setCollapsingToolbarTitle() {
        mDetailBinding.collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        // Set onOffsetChangedListener to determine when CollapsingToolbar is collapsed
        mDetailBinding.appBarDetailLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset < -50) {
                    // Show title when a CollapsingToolbarLayout is fully collapse
                    mDetailBinding.collapsingToolbar.setTitle(movieModel.getMovieName());
                    isShow = true;
                } else if (isShow) {
                    // Otherwise hide the title
                    mDetailBinding.collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public void setOnFavouriteClickListener() {
        mDetailBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieEntry = getMovieEntry();
                if (!mIsInFavourites) {
                    AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.movieDao().insertMovie(movieEntry);
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Movie Added to Favourites", Toast.LENGTH_SHORT).show();
                } else {
                    movieEntry = favouriteViewModel.getMovieEntry().getValue();
                    AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.movieDao().deleteMovie(movieEntry);
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Movie removed from Favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private MovieEntry getMovieEntry() {
        String runtime = mDetailBinding.detailMovieRuntime.getText().toString();
        String genre = mDetailBinding.detailMovieGenre.getText().toString();
        movieEntry = new MovieEntry(Integer.parseInt(movieModel.getMovieId()), movieModel.getOriginalMovieTitle(),
                movieModel.getMovieName(), movieModel.getMovieImage(), movieModel.getMovieOverview(),
                Double.parseDouble(movieModel.getMovieRating()), movieModel.getMovieReleaseDate(),
                movieModel.getMovieHztImage(), new Date(), runtime, genre, movieModel.getMovieVoters());
        return movieEntry;
    }

    private boolean isInFavourites() {
        movieRepository = new MovieRepository(getApplication());
        favouriteViewModel = new ViewModelProvider(this, new FavouriteViewModelFactory(movieRepository, Integer.parseInt(movieModel.getMovieId())))
                .get(FavouriteViewModel.class);
        favouriteViewModel.getMovieEntry().observe(this, new Observer<MovieEntry>() {
            @Override
            public void onChanged(MovieEntry movieEntry) {
                if (favouriteViewModel.getMovieEntry().getValue() == null) {
                    mDetailBinding.fab.setImageResource(R.drawable.favorite_border);
                    mIsInFavourites = false;
                } else {
                    mDetailBinding.fab.setImageResource(R.drawable.favorite_filled);
                    mIsInFavourites = true;
                }
            }
        });
        return mIsInFavourites;
    }

    @Override
    public void onInfoMovies(MovieDetailsModel detailsModel) {
        String formattedRuntime = FormatUtils
                .formatTime(DetailActivity.this, Integer.parseInt(detailsModel.getMovieRuntime()));
        mDetailBinding.detailMovieRuntime.setText(formattedRuntime);
        List<GenreModel> movieGenre = detailsModel.getMovieGenre();
        List<String> genreList = new ArrayList<>();
        for (int i = 0; i < movieGenre.size(); i++) {
            String genre = movieGenre.get(i).getName();
            genreList.add(genre);
        }
        String genreMovie = TextUtils.join(getString(R.string.genre_delimiter), genreList);
        mDetailBinding.detailMovieGenre.setText(genreMovie);
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

}
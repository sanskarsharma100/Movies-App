package com.example.moviesapp.main;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.moviesapp.R;
import com.example.moviesapp.data.MovieEntry;
import com.example.moviesapp.databinding.MovieTilesBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private final FavouriteOnClickListener favouriteOnClickListener;
    private List<MovieEntry> movieEntries;

    public FavouriteAdapter(FavouriteOnClickListener favouriteOnClickListener) {
        this.favouriteOnClickListener = favouriteOnClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MovieTilesBinding movieTilesBinding = DataBindingUtil
                .inflate(layoutInflater, R.layout.movie_tiles, parent, false);
        return new FavouriteViewHolder(movieTilesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FavouriteAdapter.FavouriteViewHolder holder, int position) {
        holder.tilesBinding.mainMovieTilesProgressBar.setVisibility(View.VISIBLE);
        MovieEntry movieEntry = movieEntries.get(position);
        holder.tilesBinding.movieName.setText(movieEntry.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(movieEntry.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.movie_image_not_found)
                .override(200, 300)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.tilesBinding.mainMovieTilesProgressBar.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.tilesBinding.mainMovieTilesProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.tilesBinding.moviePoster);
    }

    @Override
    public int getItemCount() {
        if (movieEntries == null) return 0;
        return movieEntries.size();
    }

    public void setMovies(List<MovieEntry> movies) {
        movieEntries = movies;
        notifyDataSetChanged();
    }

    public interface FavouriteOnClickListener {
        void FavouriteOnClick(MovieEntry movieEntry);
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MovieTilesBinding tilesBinding;

        public FavouriteViewHolder(MovieTilesBinding movieTilesBinding) {
            super(movieTilesBinding.getRoot());
            tilesBinding = movieTilesBinding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            favouriteOnClickListener.FavouriteOnClick(movieEntries.get(getAbsoluteAdapterPosition()));
        }
    }

}

package com.example.moviesapp.main;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.moviesapp.R;
import com.example.moviesapp.databinding.MovieTilesBinding;
import com.example.moviesapp.models.MovieModel;

import org.jetbrains.annotations.NotNull;

public class MoviePagedListAdapter extends PagedListAdapter<MovieModel, MoviePagedListAdapter.holder> {

    private static final DiffUtil.ItemCallback<MovieModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MovieModel>() {
                @Override
                public boolean areItemsTheSame(@NonNull @NotNull MovieModel oldItem, @NonNull @NotNull MovieModel newItem) {
                    return oldItem.getMovieId().equals(newItem.getMovieId());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull @NotNull MovieModel oldItem, @NonNull @NotNull MovieModel newItem) {
                    return oldItem.equals(newItem);
                }
            };
    public RecyclerViewClickListener listener;

    public MoviePagedListAdapter(RecyclerViewClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NotNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MovieTilesBinding movieTilesBinding = DataBindingUtil.inflate(inflater, R.layout.movie_tiles, parent, false);
        return new holder(movieTilesBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull MoviePagedListAdapter.holder holder, int position) {
        holder.movieTilesBinding.mainMovieTilesProgressBar.setVisibility(View.VISIBLE);
        holder.movieTilesBinding.movieName.setText(getItem(position).getMovieName());
        Glide.with(holder.itemView.getContext())
                .load(getItem(position).getMovieImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.movie_image_not_found)
                .placeholder(R.color.black)
                .override(200, 300)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.movieTilesBinding.mainMovieTilesProgressBar.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.movieTilesBinding.mainMovieTilesProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.movieTilesBinding.moviePoster);
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, MovieModel movieModel);
    }

    class holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MovieTilesBinding movieTilesBinding;

        public holder(MovieTilesBinding movieTilesBinding) {
            super(movieTilesBinding.getRoot());
            this.movieTilesBinding = movieTilesBinding;
            movieTilesBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MovieModel movieModel = getItem(getAbsoluteAdapterPosition());
            listener.onClick(view, movieModel);
        }
    }

}

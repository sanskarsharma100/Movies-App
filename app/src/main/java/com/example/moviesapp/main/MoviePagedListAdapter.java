package com.example.moviesapp.main;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.moviesapp.R;
import com.example.moviesapp.databinding.MovieTilesBinding;
import com.example.moviesapp.models.MovieModel;

import org.jetbrains.annotations.NotNull;

public class MoviePagedListAdapter extends PagedListAdapter<MovieModel, MoviePagedListAdapter.holder> {

    private final RecyclerViewClickListener listener;

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
        holder.bind(getItem(position));
    }

    public interface RecyclerViewClickListener {
        void onClick(MovieModel movieModel,MovieTilesBinding movieTilesBinding);
    }

    class holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final MovieTilesBinding movieTilesBinding;

        public holder(MovieTilesBinding movieTilesBinding) {
            super(movieTilesBinding.getRoot());
            this.movieTilesBinding = movieTilesBinding;
            itemView.setOnClickListener(this);
            movieTilesBinding.moviePoster.setOnClickListener(this);
        }

        void bind(MovieModel movieModel) {
            Glide.with(itemView.getContext())
                    .load(movieModel.getMovieImage())
                    .error(R.drawable.movie_image_not_found)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(movieTilesBinding.moviePoster);
            movieTilesBinding.movieName.setText(movieModel.getMovieName());
        }

        @Override
        public void onClick(View view) {
            MovieModel movieModel = getItem(getAbsoluteAdapterPosition());
            listener.onClick(movieModel,movieTilesBinding);
        }
    }

}

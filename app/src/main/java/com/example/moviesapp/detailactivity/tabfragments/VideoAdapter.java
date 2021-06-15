package com.example.moviesapp.detailactivity.tabfragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.moviesapp.R;
import com.example.moviesapp.databinding.VideoListItemBinding;
import com.example.moviesapp.models.VideoModel;
import com.example.moviesapp.util.Constant;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<VideoModel> videosList;
    private OnVideoClickListener listener;

    public VideoAdapter(List<VideoModel> videosList, OnVideoClickListener listener) {
        this.videosList = videosList;
        this.listener = listener;
    }

    public void setVideosList(List<VideoModel> videos) {
        this.videosList.addAll(videos);
    }

    @NonNull
    @NotNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        VideoListItemBinding videoListItemBinding = DataBindingUtil
                .inflate(layoutInflater, R.layout.video_list_item, parent, false);
        return new VideoViewHolder(videoListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VideoAdapter.VideoViewHolder holder, int position) {
        String thumbnailUrl = Constant.YOUTUBE_THUMBNAIL_BASE_URL
                + videosList.get(position).getVideoKey() + Constant.YOUTUBE_THUMBNAIL_TO_JPG;
        Glide.with(holder.itemView.getContext())
                .load(thumbnailUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.videoBinding.thumbnailImage);

        holder.videoBinding.videoTitle.setText(videosList.get(position).getVideoName());
    }

    @Override
    public int getItemCount() {
        if (videosList == null) return 0;
        return videosList.size();
    }

    public interface OnVideoClickListener {
        void onClick(String videoUrl);
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        VideoListItemBinding videoBinding;

        public VideoViewHolder(VideoListItemBinding videoBinding) {
            super(videoBinding.getRoot());
            this.videoBinding = videoBinding;
            videoBinding.thumbnailImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            VideoModel videoModel = videosList.get(getAbsoluteAdapterPosition());
            String videoUrl = Constant.YOUTUBE_BASE_URL + videoModel.getVideoKey();
            listener.onClick(videoUrl);
        }
    }

}

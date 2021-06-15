package com.example.moviesapp.detailactivity.tabfragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.R;
import com.example.moviesapp.databinding.ReviewListItemBinding;
import com.example.moviesapp.models.ReviewModel;
import com.example.moviesapp.util.FormatUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<ReviewModel> reviewList;

    public ReviewAdapter(List<ReviewModel> reviewList) {
        this.reviewList = reviewList;
    }

    public void setReviewList(List<ReviewModel> reviews) {
        this.reviewList.addAll(reviews);
    }

    @NonNull
    @NotNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ReviewListItemBinding reviewListItemBinding = DataBindingUtil
                .inflate(layoutInflater, R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(reviewListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReviewAdapter.ReviewViewHolder holder, int position) {
        holder.reviewBinding.authorName.setText(reviewList.get(position).getReviewAuthor());
        String updateDate = reviewList.get(position).getReviewUpdateDate().substring(0, 10);
        String formattedUpdateDate = FormatUtils.formatDate(updateDate);
        holder.reviewBinding.updatedOnDate.setText(formattedUpdateDate);
        holder.reviewBinding.reviewContent.setText(reviewList.get(position).getReviewContent());
        holder.reviewBinding.userReviewRating.setText(reviewList.get(position).getReviewAuthorDetails().getUserReviewRating());
    }

    @Override
    public int getItemCount() {
        if (reviewList == null) {
            return 0;
        }
        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        ReviewListItemBinding reviewBinding;

        public ReviewViewHolder(ReviewListItemBinding reviewBinding) {
            super(reviewBinding.getRoot());
            this.reviewBinding = reviewBinding;
        }
    }
}

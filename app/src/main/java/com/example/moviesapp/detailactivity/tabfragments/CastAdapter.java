package com.example.moviesapp.detailactivity.tabfragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesapp.R;
import com.example.moviesapp.databinding.CastListItemBinding;
import com.example.moviesapp.models.CastModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private final List<CastModel> casts;

    public CastAdapter(List<CastModel> casts) {
        this.casts = casts;
    }

    @NonNull
    @NotNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CastListItemBinding castListItemBinding = DataBindingUtil
                .inflate(layoutInflater, R.layout.cast_list_item, parent, false);
        return new CastViewHolder(castListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CastAdapter.CastViewHolder holder, int position) {
        CastModel cast = casts.get(position);
        Glide.with(holder.itemView.getContext())
                .load(cast.getCastImage())
                .error(
                        Glide.with(holder.itemView.getContext())
                                .load(R.drawable.no_profile_pic_found))
                .into(holder.castListItemBinding.castPhoto);
        holder.castListItemBinding.castName.setText(cast.getCastName());
        holder.castListItemBinding.characterName.setText(cast.getMovieCharacter());
    }

    @Override
    public int getItemCount() {
        if (casts == null) return 0;
        return casts.size();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder {
        CastListItemBinding castListItemBinding;

        public CastViewHolder(CastListItemBinding castListItemBinding) {
            super(castListItemBinding.getRoot());
            this.castListItemBinding = castListItemBinding;
        }
    }

}

package com.example.moviesapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceViewHolder;

public class CustomListPreference extends ListPreference {

    private Context mContext;

    public CustomListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CustomListPreference(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        Drawable drawable = AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.ripple_effect);
        holder.itemView.setForeground(drawable);
    }

}

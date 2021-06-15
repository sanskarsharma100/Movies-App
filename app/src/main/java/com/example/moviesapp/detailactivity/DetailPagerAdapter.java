package com.example.moviesapp.detailactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.moviesapp.detailactivity.tabfragments.CastFragment;
import com.example.moviesapp.detailactivity.tabfragments.InformationFragment;
import com.example.moviesapp.detailactivity.tabfragments.ReviewFragment;
import com.example.moviesapp.detailactivity.tabfragments.VideoFragment;
import com.example.moviesapp.util.Constant;

import org.jetbrains.annotations.NotNull;

public class DetailPagerAdapter extends FragmentPagerAdapter {

    public DetailPagerAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Constant.INFO:
                return new InformationFragment();
            case Constant.CAST:
                return new CastFragment();
            case Constant.TRAILER:
                return new VideoFragment();
            case Constant.REVIEWS:
                return new ReviewFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return Constant.TAB_LIST_SIZE;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return Constant.TAB_LIST[position % Constant.TAB_LIST_SIZE].toUpperCase();
    }
}

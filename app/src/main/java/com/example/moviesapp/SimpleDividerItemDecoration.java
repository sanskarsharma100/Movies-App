package com.example.moviesapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public SimpleDividerItemDecoration(Context context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.item_divider);
    }

    @Override
    public void onDrawOver(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() - params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}

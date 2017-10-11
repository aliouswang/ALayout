package com.aliouswang.alayout;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aliouswang on 2017/10/11.
 */

public class MyLayoutManager extends RecyclerView.LayoutManager{

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);

        int offsetY = 0;
        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);

            layoutDecoratedWithMargins(view, 0, offsetY, width, offsetY + height);
            offsetY += height;
        }

    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    int verticalScaollOffset = 0;
    int totalHeight = 0;
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int traval = dy;
        if (verticalScaollOffset + dy < 0) {
            traval = -verticalScaollOffset;
        }else if (verticalScaollOffset + dy > totalHeight - getVerticalSpace()) {
            traval = totalHeight - getVerticalSpace() - verticalScaollOffset;
        }
        verticalScaollOffset += traval;
        offsetChildrenVertical(-traval);
        return traval;
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }
}

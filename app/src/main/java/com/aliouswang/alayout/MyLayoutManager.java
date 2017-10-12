package com.aliouswang.alayout;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
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

        if (getItemCount() <= 0) return;
        if (state.isPreLayout()) {
            return;
        }

        detachAndScrapAttachedViews(recycler);

        int offsetY = 0;
        totalHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);

//            layoutDecoratedWithMargins(view, 0, offsetY, width, offsetY + height);


            totalHeight += height;

            Rect frame = allItemFrames.get(i);
            if (frame == null) {
                frame = new Rect();
            }

            frame.set(0, offsetY, width, offsetY + height);
            allItemFrames.put(i, frame);
            hasAttachedItems.put(i, false);

            offsetY += height;
        }
        totalHeight = Math.max(totalHeight, getVerticalSpace());

        recycleAndFillItems(recycler, state);
    }

    private SparseArray<Rect> allItemFrames = new SparseArray<>();
    private SparseBooleanArray hasAttachedItems = new SparseBooleanArray();

    private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout())  return;

        Rect displayFrame = new Rect(0, verticalScaollOffset,
                getHorizontalSpace(), verticalScaollOffset + getVerticalSpace());

        Rect childFrame = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childFrame.left = getDecoratedLeft(child);
            childFrame.top = getDecoratedTop(child);
            childFrame.bottom = getDecoratedBottom(child);
            childFrame.right = getDecoratedRight(child);
            if (Rect.intersects(displayFrame, childFrame)) {
                removeAndRecycleView(child, recycler);
            }
        }

        for (int i = 0; i < getItemCount(); i++) {
            if (Rect.intersects(displayFrame, allItemFrames.get(i))) {
                View scrap = recycler.getViewForPosition(i);
                measureChildWithMargins(scrap, 0, 0);
                addView(scrap);

                Rect frame = allItemFrames.get(i);
                layoutDecorated(scrap,
                        frame.left,
                        frame.top - verticalScaollOffset,
                        frame.right,
                        frame.bottom - verticalScaollOffset);
            }
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

        detachAndScrapAttachedViews(recycler);
        LogUtil.d("dy=" + dy);

        int traval = dy;
        if (verticalScaollOffset + dy < 0) {
            traval = -verticalScaollOffset;
        }else if (verticalScaollOffset + dy > totalHeight - getVerticalSpace()) {
            traval = totalHeight - getVerticalSpace() - verticalScaollOffset;
        }
        verticalScaollOffset += traval;
        offsetChildrenVertical(-traval);
        recycleAndFillItems(recycler, state);
        return traval;
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
}

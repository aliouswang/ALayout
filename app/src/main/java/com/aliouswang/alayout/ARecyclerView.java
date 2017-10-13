package com.aliouswang.alayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by aliouswang on 2017/10/13.
 */

public class ARecyclerView extends ViewGroup{

    public ARecyclerView(Context context) {
        super(context);
    }

    public ARecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ARecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    public static class RecycledViewPool {
        private static final int DEFAULT_MAX_SCRAP = 5;

        static class ScrapData {
            ArrayList<ViewHolder> mScrapHeap = new ArrayList<>();
            int mMaxScrap = DEFAULT_MAX_SCRAP;
            long mCreateRunningAverageNs = 0;
            long mBindRunningAverageNs = 0;
        }
        SparseArray<ScrapData> mScrap = new SparseArray<>();
        private int mAttachCount = 0;


    }

    public abstract static class ViewHolder {

    }

}

package com.aliouswang.alayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public final class Recycler {

        static final int DEFAULT_CACHE_SIZE = 2;

        final ArrayList<ViewHolder> mAttachedScrap = new ArrayList<>();
        ArrayList<ViewHolder> mChangedScrap = null;

        final ArrayList<ViewHolder> mCachedViews = new ArrayList<>();

        private final List<ViewHolder> mUnmodifiableAttachedScrap =
                Collections.unmodifiableList(mAttachedScrap);

        private int mRequestedCacheMax = DEFAULT_CACHE_SIZE;
        int mViewCacheMax = DEFAULT_CACHE_SIZE;
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

        public void clear() {
            for (int i = 0; i < mScrap.size(); i++) {
                ScrapData data = mScrap.valueAt(i);
                data.mScrapHeap.clear();
            }
        }
    }

    public abstract static class ViewHolder {

    }

}

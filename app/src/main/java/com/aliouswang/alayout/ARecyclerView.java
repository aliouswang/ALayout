package com.aliouswang.alayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by aliouswang on 2017/10/13.
 */

public class ARecyclerView extends ViewGroup{

    static final String TAG = "RecyclerView";

    static final boolean DEBUG = false;

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

        RecycledViewPool mRecycledViewPool;

        private ViewCacheExtension mViewCacheExtention;

        RecyclerListener mRecyclerListener;

        public void clear() {
            mAttachedScrap.clear();

        }

        void recycleAndClearCachedViews() {
            final int count = mCachedViews.size();

        }

        void recycleCachedViewAt(int cachedViewIndex) {
            if (DEBUG) {
                Log.d(TAG, "Recycling cached view at index " + cachedViewIndex);
            }
            ViewHolder viewHolder = mCachedViews.get(cachedViewIndex);
            if (DEBUG) {
                Log.d(TAG, "CachedViewHolder to be recycled : " + cachedViewIndex);
            }
            addViewHolderToRecyclerViewPool(viewHolder, true);
            mCachedViews.remove(cachedViewIndex);
        }

        void addViewHolderToRecyclerViewPool(ViewHolder viewHolder, boolean dispatchRecycled) {

        }

        void dispatchViewRecycled(ViewHolder viewHolder) {
            if (mRecyclerListener != null) {
                mRecyclerListener.onViewRecycled(viewHolder);
            }
        }
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

    public abstract static class ViewCacheExtension {

        public abstract View getViewForPositionAndType(Recycler recycler, int position, int type);

    }

    public interface RecyclerListener {
        void onViewRecycled(ViewHolder viewHolder);
    }

}

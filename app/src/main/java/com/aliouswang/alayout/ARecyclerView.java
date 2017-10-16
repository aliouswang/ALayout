package com.aliouswang.alayout;

import android.content.Context;
import android.database.Observable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by aliouswang on 2017/10/13.
 */

public class ARecyclerView extends ViewGroup{

    static final String TAG = "RecyclerView";

    static final boolean DEBUG = false;

    public static final int NO_POSITION = -1;
    public static final long NO_ID = -1;
    public static final int INVALID_TYPE = -1;

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
        public final View itemView;
        WeakReference<ARecyclerView> mNestedRecyclerView;
        int mPosition = NO_POSITION;
        int mOldPosition = NO_POSITION;
        long mItemId = NO_ID;
        int mItemViewType = INVALID_TYPE;
        int mPreLayoutPosition = NO_POSITION;

        ViewHolder mShadowedHolder = null;
        ViewHolder mShadowingHolder = null;

        static final int FLAG_BOUND = 1 << 0;
        static final int FLAG_UPDATE = 1 << 1;
        static final int FLAG_INVALID = 1 << 2;
        static final int FLAG_REMOVED = 1 << 3;
        static final int FLAG_NOT_RECYCLABLE = 1 << 4;
        static final int FLAG_RETURNED_FROM_SCRAP = 1 << 5;
        static final int FLAG_IGNORE = 1 << 7;
        static final int FLAG_TMP_DETACHED = 1 << 8;
        static final int FLAG_ADAPTER_POSITION_UNKNOWN = 1 << 9;
        static final int FLAG_ADAPTER_FULLUPDATE = 1 << 10;
        static final int FLAG_MOVED = 1 << 11;
        static final int FLAG_APPEARED_IN_PRE_LAYOUT = 1 << 12;
        static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
        static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 1 << 13;

        private int mFlags;
        private static final List<Object> FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST;

        List<Object> mPayloads = null;
        List<Object> mUnmodifiedPayloads = null;

        private int mIsRecyclableCount = 0;

        private Recycler mScrapContainer = null;
        private boolean mInChangeScrap = false;

        private int mWasImportantForAccessibilityBeforeHidden =
                ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO;

        int mPendingAccessibilityState = PENDING_ACCESSIBILITY_STATE_NOT_SET;

        Recycler mOwnerRecyclerView;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }

        void addFlags(int flags) {
            this.mFlags |= flags;
        }
    }

    public abstract static class ViewCacheExtension {

        public abstract View getViewForPositionAndType(Recycler recycler, int position, int type);

    }

    public interface RecyclerListener {
        void onViewRecycled(ViewHolder viewHolder);
    }

    static class AdapterDataObservable extends Observable<AdapterDataObserver> {
        public boolean hasObservers() {
            return !mObservers.isEmpty();
        }

        public void notifyChanged() {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount) {
            for (int i = mObservers.size() - 1; i >=0; i--) {
                mObservers.get(i).onItemRangeChanged(positionStart, itemCount);
            }
        }

        public void notifyItemRangeInserted(int positionStart, int itemCount) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeInserted(positionStart, itemCount);
            }
        }

        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeRemoved(positionStart, itemCount);
            }
        }

        public void notifyItemRangeMoved(int positionStart, int itemCount) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeMoved(positionStart, itemCount);
            }
        }
    }


    public abstract static class AdapterDataObserver {
        public void onChanged() {

        }

        public void onItemRangeChanged(int positionStart, int itemCount) {

        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {

        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {

        }

        public void onItemRangeMoved(int positionStart, int itemCount) {

        }
    }


}

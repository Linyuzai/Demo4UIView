package com.linyuzai.refreshloadmore;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/9/10 0010.
 */
public class LoadMoreRecyclerView extends RecyclerView {

    private boolean isLoadMoreEnable;

    private int mLastVisibleItem = 0;

    private boolean isLoadMoreViewVisible;
    private boolean mCanShowLoadMoreView;

    private OnScrollListener onScrollListener;
    private OnLoadMoreListener onLoadMoreListener;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setLayoutManager(new LinearLayoutManager(context));
        setItemAnimator(new DefaultItemAnimator());
        addOnScrollListener(new OnScrollListenerImpl());
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter instanceof LoadMoreRecyclerView.Adapter) {
            setAdapter((LoadMoreRecyclerView.Adapter) adapter);
        } else
            super.setAdapter(adapter);
    }

    @Override
    public LoadMoreRecyclerView.Adapter getAdapter() {
        return (Adapter) super.getAdapter();
    }

    public void setAdapter(LoadMoreRecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        setLoadMoreEnable(isLoadMoreEnable);
    }

    public OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public OnLoadMoreListener getOnLoadMoreListener() {
        return onLoadMoreListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public boolean isLoadMoreEnable() {
        return isLoadMoreEnable;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        isLoadMoreEnable = loadMoreEnable;
        if (getAdapter() != null)
            getAdapter().isLoadMoreEnable = isLoadMoreEnable;
    }

    public void showLoadMoreView() {
        if (isLoadMoreViewVisible || getAdapter() == null)
            return;
        getAdapter().showLoadMoreView();
        isLoadMoreViewVisible = true;
    }

    public void hideLoadMoreView() {
        if (!isLoadMoreViewVisible || getAdapter() == null)
            return;
        getAdapter().hideLoadMoreView();
        isLoadMoreViewVisible = false;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnScrollListener {
        void onUpScroll(int y);

        void onDownScroll(int y);
    }

    class OnScrollListenerImpl extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            /*
            judge if need load more data
             */
            if (mCanShowLoadMoreView && !isLoadMoreViewVisible && isLoadMoreEnable && onLoadMoreListener != null
                    && newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == getAdapter().getItemCount()) {
                showLoadMoreView();
                onLoadMoreListener.onLoadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            mCanShowLoadMoreView = true;
            mLastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
            /*
            judge scroll up or down
             */
            if (onScrollListener != null) {
                if (dy > 0)
                    onScrollListener.onDownScroll(dy);
                else if (dy < 0)
                    onScrollListener.onUpScroll(dy);
            }
        }
    }

    public static abstract class Adapter extends RecyclerView.Adapter {

        public static final String TAG = Adapter.class.getSimpleName();

        protected static final int TYPE_LOAD_MORE = -1;

        private boolean isLoadMoreEnable;

        private LoadMoreViewHolder mLoadMoreViewHolder;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_LOAD_MORE)
                return onCreateLoadMoreViewHolder(parent);
            else
                return onCreateItemViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (holder instanceof LoadMoreViewHolder)
                onBindLoadMoreViewHolder(mLoadMoreViewHolder = (LoadMoreViewHolder) holder);
            else if (holder instanceof ItemViewHolder) {
                onBindItemViewHolder((ItemViewHolder) holder, position);
            }
            Log.e("onBindViewHolder", mLoadMoreViewHolder + "");
        }

        @Override
        public int getItemCount() {
            return isLoadMoreEnable ? getDataItemCount() + 1 : getDataItemCount();
        }

        @Override
        public int getItemViewType(int position) {
            if (isLoadMoreEnable && position + 1 == getItemCount())
                return TYPE_LOAD_MORE;
            return getDataItemViewType(position);
        }

        private void showLoadMoreView() {
            /*if (isLoadMoreEnable && mLoadMoreViewHolder != null)
                mLoadMoreViewHolder.itemView.setVisibility(VISIBLE);*/
        }

        private void hideLoadMoreView() {
            /*if (isLoadMoreEnable && mLoadMoreViewHolder != null)
                mLoadMoreViewHolder.itemView.setVisibility(GONE);*/
            notifyItemRemoved(getItemCount());
        }

        public abstract int getDataItemCount();

        public int getDataItemViewType(int position) {
            return 0;
        }

        public abstract void onBindItemViewHolder(ItemViewHolder holder, int position);

        public abstract void onBindLoadMoreViewHolder(LoadMoreViewHolder holder);

        public abstract ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

        public abstract LoadMoreViewHolder onCreateLoadMoreViewHolder(ViewGroup parent);

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            public ItemViewHolder(View itemView) {
                super(itemView);
            }
        }

        public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

            public LoadMoreViewHolder(View itemView) {
                super(itemView);
                //itemView.setVisibility(GONE);
            }
        }
    }
}

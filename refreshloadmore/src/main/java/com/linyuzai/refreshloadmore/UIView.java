package com.linyuzai.refreshloadmore;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/11/8 0008.
 */

public class UIView extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView.OnLoadMoreListener {

    protected RecyclerView mRecyclerView;

    private OnRefreshListener onRefreshListener;

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }

    public UIView(Context context) {
        super(context);
        setupRecyclerView(context);
    }

    public UIView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupRecyclerView(context);
    }

    private void setupRecyclerView(Context context) {
        setOnRefreshListener(this);
        mRecyclerView = new LoadMoreRecyclerView(context);
        mRecyclerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ((LoadMoreRecyclerView) mRecyclerView).setOnLoadMoreListener(this);
        ((LoadMoreRecyclerView) mRecyclerView).setLoadMoreEnable(true);
        addView(mRecyclerView);
    }

    public void setAdapter(Adapter adapter) {
        ((LoadMoreRecyclerView) mRecyclerView).setAdapter(adapter);
    }

    public Adapter getAdapter() {
        return (Adapter) ((LoadMoreRecyclerView) mRecyclerView).getAdapter();
    }

    public OnRefreshListener getOnRefreshListener() {
        return onRefreshListener;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void replaceRecyclerView(RecyclerView recyclerView) {
        removeView(mRecyclerView);
        mRecyclerView = recyclerView;
        mRecyclerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        if (recyclerView instanceof LoadMoreRecyclerView)
            ((LoadMoreRecyclerView) mRecyclerView).setOnLoadMoreListener(this);
        addView(mRecyclerView);
    }

    public void finishRefresh() {
        setRefreshing(false);
    }

    public void finishLoadMore() {
        ((LoadMoreRecyclerView) mRecyclerView).hideLoadMoreView();
    }

    @Override
    public void onRefresh() {
        if (onRefreshListener != null)
            onRefreshListener.onRefresh();
    }

    @Override
    public void onLoadMore() {
        if (onRefreshListener != null)
            onRefreshListener.onLoadMore();
    }

    public static abstract class Adapter extends LoadMoreRecyclerView.Adapter {
        public static final String TAG = Adapter.class.getSimpleName();

        @Override
        public void onBindItemViewHolder(LoadMoreRecyclerView.Adapter.ItemViewHolder holder, int position) {
            onBindItemViewHolder((UIView.Adapter.ItemViewHolder) holder, position);
        }

        @Override
        public void onBindLoadMoreViewHolder(LoadMoreRecyclerView.Adapter.LoadMoreViewHolder holder) {
            onBindLoadMoreViewHolder((UIView.Adapter.LoadMoreViewHolder) holder);
        }

        public abstract void onBindItemViewHolder(UIView.Adapter.ItemViewHolder holder, int position);

        public abstract void onBindLoadMoreViewHolder(UIView.Adapter.LoadMoreViewHolder holder);

        public abstract UIView.Adapter.ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

        public abstract UIView.Adapter.LoadMoreViewHolder onCreateLoadMoreViewHolder(ViewGroup parent);

        public class ItemViewHolder extends LoadMoreRecyclerView.Adapter.ItemViewHolder {

            public ItemViewHolder(View itemView) {
                super(itemView);
            }
        }

        public class LoadMoreViewHolder extends LoadMoreRecyclerView.Adapter.LoadMoreViewHolder {

            public LoadMoreViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}

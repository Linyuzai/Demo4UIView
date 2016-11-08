package com.linyuzai.demo4uiview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linyuzai.refreshloadmore.UIView;

public class MainActivity extends AppCompatActivity {

    UIView mUiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUiView = (UIView) findViewById(R.id.ui);
        mUiView.setAdapter(new MyAdapter());
        mUiView.setOnRefreshListener(new UIView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUiView.finishRefresh();
            }

            @Override
            public void onLoadMore() {
                mUiView.finishLoadMore();
            }
        });
    }

    class MyAdapter extends UIView.Adapter {

        @Override
        public void onBindItemViewHolder(ItemViewHolder holder, int position) {

        }

        @Override
        public void onBindLoadMoreViewHolder(LoadMoreViewHolder holder) {

        }

        @Override
        public int getDataItemCount() {
            return 15;
        }

        @Override
        public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public LoadMoreViewHolder onCreateLoadMoreViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
            return new LoadMoreViewHolder(view);
        }
    }
}

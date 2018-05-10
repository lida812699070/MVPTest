package com.careagle.sdk.base.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.careagle.sdk.R;
import com.careagle.sdk.weight.EmptyView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lida on 2018/1/15.
 */

public abstract class BaseRecyclerViewActivity<T> extends BaseMVPActivity implements SwipeRefreshLayout.OnRefreshListener, EmptyView.ReloadListener, BaseQuickAdapter.RequestLoadMoreListener {
    protected int page = 0;
    private EmptyView emptyView;
    protected ArrayList<T> list;

    public abstract RecyclerView getRecyclerView();

    public abstract BaseQuickAdapter getAdapter();

    public abstract SwipeRefreshLayout getSwipeRefreshLayout();

    public EmptyView getEmptyView() {
        return emptyView;
    }

    public List<T> getList() {
        return list;
    }

    public abstract void initData();

    public abstract void initLogic();

    public int getPage() {
        return page;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initRecyclerView();
    }

    private void initRecyclerView() {
        list = new ArrayList<>();
        initLogic();
        emptyView = new EmptyView(this);
        getSwipeRefreshLayout().setColorSchemeColors(getResources().getColor(R.color.theme_color));
        getSwipeRefreshLayout().setOnRefreshListener(this);
        getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        getRecyclerView().setAdapter(getAdapter());
        getAdapter().setEmptyView(getEmptyView());
        getAdapter().setOnLoadMoreListener(this, getRecyclerView());
        getEmptyView().setLoadState(EmptyView.LoadState.LOAD_STATE_LOADING);
        getEmptyView().setReloadListener(this);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 0;
        initData();
    }

    /**
     * 加载数据
     *
     * @param totalPage 总页数
     * @param data      数据
     */
    public void setData(int totalPage, List<T> data) {
        if (page == 0) list.clear();
        list.addAll(data);
        if (page < totalPage) {
            getAdapter().loadMoreComplete();
        } else {
            getAdapter().loadMoreEnd();
        }
        getAdapter().notifyDataSetChanged();
        getSwipeRefreshLayout().setRefreshing(false);
        if (list.size() == 0) {
            getEmptyView().setLoadState(EmptyView.LoadState.LOAD_STATE_EMPTY);
        }
    }

    /**
     * 加载失败  如果第一页就失败  那就显示用布局的错误
     */
    public void showLoadError() {
        getAdapter().loadMoreFail();
        if (getSwipeRefreshLayout() != null) {
            getSwipeRefreshLayout().setRefreshing(false);
        }
        if (page > 0) {
            page--;
        }
        if (list.size() == 0) {
            getEmptyView().setLoadState(EmptyView.LoadState.LOAD_STATE_ERROR);
        }
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        initData();
    }

    /**
     * 点击空布局加载失败
     */
    @Override
    public void onReload() {
        onRefresh();
    }
}

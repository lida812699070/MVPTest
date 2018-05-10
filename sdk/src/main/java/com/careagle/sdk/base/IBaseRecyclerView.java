package com.careagle.sdk.base;


/**
 * Created by lida on 2018/1/15.
 */

public interface IBaseRecyclerView<T> extends IBaseView {
    int getPage();

    void showLoadError();
}

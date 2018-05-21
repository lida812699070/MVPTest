package com.careagle.sdk.base.activity;

import com.careagle.sdk.base.BasePresenter;
import com.careagle.sdk.base.IBaseModel;

/**
 * Created by lida on 2018/4/3.
 */

public abstract class BaseMVPActivity<P extends BasePresenter, M extends IBaseModel>
        extends BaseActivity {

    protected P presenter;
    private M model;

    protected void initData() {
        super.initData();
        presenter = (P) initPresenter();
        if (presenter != null) {
            model = (M) presenter.getModel();
            if (model != null) {
                presenter.attachMV(model, this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachMV();
        }
    }

}

package com.careagle.sdk.base;

import android.support.annotation.NonNull;

import com.careagle.sdk.RxManager;
import com.careagle.sdk.utils.JLog;

/**
 * Created by lida on 2018/4/3.
 */

public abstract class BasePresenter<M, V> {

    public M model;
    public V view;
    protected RxManager rxManager = new RxManager();

    public abstract M getModel();

    public void attachMV(@NonNull M model, @NonNull V view) {
        this.model = model;
        this.view = view;
        this.onStart();
        JLog.e("绑定");
    }

    /**
     * 解绑IModel和IView
     */
    public void detachMV() {
        JLog.e("解除绑定");
        rxManager.unSubscribe();
        view = null;
        model = null;
    }

    /**
     * IView和IModel绑定完成立即执行
     * <p>
     * 实现类实现绑定完成后的逻辑,例如数据初始化等,界面初始化, 更新等
     */
    public abstract void onStart();

}

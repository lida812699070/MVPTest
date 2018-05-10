package com.careagle.sdk.callback;


import com.careagle.sdk.utils.MyToast;

/**
 * Created by lida on 2018/4/19.
 */

public abstract class PermissionCallBack {
    public abstract void success();

    public void failed() {
        MyToast.toast("请到设置中同意相关权限");
    }
}

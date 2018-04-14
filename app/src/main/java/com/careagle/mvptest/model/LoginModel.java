package com.careagle.mvptest.model;

import android.support.annotation.NonNull;

import com.careagle.mvptest.api.UserApi;
import com.careagle.mvptest.contract.LoginContract;
import com.careagle.mvptest.entity.Result;
import com.careagle.sdk.base.BaseModel;
import com.careagle.sdk.helper.RetrofitCreateHelper;
import com.careagle.sdk.helper.RxHelper;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by lida on 2018/4/4.
 */

public class LoginModel extends BaseModel implements LoginContract.Model {

    @NonNull
    public static LoginModel newInstance() {
        return new LoginModel();
    }

    @Override
    public Observable<Result> goLogin(Map<String, String> headers, Map<String, String> map) {
        return RetrofitCreateHelper
                .createApi(UserApi.class)
                .goLogin(headers,map)
                .compose(RxHelper.<Result>rxSchedulerHelper());
    }
}

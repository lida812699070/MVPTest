package com.careagle.mvptest.contract;

import android.content.Context;

import com.careagle.mvptest.entity.Result;
import com.careagle.sdk.base.BasePresenter;
import com.careagle.sdk.base.IBaseModel;
import com.careagle.sdk.base.IBaseView;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.HeaderMap;

/**
 * Created by lida on 2018/4/4.
 */

public interface LoginContract {
    interface Model extends IBaseModel {
        /**
         * 登录
         */
        Observable<Result> goLogin(@HeaderMap Map<String, String> headers, @FieldMap Map<String, String> map);

    }

    interface View extends IBaseView {

        String getUsername();

        String getPassword();
    }

    abstract class Presenter extends BasePresenter<Model, View> {

        public abstract void goLogin(Context context);
    }
}

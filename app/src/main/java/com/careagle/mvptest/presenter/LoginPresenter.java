package com.careagle.mvptest.presenter;

import com.careagle.mvptest.contract.LoginContract;
import com.careagle.mvptest.entity.Result;
import com.careagle.mvptest.global.G;
import com.careagle.mvptest.model.LoginModel;
import com.careagle.sdk.utils.MyToast;

import java.util.HashMap;

import io.reactivex.functions.Consumer;

/**
 * Created by lida on 2018/4/4.
 */

public class LoginPresenter extends LoginContract.Presenter {

    public static LoginPresenter newInstance() {
        return new LoginPresenter();
    }

    @Override
    public void goLogin() {
        view.showProgress("正在登录");
        String username = view.getUsername();
        String password = view.getPassword();
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        rxManager.register(model.goLogin(G.getHeader(), map)
                .compose(view.bindLifecycle())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        if (view == null) return;
                        if (result.getCode() == 200) {
                            MyToast.toast("登录成功");
                        } else {
                            MyToast.toast(result.getMessage());
                        }
                        view.hideProgress();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (view == null) return;
                        MyToast.toast( "登录失败");
                        view.hideProgress();
                    }
                }
        ));
    }


    @Override
    public LoginContract.Model getModel() {
        return LoginModel.newInstance();
    }

    @Override
    public void onStart() {

    }
}

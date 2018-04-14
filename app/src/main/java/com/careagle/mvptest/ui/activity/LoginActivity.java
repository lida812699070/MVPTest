package com.careagle.mvptest.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.careagle.mvptest.R;
import com.careagle.mvptest.contract.LoginContract;
import com.careagle.mvptest.model.LoginModel;
import com.careagle.mvptest.presenter.LoginPresenter;
import com.careagle.sdk.base.BasePresenter;
import com.careagle.sdk.base.activity.BaseMVPActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseMVPActivity<LoginContract.Presenter, LoginContract.Model> implements LoginContract.View {


    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                presenter.goLogin(this);
                break;
        }
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return LoginPresenter.newInstance();
    }

    @Override
    public String getUsername() {
        return username.getText().toString();
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }
}

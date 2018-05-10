package com.careagle.sdk.base.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careagle.sdk.AppManager;
import com.careagle.sdk.R;
import com.careagle.sdk.base.IBaseView;
import com.careagle.sdk.callback.MyDialogCallback;
import com.careagle.sdk.callback.MyTakePhotoCallBack;
import com.careagle.sdk.callback.PermissionCallBack;
import com.careagle.sdk.utils.MyToast;
import com.careagle.sdk.utils.RxPermissionsUtil;
import com.careagle.sdk.utils.StateBarUtils;
import com.careagle.sdk.weight.CustomProgress;

import butterknife.ButterKnife;

/**
 * Created by lida on 2018/4/3.
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

    private CustomProgress progressDialog;
    private AlertDialog selectDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //简书类似效果的状态栏
        if (isWhiteStatusBar() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.setStatusBarColor(Color.TRANSPARENT);
            //白底黑色
            StateBarUtils.statusBarLightMode(this);
        } else if (isWhiteStatusBar() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            setStatusBarColor(Color.TRANSPARENT);
            decorView.setSystemUiVisibility(option);
        }
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        initData();
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected void initData() {
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorId
     */
    protected void setStatusBarColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            if (!isTransparent()) {
                window.setStatusBarColor(colorId);
            }
        }
    }

    public boolean isTransparent() {
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_finish_trans_in, R.anim
                .activity_finish_trans_out);
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        super.startActivity(intent, options);
        overridePendingTransition(R.anim.activity_start_zoom_in, R.anim
                .activity_start_zoom_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.activity_start_zoom_in, R.anim
                .activity_start_zoom_out);
    }

    @Override
    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        MyToast.toast(msg);
    }

    /**
     * @param isNormal 是否是正常状态栏
     *                 true     黑底白色状态栏文字
     *                 false    白底黑色文字
     */
    public void changeStatusBarNormal(boolean isNormal) {
        if (isNormal) {
            StateBarUtils.normalLightMode(this);
        } else {
            StateBarUtils.statusBarLightMode(this);
        }
    }


    protected void initToolbar(String title) {
        initToolbar(title, null);
    }

    /**
     * toolbar     标题
     * subTitle    副标题
     *
     * @param title 标题
     */
    protected void initToolbar(String title, String subTitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        if (toolbar == null) {
            return;
        }
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.tv_title);
        toolbarTitle.setText(title);
        if (!TextUtils.isEmpty(subTitle)) {
            TextView tvToolbarSubtitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_subtitle);
            tvToolbarSubtitle.setText(subTitle);
            tvToolbarSubtitle.setVisibility(View.VISIBLE);
        }
        ImageView ivBack = (ImageView) toolbar.findViewById(R.id.iv_back);
        ivBack.setVisibility(View.VISIBLE);

    }

    @Override
    public void showProgress(String message) {
        showProgress(message, false);
    }

    public void showProgress(String msg, boolean cancelable) {
        if (TextUtils.isEmpty(msg)) return;
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = CustomProgress.show(this, msg, cancelable, null);
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
                progressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isWhiteStatusBar() {
        return true;
    }

    public abstract int getLayoutId();

    public void showMyDialog(String msg, final MyDialogCallback callback) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.onClickOk(dialogInterface, i);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public void showSelectDialog(final MyTakePhotoCallBack callBack) {
        RxPermissionsUtil.requestPermission(this, new PermissionCallBack() {
                    @Override
                    public void success() {
                        toShowSelectDialog(callBack);
                    }
                }, Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void toShowSelectDialog(final MyTakePhotoCallBack callBack) {
        if (selectDialog == null) {
            View.OnClickListener selectDialogListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = view.getId();
                    if (i == R.id.tv_cancel) {
                        selectDialog.dismiss();

                    } else if (i == R.id.tv_select_photo) {
                        callBack.selectPhoto();
                        selectDialog.dismiss();

                    } else if (i == R.id.tv_take_photo) {
                        callBack.takePhoto();
                        selectDialog.dismiss();

                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_take_photo, null);
            dialogView.findViewById(R.id.tv_take_photo).setOnClickListener(selectDialogListener);
            dialogView.findViewById(R.id.tv_select_photo).setOnClickListener(selectDialogListener);
            dialogView.findViewById(R.id.tv_cancel).setOnClickListener(selectDialogListener);
            builder.setView(dialogView);
            selectDialog = builder.create();
            selectDialog.setCancelable(true);
            selectDialog.show();
            //此处设置位置窗体大小
            Window window = selectDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.MyDialogAnim);
            window.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_radio_bg));
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            window.setLayout((int) (wm.getDefaultDisplay().getWidth() * 0.9), LinearLayout.LayoutParams.WRAP_CONTENT);
        } else {
            selectDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (selectDialog != null && selectDialog.isShowing()) {
            selectDialog.dismiss();
            selectDialog = null;
        }
    }
}

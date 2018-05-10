package com.careagle.sdk.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;

import com.careagle.sdk.callback.PermissionCallBack;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Field;

import io.reactivex.functions.Consumer;

/**
 * Created by lida on 2018/4/19.
 */

public class RxPermissionsUtil {
    /**
     * 是否有相机权限的申请
     */
    private static boolean hasCamera;

    @SuppressLint("NewApi")
    public static void requestPermission(Activity activity, final PermissionCallBack callBack, String... strPermissions) {
        if (activity == null || activity.isDestroyed()) {
            callBack.failed();
            return;
        }
        hasCamera = false;
        for (String strPermission : strPermissions) {
            if (strPermission.equals(Manifest.permission.CAMERA)) {
                hasCamera = true;
                break;
            }
        }
        new RxPermissions(activity)
                .request(strPermissions)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) {
                        if (hasCamera && Build.VERSION.SDK_INT < 23) {
                            aBoolean = hasCameraPermission();
                        }
                        if (aBoolean) {
                            callBack.success();
                        } else {
                            callBack.failed();
                        }
                    }
                });

    }

    private static boolean hasCameraPermission() {
        boolean hahPermission = true;
        try {
            Camera camera = Camera.open();
            hahPermission = checkVivoCamera(camera);
            camera.getParameters();//适配部分魅族手机
            camera.release();
            camera = null;
        } catch (Exception e) {
            e.printStackTrace();
            hahPermission = false;
        }
        return hahPermission;
    }

    /**
     * 适配部分Vivo手机
     *
     * @param camera
     * @return
     */
    private static boolean checkVivoCamera(Camera camera) {
        Field fieldPassword = null;
        try {
            fieldPassword = camera.getClass().getDeclaredField("mHasPermission");
            fieldPassword.setAccessible(true);
            return (boolean) fieldPassword.get(camera);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

    }
}

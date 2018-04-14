package com.careagle.sdk.utils;

import android.hardware.Camera;

/**
 * Created by lida on 2018/4/6.
 */

public class CameraPermissionUtils {
    public static boolean checkCameraPermission() {
        try {
            Camera camera = Camera.open();
            //针对魅族手机
            camera.getParameters();
            camera.release();
            camera = null;
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

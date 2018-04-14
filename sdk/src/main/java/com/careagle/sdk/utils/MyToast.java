//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.careagle.sdk.utils;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.careagle.sdk.Config;
import com.careagle.sdk.R;


public class MyToast {
    private static Toast toast = null;
    private static MyToast.ToastParams params;
    private static final int errorIconResId = 0;
    private static final int successIconResId = 0;
    private static final int defaultIconResId = 0;

    public MyToast() {
    }

    public static void toast(String content) {
        MyToast.ToastParams params = getParams().reset();
        params.content = content;
        toast(params);
    }

    public static void toast(String content, int iconResId, int duration) {
        MyToast.ToastParams params = getParams().reset();
        params.iconResId = iconResId;
        params.content = content;
        toast(params);
    }

    public static void toastError(String content) {
        toast(content, 0, 0);
    }

    public static void toastError(int textResId) {
        toast(Config.getContext().getString(textResId), 0, 0);
    }

    public static void toastSuccess(String content) {
        toast(content, 0, 0);
    }

    public static void toastSuccess(int resId) {
        toast(Config.getContext().getString(resId), 0, 0);
    }

    @SuppressLint({"InflateParams"})
    public static void toast(MyToast.ToastParams params) {
        View layout = LayoutInflater.from(Config.getContext()).inflate(R.layout.layout_my_toast, (ViewGroup) null);
        ImageView imageView = (ImageView) layout.findViewById(R.id.icon_imageview);
        TextView textView = (TextView) layout.findViewById(R.id.message_textview);
        textView.setText(params.content);
        if (params.iconResId > 0) {
            imageView.setImageResource(params.iconResId);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        makeToast(params, layout).show();
    }

    private static MyToast.ToastParams getParams() {
        if (params == null) {
            Class var0 = MyToast.ToastParams.class;
            synchronized (MyToast.ToastParams.class) {
                if (params == null) {
                    params = new MyToast.ToastParams();
                }
            }
        }

        return params;
    }

    private static Toast makeToast(MyToast.ToastParams params, View view) {
        if (toast == null) {
            Class var2 = Toast.class;
            synchronized (Toast.class) {
                if (toast == null) {
                    toast = new Toast(Config.getContext());
                }
            }
        }

        toast.setGravity(params.gravity, params.xOffset, params.yOffset);
        toast.setDuration(params.duration);
        toast.setView(view);
        return toast;
    }

    public static class ToastParams {
        public String content;
        public int gravity;
        public int xOffset;
        public int yOffset;
        public int duration;
        public int iconResId;

        public ToastParams() {
            this.reset();
        }

        public MyToast.ToastParams reset() {
            this.gravity = 17;
            this.xOffset = 0;
            this.yOffset = 0;
            this.content = null;
            this.duration = 0;
            this.iconResId = 0;
            return this;
        }
    }
}

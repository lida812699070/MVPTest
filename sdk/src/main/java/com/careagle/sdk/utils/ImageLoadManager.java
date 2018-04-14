package com.careagle.sdk.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by lida on 2018/4/7.
 */

public class ImageLoadManager {

    public static void load(File file, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(file)
                .into(imageView);
    }
}

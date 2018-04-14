package com.careagle.mvptest.global;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.careagle.sdk.BuildConfig;
import com.careagle.sdk.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lida on 2018/4/4.
 */

public class G {
    public static final String DEVELOPE_VERSION = "www.careagle.net";
    public static String FILE_PROVIDER_AUTHORITY = null;

    public static Map<String, String> getHeader(Context context) {
        HashMap<String, String> header = new HashMap<>();
        String token = (String) SPUtils.get(context, "token", "");
        if (!TextUtils.isEmpty(token)) {
            header.put("X-Auth-Token", token);
        }
        header.put("AGENT", "X-Android");
        header.put("APP-VERSION", BuildConfig.VERSION_NAME);
        header.put("DEVICE", Build.MODEL + " " + android.os.Build.MANUFACTURER);
        header.put("SYSTEM-VERSION", android.os.Build.VERSION.RELEASE);
        return header;
    }
}


package com.careagle.sdk.utils;

import android.text.TextUtils;
import android.util.Log;

import com.careagle.sdk.Config;


/**
 * Created by lida on 2017/11/27.
 */

public class JLog {
    public static void e(String msg) {
        if (Config.DEBUG) {
            if (TextUtils.isEmpty(msg)) return;
            Log.e("jlog", msg);
        }
    }

    public static void e(String tag, String msg) {
        if (Config.DEBUG) {
            if (TextUtils.isEmpty(msg)) return;
            Log.e(tag, msg);
        }
    }

    public static void d(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        if (Config.DEBUG) {
            Log.d("http", msg);
        }
    }

    public static void i(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        if (Config.DEBUG) {
            Log.i("http", msg);
        }
    }
}

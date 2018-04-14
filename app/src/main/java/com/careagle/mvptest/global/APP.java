package com.careagle.mvptest.global;

import android.app.Application;

import com.careagle.sdk.Config;

/**
 * Created by lida on 2018/4/2.
 */

public class APP extends Application {

    public static APP newInstance;

    public static APP newInstance() {
        return newInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        newInstance = this;
        Config.setApp(newInstance);
        G.FILE_PROVIDER_AUTHORITY = getPackageName() + ".fileProvider";
    }
}

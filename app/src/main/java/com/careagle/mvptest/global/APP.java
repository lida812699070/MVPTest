package com.careagle.mvptest.global;

import android.app.Application;

import com.careagle.sdk.Config;
import com.careagle.sdk.utils.JLog;
import com.tencent.smtt.sdk.QbSdk;

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

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                JLog.e("加载完成");
            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        };

        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}

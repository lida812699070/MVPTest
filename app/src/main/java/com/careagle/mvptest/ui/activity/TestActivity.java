package com.careagle.mvptest.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.careagle.mvptest.R;
import com.careagle.mvptest.utils.X5WebView;
import com.tencent.smtt.sdk.TbsVideo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    X5WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TbsVideo.canUseTbsPlayer(this);
        ButterKnife.bind(this);
        webView.loadUrl("http://p57wy28ou.bkt.clouddn.com/lmu7wQXjWc20Z_8fL26Dn1L-_fjX");

    }
}

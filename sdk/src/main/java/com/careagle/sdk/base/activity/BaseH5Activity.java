package com.careagle.sdk.base.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.careagle.sdk.weight.MyWebViewClient;


/**
 * Created by lida on 2018/2/26.
 */

public abstract class BaseH5Activity extends BaseMVPActivity {

    protected String url;
    private String title;
    private String subTitle;

    protected abstract WebView getWebView();

    protected abstract ProgressBar getProgressBar();

    @Override
    protected void initView(Bundle savedInstanceState) {
        initWebView();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        subTitle = intent.getStringExtra("subTitle");
        initToolbar(title, subTitle);
        if (TextUtils.isEmpty(url)) {
            showMessage("暂无权限查看！");
            finish();
            return;
        }
        init();
        getWebView().loadUrl(url);
    }

    protected abstract void init();

    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        getWebView().getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getWebView().getSettings().setJavaScriptEnabled(true);
        getWebView().addJavascriptInterface(this, "syc");//wx对象就是java传到js的对象  js可以用window.wx.xxxmethod()调用java的方法
        getWebView().getSettings().setUseWideViewPort(true);
        getWebView().getSettings().setBuiltInZoomControls(true);
        getWebView().getSettings().setSupportZoom(true); // 支持缩放
        getWebView().getSettings().setDisplayZoomControls(false);
        // 自适应屏幕
        getWebView().getSettings().setDefaultTextEncodingName("UTF-8");
        getWebView().getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        getWebView().getSettings().setLoadWithOverviewMode(true);

        getWebView().setWebViewClient(new MyWebViewClient());
        getWebView().setWebChromeClient(new MyWebChromeClient());
//        getWebView().setDownloadListener(this);
        getWebView().setHorizontalScrollBarEnabled(true);
        getWebView().setVerticalFadingEdgeEnabled(false);
        getWebView().setVerticalScrollBarEnabled(false);
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (getProgressBar() != null) {
                getProgressBar().setProgress(newProgress);
                if (newProgress >= 99) {
                    getProgressBar().setVisibility(View.GONE);
                }
            }
        }
    }

//    //获取token
//    @JavascriptInterface
//    public String getToken() {
//        String token = (String) SPUtils.get(getApplicationContext(), "token", "");
//        return token;
//    }

}

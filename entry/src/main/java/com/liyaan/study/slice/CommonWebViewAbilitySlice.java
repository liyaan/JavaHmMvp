package com.liyaan.study.slice;

import com.example.utils.base.BaseAbilitySlice;
import com.example.utils.component.decoration.Utils;
import com.liyaan.study.ResourceTable;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ProgressBar;
import ohos.agp.components.webengine.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;

import static com.liyaan.study.common.Consts.WEB_URL;

public class CommonWebViewAbilitySlice extends BaseAbilitySlice {
    private WebView mWebView;
    private static final String JS_NAME = "JsCallJava";
    private String url;

    private ProgressBar mProgressBar;
    @Override
    public int getLayoutId() {
        return ResourceTable.Layout_ability_webview;
    }

    @Override
    public void initView() {
        mWebView = (WebView) findComponentById(ResourceTable.Id_web_view);
        mProgressBar = (ProgressBar) findComponentById(ResourceTable.Id_progressbar);
    }

    @Override
    public void onStartIntent(Intent intent) {
        url = (String) intent.getParams().getParam(WEB_URL);
        Utils.info("url="+url);
    }

    @Override
    public void initOnActive() {
//        mProgressBar.setProgressValue(50);
        if (mWebView==null){
            Utils.info("aaaaaaaaaaaaaaa");
            return;
        }
        WebConfig webConfig = mWebView.getWebConfig();
        webConfig.setDataAbilityPermit(true);  //这个要加上，设置 webview 支持打开本地文件
        // 是否支持Javascript，默认值false
        webConfig.setJavaScriptPermit(true);
        webConfig.setLoadsImagesPermit(true);
        webConfig.setWebStoragePermit(true);
        webConfig.setLocationPermit(true);
        webConfig.setMediaAutoReplay(true);
        mWebView.setWebAgent(new WebAgent() {
            @Override
            public boolean isNeedLoadUrl(WebView webView, ResourceRequest request) {
                if (request == null || request.getRequestUrl() == null) {
                    return false;
                }
                String url = request.getRequestUrl().toString();
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    webView.load(url);
                    return false;
                } else {
                    return super.isNeedLoadUrl(webView, request);
                }
            }
        });

        mWebView.setBrowserAgent(new BrowserAgent(this) {
            @Override
            public boolean onJsMessageShow(WebView webView, String url, String message, boolean isAlert, JsMessageResult result) {
                if (isAlert) {
                    new ToastDialog(getApplicationContext()).setText(message).setAlignment(LayoutAlignment.CENTER).show();
                    result.confirm();
                    return true;
                } else {
                    return super.onJsMessageShow(webView, url, message, isAlert, result);
                }
            }

            @Override
            public void onProgressUpdated(WebView webView, int newValue) {
                super.onProgressUpdated(webView, newValue);
                mProgressBar.setProgressValue(newValue);
                if (newValue>=99){
                    mProgressBar.setVisibility(Component.HIDE);
                }
            }
        });

        // 配置JS发来的消息处理
        mWebView.addJsCallback(JS_NAME, str -> {
            // 处理接收到的Js发送来的消息
            new ToastDialog(this).setText(str).setAlignment(LayoutAlignment.CENTER).show();

            // 返回给Js
            return "Js Call Java Success";
        });
        mWebView.load(url);
    }

    @Override
    public void onBaseStop() {
        Utils.info("onBaseStop");
        mWebView.getWebConfig().setJavaScriptPermit(false);
        mWebView.removeJsCallback(JS_NAME);
        mWebView.load("");
        mWebView = null;
    }

    @Override
    protected void onBackPressed() {
        Utils.info("onBackPressed");
        terminate();
    }
}
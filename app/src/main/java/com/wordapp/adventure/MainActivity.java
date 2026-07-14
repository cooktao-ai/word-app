package com.wordapp.adventure;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.PermissionRequest;

/**
 * 单词大冒险 - WebView 壳
 * 加载 assets/www/index.html，提供全屏 WebView 容器
 */
public class MainActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 全屏沉浸式
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        webView = new WebView(this);
        setContentView(webView);

        // 配置 WebView
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);           // 启用 JS
        settings.setDomStorageEnabled(true);            // 启用 localStorage（保存学习进度）
        settings.setDatabaseEnabled(true);              // 启用数据库
        settings.setAllowFileAccess(true);              // 允许访问本地文件
        settings.setAllowContentAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false); // 允许自动播放语音（TTS）
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(false);                 // 禁止缩放（已做移动端适配）
        settings.setBuiltInZoomControls(false);
        settings.setTextZoom(100);                      // 固定字体大小，不被系统设置影响

        // 在 WebView 内部打开链接（不调外部浏览器）
        webView.setWebViewClient(new WebViewClient());
        // 允许 JS 弹窗/alert/confirm
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                // 自动授权（语音等）
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        request.grant(request.getResources());
                    }
                });
            }
        });

        // 加载本地网页
        webView.loadUrl("file:///android_asset/www/index.html");
    }

    // 返回键：先退网页历史，再退 App
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 暂停 WebView 中的 JS 执行，省电
        if (webView != null) webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}

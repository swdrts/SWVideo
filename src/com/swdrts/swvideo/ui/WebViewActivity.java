
package com.swdrts.swvideo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.swdrts.swvideo.Constant;
import com.swdrts.swvideo.R;
import com.swdrts.swvideo.utils.SLog;

public class WebViewActivity extends Activity {

    private Context mContext;
    private WebView mWebView;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.webview);
        initIntent(getIntent());
        openWebView();

    }

    private void initIntent(Intent intent) {
        if (null != intent) {
            if (!TextUtils.isEmpty(intent.getStringExtra(Constant.WEB_URL))) {
                mUrl = intent.getStringExtra(Constant.WEB_URL);
            }
        }
    }

    private void openWebView() {
        if (TextUtils.isEmpty(mUrl)) {
            SLog.error("mUrl is empty");
            return;
        }
        SLog.debug("mUrl-->" + mUrl);

        mWebView.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本  
        mWebView.getSettings().setBuiltInZoomControls(true);//设置使支持缩放
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                SLog.debug("page override-->"+url);
                if (url.contains("v.youku.com/v_show/")) {
                    Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constant.VIDEO_PATH, url);
                    intent.putExtra(Constant.TYPE_VIDEO, Constant.TYPE_YOUKU);
                    mContext.startActivity(intent);
                    return true;
                }
                view.loadUrl(url);// 使用当前WebView处理跳转
                return true;// true表示此事件在此处被处理，不需要再广播
            }

            @Override
            // 转向错误时的处理
            public void onReceivedError(WebView view, int errorCode,
                    String description, String failingUrl) {
                // TODO Auto-generated method stub
                Toast.makeText(mContext, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                SLog.debug("page start-->"+url);
                super.onPageStarted(view, url, favicon);
            }
            
        };
        mWebView.setWebViewClient(webViewClient);
        mWebView.loadUrl(mUrl);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}

package com.suntown.cloudmonitoring.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends BaseActivity {

    private static final String TAG = "WebActivity";
    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra(Constant.WEB_URL);
        Log.i(TAG, "url-" + url);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setTextSize(WebSettings.TextSize.SMALLEST);
        webView.loadUrl(url);

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode==KeyEvent.KEYCODE_BACK)
//        {
//            if(webView.canGoBack())
//            {
//                webView.goBack();//返回上一页面
//                return true;
//            }
//            else
//            {
//                System.exit(0);//退出程序
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}

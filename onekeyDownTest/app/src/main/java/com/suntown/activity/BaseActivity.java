package com.suntown.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.suntown.R;
import com.suntown.net.BaseRequest;
import com.suntown.net.BaseResponse;
import com.suntown.net.Callback;
import com.suntown.net.NetUtil;
import com.suntown.net.OnSuccessCallback;

import okhttp3.OkHttpClient;

public abstract class BaseActivity extends Activity implements Callback {
    private OnSuccessCallback mOnSuccessCallback;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        okHttpClient = new OkHttpClient();
    }
    //由父类发出请求在通知子类，错误由父类处理
    public void sendRequest(BaseRequest request, Class<? extends BaseResponse> responseClass, OnSuccessCallback onSuccessCallback) {

        mOnSuccessCallback = onSuccessCallback;
        NetUtil.sendRequest(request, responseClass, this);
    }

    @Override
    public void onError(BaseRequest request, Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOther(BaseRequest request, BaseResponse response) {
        Toast.makeText(this, "其它错误", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccess(BaseRequest request, BaseResponse response) {
        if (response == null || response.data == null) {
            
        } else {
            mOnSuccessCallback.onResultSuccess(request, response);
        }
    }
}

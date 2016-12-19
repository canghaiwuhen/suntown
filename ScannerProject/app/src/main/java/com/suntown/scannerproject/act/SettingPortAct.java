package com.suntown.scannerproject.act;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.suntown.scannerproject.R;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.Utils;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingPortAct extends BaseActivity {

    @BindView(R.id.tv_now_port)
    TextView tvNowPort;
    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.et_port)
    EditText etPort;
    private InputMethodManager systemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_port);
        ButterKnife.bind(this);
        systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        String serverIp = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverIp)){
            serverIp= ApiConstant.BASE_URL;
        }
        tvNowPort.setText(serverIp);
    }


    @OnClick({R.id.iv_back, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                String server;
                String url = etUrl.getText().toString().trim();
                String port = etPort.getText().toString().trim();
                if ("".equals(url)){
                    Utils.showToast(this,"请输入IP地址");
                    return;
                }
                if (!"".equals(port)){
                    server=url+":"+port;
                }else{
                    server=url;
                }
//                Pattern exp = Pattern.compile("^http://[\\w\\.\\-]+(?:/|(?:/[\\w\\.\\-]+)*)?$", Pattern.CASE_INSENSITIVE);
//                boolean ismatches = exp.matcher(server).matches();
//                if (!ismatches) {
//                    Utils.showToast(this,"IP地址格式错误，请确认");
//                    return;
//                }
                SPUtils.put(this,Constant.SUBSERVER_IP,server);
                finish();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                systemService.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
}

package com.suntowns.labeltest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.suntowns.labeltest.utils.Constant;
import com.suntowns.labeltest.utils.SPUtils;
import com.suntowns.labeltest.utils.Utils;


public class NetWifiActivity extends Activity implements View.OnClickListener {

    private ImageView iv_back;
    private boolean mIsConncting = false;
    protected Handler mViewHandler = new Handler();
    private EditText etWifiPsw;
    private String ssid;
    private EditText etWifiNum;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_wifi);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
        registerReceiver(mWifiChangedReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void init() {
        iv_back = ((ImageView) findViewById(R.id.iv_back));
        etWifiNum = ((EditText) findViewById(R.id.et_wifi_num));
        etWifiPsw = ((EditText) findViewById(R.id.et_wifi_psw));
        btnSearch = ((Button) findViewById(R.id.btn_search));
        btnSearch.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        String psw = SPUtils.getString(this, Constant.WIFI_PASSWORD);
        Log.i("NetWifiActivity", psw);
        if (!psw.equals("")) {
            Log.i("NetWifiActivity", psw);
            etWifiPsw.setText(psw);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mWifiChangedReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_search:
                String wifiNum = etWifiNum.getText().toString().trim();
                String wifiPsw = etWifiPsw.getText().toString().trim();
                if (wifiNum.equals("") || wifiPsw.equals("")) {
                    Utils.showToast(NetWifiActivity.this, "请输入wifi密码");
                    return;
                }
                SPUtils.put(this, Constant.WIFI_SSID, wifiNum);
                SPUtils.put(this, Constant.WIFI_PASSWORD, wifiPsw);
                startActivity(new Intent(NetWifiActivity.this, ConfigActivity.class));
                break;
        }
    }

    private String getSsid() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        if (wm != null) {
            WifiInfo wi = wm.getConnectionInfo();
            if (wi != null) {
                String ssid = wi.getSSID();
                String bssid = wi.getBSSID();
                SPUtils.put(NetWifiActivity.this,Constant.BSSID,bssid);
                Log.i("WIFI",bssid);
                if (ssid.length() > 2 && ssid.startsWith("\"") && ssid.endsWith("\"")) {
                    return ssid.substring(1, ssid.length() - 1);
                } else {
                    return ssid;
                }
            }
        }
        return "";
    }

    BroadcastReceiver mWifiChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null && networkInfo.isConnected()) {
                etWifiNum.setText(getSsid());
                SPUtils.put(NetWifiActivity.this,Constant.WIFI_SSID,ssid);
                etWifiPsw.requestFocus();
                btnSearch.setEnabled(true);
            } else {
                etWifiNum.setText(getString(R.string.hiflying_smartlinker_no_wifi_connectivity));
                etWifiPsw.requestFocus();
                btnSearch.setEnabled(false);
            }
        }
    };

}

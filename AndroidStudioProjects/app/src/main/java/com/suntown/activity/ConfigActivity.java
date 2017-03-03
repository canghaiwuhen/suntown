package com.suntown.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.hiflying.smartlink.OnSmartLinkListener;
import com.hiflying.smartlink.SmartLinkedModule;
import com.hiflying.smartlink.v3.SnifferSmartLinker;
import com.suntown.R;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigActivity extends BaseActivity implements OnSmartLinkListener {

    private static final String TAG = "ConfigActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private boolean mIsConncting = false;
    private SnifferSmartLinker mSnifferSmartLinker;
    private ProgressDialog mWaitingDialog;
    private String ssid;
    private String wifi_password;
    protected Handler mViewHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ssid = SPUtils.getString(this, Constant.WIFI_SSID);
        wifi_password = SPUtils.getString(this, Constant.WIFI_PASSWORD);
        mSnifferSmartLinker = SnifferSmartLinker.getInstence();
        mWaitingDialog = new ProgressDialog(this);
        mWaitingDialog.setMessage(getString(R.string.hiflying_smartlinker_waiting));
        mWaitingDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), (dialog, which) -> {
        });
        mWaitingDialog.setOnDismissListener(dialog -> {
            mSnifferSmartLinker.setOnSmartLinkListener(null);
            mSnifferSmartLinker.stop();
            mIsConncting = false;
        });
    }

    public void configWifi(View view) {
        if (!mIsConncting) {
            try {
                mSnifferSmartLinker.setOnSmartLinkListener(this);
                mSnifferSmartLinker.start(getApplicationContext(), wifi_password, ssid);
                mIsConncting = true;
                mWaitingDialog.show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mSnifferSmartLinker.setOnSmartLinkListener(null);

    }

    @Override
    public void onTimeOut() {
        Log.w(TAG, "onTimeOut");
        mViewHandler.post(() -> {
                // TODO Auto-generated method stub
                Utils.showToast(getApplicationContext(), getString(R.string.hiflying_smartlinker_timeout));
                mWaitingDialog.dismiss();
                mIsConncting = false;
        });
    }

    @Override
    public void onCompleted() {
        Log.w(TAG, "onCompleted");
        mViewHandler.post(() -> {
                // TODO Auto-generated method stub
                Utils.showToast(getApplicationContext(), getString(R.string.hiflying_smartlinker_completed));
                mWaitingDialog.dismiss();
                mIsConncting = false;
//                startActivity(new Intent(ConfigActivity.this, SettingTinyipActivity.class));
                startActivity(new Intent(ConfigActivity.this, BindGoodsActivity.class));
        });
    }

    private String moduleIP;
    private String mac;

    @Override
    public void onLinked(final SmartLinkedModule module) {
        // TODO Auto-generated method stub
        Log.w(TAG, "onLinked");
        mViewHandler.post(() ->{
                    mac = module.getMac();
                    moduleIP = module.getModuleIP();
                    Log.i("Config", mac);
                    Log.i("Config", moduleIP);
                    Utils.showToast(ConfigActivity.this, "连接成功");
                    mWaitingDialog.dismiss();
                    SPUtils.put(ConfigActivity.this, Constant.MODULE_IP, moduleIP);
//                    Intent intent = new Intent(ConfigActivity.this, SettingTinyipActivity.class);
                    Intent intent = new Intent(ConfigActivity.this, BindGoodsActivity.class);
//                intent.putExtra(Constant.MODULE_IP,module.getModuleIP());
                    startActivity(intent);
                    finish();
        });
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}

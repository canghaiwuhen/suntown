package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.APInfoBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class APDetialActivity extends BaseActivity {

    @BindView(R.id.apip)
    TextView apip;
    @BindView(R.id.apaddr)
    TextView apaddr;
    @BindView(R.id.port)
    TextView port;
    @BindView(R.id.shop)
    TextView shop;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.sn)
    TextView sn;
    @BindView(R.id.soft_version)
    TextView softVersion;
    @BindView(R.id.hardware_type)
    TextView hardwareType;
    @BindView(R.id.hardware_version)
    TextView hardwareVersion;
    @BindView(R.id.last_move_time)
    TextView lastMoveTime;
    @BindView(R.id.tv_num)
    TextView tvNum;
    private TextView tvBindNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apdetial);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        APInfoBean.RECORDBean recordBean = getIntent().getParcelableExtra("RECORD_BEAN");
        apip.setText(recordBean.APIP + "");
        apaddr.setText(recordBean.APADDR + "");
        port.setText(recordBean.PORT + "");
        shop.setText(recordBean.SNAME + "");
        state.setText(recordBean.STATUS == 0 ? "异常" : "正常");
        String Sn = recordBean.SN;
        sn.setText(sn.equals(null) ? "" : Sn);
        hardwareType.setText(recordBean.HWTYPE + "");
        hardwareVersion.setText(recordBean.HWVERSION);
        softVersion.setText(recordBean.SWVERSION);
        tvNum.setText(recordBean.EDCOUNT+"");
        lastMoveTime.setText(recordBean.LASTDATE);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}

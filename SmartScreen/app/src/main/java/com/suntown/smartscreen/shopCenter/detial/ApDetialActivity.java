package com.suntown.smartscreen.shopCenter.detial;

import android.os.Bundle;
import android.widget.TextView;

import com.suntown.smartscreen.R;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.data.ApListBean;
import com.suntown.smartscreen.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApDetialActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.apip)
    TextView apip;
    @BindView(R.id.apaddr)
    TextView apaddr;
    @BindView(R.id.port)
    TextView port;
    @BindView(R.id.sn)
    TextView sn;
    @BindView(R.id.tv_f0)
    TextView tvF0;
    @BindView(R.id.tv_f1)
    TextView tvF1;
    @BindView(R.id.hardware_type)
    TextView hardwareType;
    @BindView(R.id.hardware_version)
    TextView hardwareVersion;
    @BindView(R.id.soft_version)
    TextView softVersion;
    @BindView(R.id.update_time)
    TextView updateTime;

    @Override
    protected int getContentView() {
        return R.layout.activity_ap_detial;
    }

    @Override
    protected void initView() {
        tvTitle.setText("AP详情");
        ApListBean.RECORDBean recordBean = getIntent().getParcelableExtra(Constant.RECORD);
        apip.setText(recordBean.APIP );
        apaddr.setText(recordBean.APADDR );
        port.setText(recordBean.PORTNO );
        tvF0.setText(recordBean.F0);
        tvF1.setText(recordBean.F1);
        String Sn = recordBean.SN;
        sn.setText(null==sn ? "" : Sn);
        hardwareType.setText(recordBean.HWTYPE + "");
        hardwareVersion.setText(recordBean.HWVERSION);
        softVersion.setText(recordBean.SWVERSION);
        updateTime.setText(recordBean.LASTTASKDATE);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

}

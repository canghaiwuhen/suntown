package com.suntown.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntown.R;
import com.suntown.bean.DeviceInfoBean;
import com.suntown.utils.HexStr;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceListDatilActivity extends Activity {


    @BindView(R.id.tv_tag_ip)
    TextView tvTagIp;
    @BindView(R.id.tv_tag_battery)
    TextView tvTagBattery;
    @BindView(R.id.tv_tag_version)
    TextView tvTagVersion;
    @BindView(R.id.tv_band_goods)
    TextView tvBandGoods;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list_datil);
        ButterKnife.bind(this);
//        DeviceInfoBean.DeviceInfoAndMemidBean recordBean = (DeviceInfoBean.DeviceInfoAndMemidBean) getIntent().getParcelableExtra("recordBean");
        DeviceInfoBean.DeviceInfoAndMemidBean recordBean = getIntent().getParcelableExtra("recordBean");
        Log.i("DeviceActivity", "recordBean:" + recordBean);
        tvTagIp.setText(recordBean.getTINYIP());
        int batvalue = Math.abs(Integer.parseInt(recordBean.getBATVALUE()));
        String bateStr = batvalue + "";
        String str = bateStr.substring(0, 1) + "." + bateStr.substring(1 , 2);
        tvTagBattery.setText(str + "V");
        final String swversion = recordBean.getSWVERSION();
        Log.i("batvalue","batvalue:"+batvalue+",swversion"+swversion);
        final String i1 = swversion.substring(0, 2);
        final String i2 = swversion.substring(2, 4);
        Log.i("batvalue","swversion:"+ i1+",swversion"+i2);
        tvTagVersion.setText("未获取");
        tvBandGoods.setText(recordBean.getGNAME());
        tvGoodsPrice.setText("￥" + recordBean.getUPTPRICE());
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}

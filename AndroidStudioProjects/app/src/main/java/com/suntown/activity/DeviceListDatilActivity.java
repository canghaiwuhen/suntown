package com.suntown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.api.ApiService;
import com.suntown.bean.DeviceInfoBean;
import com.suntown.bean.TinyipAddressBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.widget.adapters.PopWindow;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class DeviceListDatilActivity extends BaseActivity {


    private static final String TAG = "DeviceListDatilActivity";
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
    @BindView(R.id.tv_receiver)
    TextView tvReceiver;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    private String tinyip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_device_list_datil);
        setContentView(R.layout.activity_tag_datil);
        ButterKnife.bind(this);

//        tvPay.setText("手机确认");
//        DeviceInfoBean.DeviceInfoAndMemidBean recordBean = (DeviceInfoBean.DeviceInfoAndMemidBean) getIntent().getParcelableExtra("recordBean");
        DeviceInfoBean.RECORDBean recordBean = getIntent().getParcelableExtra("recordBean");
        Log.i("DeviceActivity", "recordBean:" + recordBean);
        tinyip = recordBean.TINYIP;
        tvTagIp.setText(tinyip);

//        TODO 设置备注
//        String nikeName = SPUtils.getString(this, tinyip);
//        etNickName.setText(nikeName);

        String batvalue1 = recordBean.BATVALUE;
        if (!"".equals(batvalue1)) {
            int batvalue = Math.abs(Integer.parseInt(batvalue1));
            String bateStr = batvalue + "";
            String str = bateStr.substring(0, 1) + "." + bateStr.substring(1, 2);
            tvTagBattery.setText(str + "V");
            Log.i("batvalue", "batvalue:" + batvalue);
        }
        final String swversion = recordBean.SWVERSION;
        if (!"".equals(swversion)) {
            Log.i("batvalue", "swversion" + swversion);
            final String i1 = swversion.substring(0, 2);
            final String i2 = swversion.substring(2, 4);
            Log.i("batvalue", "swversion:" + i1 + ",swversion" + i2);
        }
        tvTagVersion.setText("未获取");
        tvBandGoods.setText(recordBean.GNAME);
        tvGoodsPrice.setText("￥" + recordBean.UPTPRICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressforTinyip(tinyip);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        String nickName = etNickName.getText().toString();
//        SPUtils.put(this,tinyip,nickName);
    }

    private void getAddressforTinyip(String tagNum) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, tagNum);
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).getAddressforTinyip(params).compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "s:" + s.toString());
                String json = s.replace("<ns:getAddressforTinyipResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:getAddressforTinyipResponse>", "");
                TinyipAddressBean tinyipAddressBean = new Gson().fromJson(json, TinyipAddressBean.class);
                if (tinyipAddressBean.ROWS > 0) {
                    TinyipAddressBean.RECORDBean recordBean = tinyipAddressBean.RECORD.get(0);
                    tvReceiver.setText(recordBean.RECEIVER);
                    tvAddress.setText(recordBean.ADDRESS);
                    tvTel.setText(recordBean.PHONE);
                }

            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == 150) {
//            String returnAddress = data.getStringExtra(Constant.ADDRESS);
//            if (!"".equals(returnAddress)) {
//                Log.i("text", "address:" + returnAddress);
//                String[] split = returnAddress.split("/");
//                tvReceiver.setText(split[0]);
//                tvTel.setText(split[1]);
//                tvAddress.setText(split[2]);
//            }
//        }
//    }

//    @OnClick({R.id.tv_change_address, R.id.iv_back, R.id.tv_pay})
    @OnClick({ R.id.iv_back,R.id.tv_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_address:
                Intent intent = new Intent(this, SettingTinyipActivity.class);
                intent.putExtra(Constant.TINTIP, tinyip);
                startActivity(intent);
                break;
        }
    }

}

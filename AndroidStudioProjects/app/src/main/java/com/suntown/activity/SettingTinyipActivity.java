package com.suntown.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.api.ApiClient;
import com.suntown.api.ApiService;
import com.suntown.bean.AddressBean;
import com.suntown.bean.LoginBean;
import com.suntown.bean.ServerInfoBean;
import com.suntown.bean.TinyipAddressBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.Constant;
import com.suntown.utils.HexStr;
import com.suntown.utils.SPUtils;
import com.suntown.utils.SmartSocketUtils;
import com.suntown.utils.SocThread;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;
import com.suntown.widget.LoadingDialog;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class SettingTinyipActivity extends BaseActivity {

    @BindView(R.id.tv_tinyip)
    TextView tvTinyip;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tel)
    TextView tel;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_item)
    LinearLayout llItem;
    private static final String TAG = "SettingTinyipActivity";
    private SocThread socketThread;
    private Context ctx;
    private String tagNum;
    private String versionName;
    private boolean hasNext;
    private String id;
    private String module_ip;
    private LoadingDialog dialog;
    private String batValueName;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_tinyip);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        init();
    }

    private void init() {
        tagNum = getIntent().getStringExtra(Constant.TAG_NAME);
        tvTinyip.setText(tagNum);
        getAddressforTinyip(tagNum);
    }

//    private void startSocket(String module_ip) {
//        final byte[] bytes = SmartSocketUtils.makeBytes(Constant.GET_TAGINFO, new byte[]{0x00}, this);
////        final byte[] bytes = SmartSocketUtils.makeBytes(Constant.HIBERNATE_TAG, new byte[]{0x0A});
//        Log.i(TAG, "bytes:" + bytes);
//        // 设置ip端口，连接超时时长 TODO 创建socket 获取标签IP
//        new Thread(() -> {
//            socketThread = new SocThread(mhandler, mhandlerSend, ctx, module_ip, bytes);
//            socketThread.start();
////                socketThread.send(bytes);
//        }).start();
//    }

    /**
     * 获取标签绑定地址
     *
     * @param tagNum
     */
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
                    tvName.setText(recordBean.RECEIVER);
                    tvAddress.setText(recordBean.ADDRESS);
                    tel.setText(recordBean.PHONE);
                    id = recordBean.ID;
                }

            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
        });
    }


    @OnClick({R.id.iv_back, R.id.ll_item, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
//                finish();
                break;
            case R.id.btn_next:
                if (Utils.isFastClick()) {
                    Utils.showToast(this,"请不要重复点击");
                    return;
                }
                if (!"".equals(id)) {
                    //TODO 设置地址
                    String tinyip = tvTinyip.getText().toString();
                    String address = tvName.getText().toString() + "/" + tel.getText().toString() + "/" + tvAddress.getText().toString();
                    Map<String, String> params = new HashMap<>();
                    params.put(Constant.ARG0, tinyip);
                    //地址ID
                    params.put(Constant.ARG1, id);
                    String ip = Constant.BASE_HOST;
                    Retrofit retrofit = new Retrofit.Builder().
                            addConverterFactory(ScalarsConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
                    retrofit.create(ApiService.class).setAddressforTinyip(params).compose(RxSchedulers.io_main()).subscribe(s -> {
                        Log.i(TAG, "s:" + s);
                        String json = s.replace("<ns:setAddressforTinyipResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                        json = json.replace("</ns:return></ns:setAddressforTinyipResponse>", "");
                        LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                        if (loginBean.getRESULT().equals("1")) {
                            //TODO 设置标签wifi
                             setWIFIAndIP(tagNum,SPUtils.getString(SettingTinyipActivity.this,Constant.WIFI_SSID));

                        }

                    }, throwable -> {
                        Log.i(TAG, "throwable:" + throwable);
                    });
                } else {
                    Utils.showToast(this, "请设置地址");
                }
                break;
            case R.id.ll_item:
                Intent intent = new Intent(this, AddressCenterActivity.class);
                intent.putExtra("isWaitPay", true);
                startActivityForResult(intent, 200);
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 300) {
            //TODO
            AddressBean.RECORDBean recordBean = data.getParcelableExtra(Constant.RECORD_BEAN);
            if (!"".equals(recordBean) && null != recordBean) {
                Log.i("text", "recordBean:" + recordBean.toString());
//                String[] split = returnAddress.split("/");
                tvName.setText(recordBean.RECEIVER);
                tel.setText(recordBean.PHONE);
                tvAddress.setText(recordBean.ADDRESS);
                id = recordBean.ID;
            }
        }
    }

    /**
     * 设置标签 wifi
     * @param tagNum
     * @param ssid
     */
    private void setWIFIAndIP(String tagNum, String ssid) {
        Log.i(TAG, "tagNum:" + tagNum+",ssid:"+ssid);
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, tagNum);
        params.put(Constant.ARG1, ssid);
        String ip = Constant.BASE_HOST;
//        String ip = "http://192.168.0.132:8082/axis2/services/SUNTOKWEBSERVICE/";
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).setWIFIIP(params).compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "s:" + s);
                String json = s.replace("<ns:setWIFIAndIPResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:setWIFIAndIPResponse>", "");
                LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                if (loginBean.getRESULT().equals("1")) {
                    Log.i(TAG,"设置标签WIFI地址成功");
                    Utils.showToast(SettingTinyipActivity.this,"设置标签WIFI地址成功");
                    //TODO 跳转中转界面
                    startActivity(new Intent(SettingTinyipActivity.this,GoOnConfigActivity.class));
                    finish();
                }else{
                    Log.i(TAG,"设置标签WIFI地址失败");
                }
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
        });
    }
}

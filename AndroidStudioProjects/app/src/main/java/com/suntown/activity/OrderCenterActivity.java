package com.suntown.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.api.ApiService;
import com.suntown.bean.BaseBean;
import com.suntown.fragment.CompletedFragment;
import com.suntown.fragment.UndoneFragment;
import com.suntown.fragment.WaitFragment;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Xml2String;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class OrderCenterActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "OrderCenterActivity";
    private LinearLayout llLoad;
    private ImageView ivBack;
    private LinearLayout llNoDevice;
    private TextView tvWaitPay;
    private TextView tvUndone;
    private TextView tvCompeted;
    private FrameLayout flShowFragment;
    public String memid;
    private FragmentManager fragmentManager;
    private OkHttpClient client;
    public List<String> memidList = new ArrayList<>();
    public String ssid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_center);
        memid = SPUtils.getString(this, Constant.MEMID);
        ssid = SPUtils.getString(this, Constant.WIFI_SSID);
        client = new OkHttpClient();
        //TODO 发送服务器推送信息清空
        resetPushNUM();
        init();
    }



    private void init() {
        getUserInfo();
        initView();
        fragmentManager = getFragmentManager();
        //显示隐藏Fragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_show_fragment,new WaitFragment());
        transaction.commit();

    }

    private void getUserInfo() {
        final String ssid = SPUtils.getString(this, Constant.WIFI_SSID);
            //@"arg0":wifiID,@"arg1":memid
    }

    private void initView() {
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        llLoad = ((LinearLayout) findViewById(R.id.ll_load));
        llNoDevice = ((LinearLayout) findViewById(R.id.ll_no_device));
        flShowFragment = ((FrameLayout) findViewById(R.id.fl_show_fragment));
        tvWaitPay = ((TextView) findViewById(R.id.tv_wait_pay));
        tvUndone = ((TextView) findViewById(R.id.tv_undone));
        tvCompeted = ((TextView) findViewById(R.id.tv_completed));
        tvWaitPay.setOnClickListener(TitleOnClickListener);
        tvUndone.setOnClickListener(TitleOnClickListener);
        tvCompeted.setOnClickListener(TitleOnClickListener);
        ivBack.setOnClickListener(this);
        changeTitle();

    }

    private View.OnClickListener TitleOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction transaction;
            switch (v.getId()){
                case R.id.tv_wait_pay:
                    changeTitle();
                    //TODO
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fl_show_fragment,new WaitFragment());
                    transaction.commit();
                    break;
                case R.id.tv_undone:
                    tvWaitPay.setBackgroundResource(R.color.colorWhite);
                    tvWaitPay.setTextColor(Color.GRAY);
                    tvUndone.setBackgroundResource(R.color.colorYello);
                    tvUndone.setTextColor(Color.WHITE);
                    tvCompeted.setBackgroundResource(R.color.colorWhite);
                    tvCompeted.setTextColor(Color.GRAY);
                    //TODO
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fl_show_fragment,new UndoneFragment());
                    transaction.commit();
                    break;
                case R.id.tv_completed:
                    tvWaitPay.setBackgroundResource(R.color.colorWhite);
                    tvWaitPay.setTextColor(Color.GRAY);
                    tvUndone.setBackgroundResource(R.color.colorWhite);
                    tvUndone.setTextColor(Color.GRAY);
                    tvCompeted.setBackgroundResource(R.color.colorYello);
                    tvCompeted.setTextColor(Color.WHITE);
                    //TODO
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fl_show_fragment,new CompletedFragment());
                    transaction.commit();
                    break;
            }
        }
    };

//    public void clickNetDevice(View view) {
//        startActivity(new Intent(this, NetWifiActivity.class));
//        finish();
//    }
    private void changeTitle() {
        tvWaitPay.setBackgroundResource(R.color.colorYello);
        tvWaitPay.setTextColor(Color.WHITE);
        tvUndone.setBackgroundResource(R.color.colorWhite);
        tvUndone.setTextColor(Color.GRAY);
        tvCompeted.setBackgroundResource(R.color.colorWhite);
        tvCompeted.setTextColor(Color.GRAY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
            break;
        }
    }
    //清空推送消息
    private void resetPushNUM() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, memid);
        params.put(Constant.ARG1, "1");
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
        addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).resetPushNUM(params).compose(RxSchedulers.io_main()).subscribe(s -> {
            try {
                String json = new Xml2String(s).Pull2Xml();
                BaseBean baseBean = new Gson().fromJson(json, BaseBean.class);
                if ("0".equals(baseBean.RESULT)) {
                    Log.i(TAG,"PUSHNUM 重置成功");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }, throwable -> {

        });
    }
}

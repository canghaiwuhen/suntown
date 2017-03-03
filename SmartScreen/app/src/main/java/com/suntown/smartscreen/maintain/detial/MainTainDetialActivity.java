package com.suntown.smartscreen.maintain.detial;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.data.BaseBean;
import com.suntown.smartscreen.data.DispMShopBean;
import com.suntown.smartscreen.data.ResultBean;
import com.suntown.smartscreen.maintain.MainTainAdapter;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.utils.BitmapUtils;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;
import com.suntown.smartscreen.weight.LoadingDialog;
import com.suntown.smartscreen.weight.NestedListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class MainTainDetialActivity extends BaseActivity {

    private static final String TAG = "MainTainDetialActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.nlv_main)
    NestedListView nlvMain;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.tv_angle)
    TextView tvAngle;
    @BindView(R.id.sl_main)
    ScrollView slMain;
    @BindView(R.id.ll_normal)
    LinearLayout llNormal;
    private MainTainDetialAdapter mainTainDetialAdapter;
    private String modUrl;
    List<DispMShopBean.RECORDBean> record = new ArrayList<>();
    private String dmCode;
    private LoadingDialog dialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_main_tain_detial;
    }

    @Override
    protected void initView() {
        dialog = new LoadingDialog(this);
        dialog.show();
        String specName = getIntent().getStringExtra(Constant.SPECNAME);
        dmCode = getIntent().getStringExtra(Constant.DMCODE);
        tvTitle.setText("".equals(specName)?"临保模板":specName);
        Log.i(TAG,"dmCode:"+dmCode+",  specName:"+specName);
        modUrl = SPUtils.getString(this, Constant.MODURL);
        if ("".equals(modUrl)) {
            modUrl = ApiConstant.BASE_URL;
        }
        requestData(modUrl, dmCode);
//        rlMain.setLayoutManager(new LinearLayoutManager(this));
//        mainTainDetialAdapter = new MainTainDetialAdapter(record);
//        rlMain.setAdapter(mainTainDetialAdapter);
        mainTainDetialAdapter = new MainTainDetialAdapter(this, record);
        nlvMain.setAdapter(mainTainDetialAdapter);
        mainTainDetialAdapter.setOnAdapterCallBack(new MainTainDetialAdapter.OnAdapterCallBack() {
            @Override
            public void onItemClick(int position) {
                //TODO 停用 ActiveGDM arg0=sid arg1 = dmcode arg2 = 1 启用  0停用
                DispMShopBean.RECORDBean recordBean = record.get(position);
                String sid = recordBean.SID;
                String activ = "0";
                if ("0".equals(recordBean.ISACTIVE)){
                    //启用
                    activ="1";
                }else if ("1".equals(recordBean.ISACTIVE)){
                    //停用
                    activ="0";
                }
                if (!Utils.isFastClick()) {
                    Log.i(TAG,"record:"+record.toString());
                    Log.i(TAG,"activ:"+activ);
                    submitServer(sid,dmCode,activ,position);
                }
            }
        });
    }

    /**
     * 停用 启用模板
     * @param sid
     * @param dmCode
     * @param activ
     */
    private void submitServer(String sid, String dmCode, String activ,int position) {
        new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(modUrl).build().
                //0不加载图片 1加载图片
                        create(ApiService.class).setActiveGDM(sid,dmCode, activ)
                .compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "s:" + s);
                String json = s.replace("<ns:ActiveGDMResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:ActiveGDMResponse>", "");
                Log.i(TAG, "json:" + json);
                ResultBean resultBean = new Gson().fromJson(json, ResultBean.class);
                if ("1".equals(resultBean.RESULT)) {
                    if ("1".equals(activ)) {
                        record.get(position).ISACTIVE="1";
                        Utils.showToast(MainTainDetialActivity.this,"已启用");
                    }else if ("0".equals(activ)){
                        record.get(position).ISACTIVE="0";
                        Utils.showToast(MainTainDetialActivity.this,"已停用");
                    }
                    Log.i(TAG,"record:"+record.toString());
                    mainTainDetialAdapter.notifyDataSetChanged();
                }else{
                    Utils.showToast(MainTainDetialActivity.this, "设置失败，请重试");
                }
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
            Utils.showToast(MainTainDetialActivity.this, "网络异常,请重试");
        });
    }

    /**
     * 获取模板详情
     *
     * @param modUrl
     * @param dmCode
     */
    private void requestData(String modUrl, String dmCode) {
        new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(modUrl).build().
                //0不加载图片 1加载图片
                        create(ApiService.class).getDMDetail(dmCode, 1 + "")
                .compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG, "s:" + s);
            String json = s.replace("<ns:getDMDetailResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            json = json.replace("</ns:return></ns:getDMDetailResponse>", "");
            Log.i(TAG, "s:" + s);
            DmDetialBean dmDetialBean = new Gson().fromJson(json, DmDetialBean.class);
            dialog.dismiss();
            if (0 < dmDetialBean.ROWS) {
                DmDetialBean.RECORDBean recordBean = dmDetialBean.RECORD.get(0);
                tvName.setText(recordBean.DMNAME);
                tvType.setText(recordBean.TYPENAME);
                tvSize.setText(recordBean.SPECNAME);
                tvAngle.setText(recordBean.ANGLE);
                Bitmap bitmap = BitmapUtils.stringtoBitmap(recordBean.DMIMG);
                ivPic.setImageBitmap(bitmap);
                requestShop(recordBean.DMCODE);
            } else {
                slMain.setVisibility(View.GONE);
                llNormal.setVisibility(View.VISIBLE);
                Utils.showToast(this, "暂无数据");
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
            Utils.showToast(this, "网络异常");
        });
    }

    /**
     * 返回模板分配的门店
     *
     * @param dmcode
     */
    private void requestShop(String dmcode) {
        new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(modUrl).build().
                create(ApiService.class).getDispMShops(dmcode).compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "s:" + s);
                String json = s.replace("<ns:getDispMShopsResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:getDispMShopsResponse>", "");
                Log.i(TAG, "s:" + s);
                DispMShopBean dispMShopBean = new Gson().fromJson(json, DispMShopBean.class);
                if (0 < dispMShopBean.ROWS) {
                    record.addAll(dispMShopBean.RECORD);
                    mainTainDetialAdapter.notifyDataSetChanged();
                } else {

                }
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
            Utils.showToast(MainTainDetialActivity.this, "网络异常");
        });
    }


    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

}

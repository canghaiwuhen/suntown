package com.suntown.smartscreen.spaceShelf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.data.ShelfInfoBean;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class ShelfDetialActivity extends BaseActivity {


    private static final String TAG = "ShelfDetialActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.elv_list)
    ExpandableListView elvList;
    boolean isToggleOn = false;
    private String modUrl;
    private List<ShelfInfoBean.RECORDBean> record;
    private ExpandAdapter adapter;
    List<Integer> list = new ArrayList<>();
    HashMap<Integer,List<ShelfInfoBean.RECORDBean>> hashMap = new HashMap<>();
    private LinearLayout llNormal;

    @Override
    protected int getContentView() {
        return R.layout.activity_shelf_detial;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String sid = intent.getStringExtra(Constant.SHOP_ID);
        String shelf = intent.getStringExtra(Constant.SHELF);
        modUrl = SPUtils.getString(this, Constant.MODURL);
        if ("".equals(modUrl)) {
            modUrl = ApiConstant.BASE_URL;
        }
        Log.i(TAG, "sid:" + sid + ",shelf:" + shelf);
        tvTitle.setText(shelf);
        initData(shelf,sid);
        llNormal = (LinearLayout) findViewById(R.id.ll_normal);
        adapter = new ExpandAdapter(this, list, hashMap);
        elvList.setAdapter(adapter);
    }

    /**
     * 获取数据
     * @param shelf
     * @param sid
     */
    private void initData(String shelf, String sid) {
//        getShelfData
        new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(modUrl).build().create(ApiService.class).
                getShelfData(shelf,sid).compose(RxSchedulers.io_main()).
                subscribe(s -> {
                    Log.i(TAG, "s:" + s);
                    String json = s.replace("<ns:getShelfDataResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                    json = json.replace("</ns:return></ns:getShelfDataResponse>", "");
                    Log.i(TAG, "json:" + json);
                    ShelfInfoBean shelfInfoBean =  new Gson().fromJson(json, ShelfInfoBean.class);
                    if (0<shelfInfoBean.ROWS) {
                        initList(shelfInfoBean);
                        adapter.notifyDataSetChanged();
                    }else{
                        llNormal.setGravity(View.VISIBLE);
                    }


                }, throwable -> {
                    Log.i(TAG, "throwable:" + throwable);
                    Utils.showToast(ShelfDetialActivity.this, "网络异常,请重试");
                });
    }

    /**
     * 设置数据
     * @param shelfInfoBean
     */
    private void initList(ShelfInfoBean shelfInfoBean) {
        record = shelfInfoBean.RECORD;
        List<ShelfInfoBean.SHELFBean> shelfList = shelfInfoBean.SHELF;
//        for (ShelfInfoBean.SHELFBean shelfBean : shelfList) {
//            int rownumbers = shelfBean.ROWNUMBERS;
//            list.add(rownumbers);
//        }
        Collections.sort(list);
        for (ShelfInfoBean.RECORDBean recordBean : record) {
            int rownumber = recordBean.ROWNUMBER;
            recordBean.isToggleOn=isToggleOn;
            if (hashMap.containsKey(rownumber)) {
                List<ShelfInfoBean.RECORDBean> recordBeen = hashMap.get(rownumber);
                recordBeen.add(recordBean);
            }else{
                list.add(rownumber);
                ArrayList<ShelfInfoBean.RECORDBean> recordBeen = new ArrayList<>();
                recordBeen.add(recordBean);
                hashMap.put(rownumber,recordBeen) ;
            }
        }
        for (Integer integer : list) {
            List<ShelfInfoBean.RECORDBean> recordBeen = hashMap.get(integer);
            Collections.sort(recordBeen, (recordBean, t1) -> {
                if(recordBean.COLNUMBER>t1.COLNUMBER){
                    return 1;
                }
                return -1;
            });
            Log.i(TAG, "list:" +list.toString());

        }
    }


    @OnClick({R.id.iv_back, R.id.tg_choose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tg_choose:
                isToggleOn=!isToggleOn;
                //TODO 视图切换 刷新适配器
                Log.i(TAG,"isToggleOn:"+isToggleOn);
                for (Integer integer : list) {
                    for (ShelfInfoBean.RECORDBean recordBean : hashMap.get(integer)) {
                        recordBean.isToggleOn=isToggleOn;
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }
}

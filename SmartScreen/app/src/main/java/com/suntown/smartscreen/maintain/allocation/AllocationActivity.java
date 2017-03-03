package com.suntown.smartscreen.maintain.allocation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.base.RxManager;
import com.suntown.smartscreen.data.AllShopBean;
import com.suntown.smartscreen.data.DispMShopBean;
import com.suntown.smartscreen.data.User;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.price.chooseShop.ChooseShopContract;
import com.suntown.smartscreen.price.chooseShop.ChooseShopModel;
import com.suntown.smartscreen.price.chooseShop.ChooseShopPresenter;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;
import com.suntown.smartscreen.utils.XmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.functions.Action1;

public class AllocationActivity extends BaseActivity<ChooseShopPresenter, ChooseShopModel> implements ChooseShopContract.View {


    private static final String TAG = "AllocationActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_fast)
    ListView lvFast;
    private ArrayList<InfoBean> nameList;
    private List<AllShopBean> allShopBeanList;
    private String dmCode;
    private String modUrl;
    private List<DispMShopBean.RECORDBean> record;

    @Override
    protected int getContentView() {
        return R.layout.activity_allocation;
    }

    @Override
    protected void initView() {
        tvTitle.setText("门店选择");
        dmCode = getIntent().getStringExtra(Constant.DMCODE);
        Log.i(TAG, "dmCode:" + dmCode);
        String userid = SPUtils.getString(this, Constant.USER_ID);
        modUrl = SPUtils.getString(this, Constant.MODURL);
        if ("".equals(modUrl)) {
            modUrl = ApiConstant.BASE_URL;
        }
        //TODO 获取所有门店
        mPresenter.getUserShops(userid, modUrl);
    }

    @Override
    public void getUserShopsSuccess(List<AllShopBean> allShopBeanList) {
        this.allShopBeanList = allShopBeanList;
        nameList = new ArrayList<>();
        for (AllShopBean allShopBean : allShopBeanList) {
            String name = allShopBean.name;
            nameList.add(new InfoBean(name, false));
        }
        getDispMShops(dmCode);
        MyAdapter myAdapter = new MyAdapter(this, nameList);
        lvFast.setAdapter(myAdapter);
        lvFast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = nameList.get(i).name;
                nameList.clear();
                for (AllShopBean allShopBean : allShopBeanList) {
                    if (allShopBean.name.equals(name)) {
                        List<AllShopBean.CityBean> city = allShopBean.city;
                        for (AllShopBean.CityBean cityBean : city) {
                            nameList.add(new InfoBean(cityBean.name, false));
                        }
                        break;
                    }
                }
                for (AllShopBean allShopBean : allShopBeanList) {
                    for (AllShopBean.CityBean cityBean : allShopBean.city) {
                        if (cityBean.name.equals(name)) {
                            List<AllShopBean.CityBean.ShopBean> shop = cityBean.shop;
                            for (AllShopBean.CityBean.ShopBean shopBean : shop) {
                                InfoBean e = new InfoBean(shopBean.name, true);
                                for (DispMShopBean.RECORDBean recordBean : record) {
                                    if (recordBean.SNAME.equals(shopBean.name)) {
                                        e.isCheched = true;
                                    }
                                }
                                nameList.add(e);
                            }
                            break;
                        }
                    }
                }
                for (AllShopBean allShopBean : allShopBeanList) {
                    for (AllShopBean.CityBean cityBean : allShopBean.city) {
                        if (cityBean.name.equals(name)) {
                            cityBean.checked = !cityBean.checked;
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void showMsg(String msg) {
        Utils.showToast(this, msg);
    }

    @OnClick({R.id.iv_back, R.id.button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.button:
                //TODO
                List<String> stringList = new ArrayList<>();
                for (InfoBean infoBean : nameList) {
                    if (infoBean.isCheched) {
                        stringList.add(infoBean.name);
                    }
                }
                if (0 < stringList.size()) {
                    //nameList
                    ArrayList<SubBean> sidList = new ArrayList<>(); //( 1 删除  0 插入)
                    //判断有没有改变旧数据
                        for (DispMShopBean.RECORDBean recordBean : record) {
                            for (InfoBean infoBean : nameList) {
                                if (recordBean.SNAME.equals(infoBean.name)) {
                                    boolean isCheched = infoBean.isCheched;
                                    if (!isCheched){
                                        sidList.add(new SubBean(recordBean.SID, 1));
                                    }
                                }
                            }
                    }
                    for (String name : stringList) {
                        for (AllShopBean allShopBean : allShopBeanList) {
                            for (AllShopBean.CityBean cityBean : allShopBean.city) {
                                for (AllShopBean.CityBean.ShopBean shopBean : cityBean.shop) {
                                    if (name.equals(shopBean.name)) {
                                        sidList.add(new SubBean(shopBean.id, 0));
                                    }
                                }
                            }
                        }
                    }
                    Log.i(TAG, "name:" + stringList.toString());
                    //转化成xml
                    String xml = XmlUtils.SubBean2Xml(dmCode, sidList);
                    submitServer(xml);
                } else {
                    Utils.showToast(AllocationActivity.this, "不能提交");
                }
                break;
        }
    }

    /**
     * 返回模板分配的门店
     *
     * @param dmCode
     */
    private void getDispMShops(String dmCode) {
        new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(modUrl).build().
                create(ApiService.class).getDispMShops(dmCode)
                .compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG, "s:" + s);
            String json = s.replace("<ns:getDispMShopsResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            json = json.replace("</ns:return></ns:getDispMShopsResponse>", "");
            Log.i(TAG, "json:" + json);
            DispMShopBean dispMShopBean = new Gson().fromJson(json, DispMShopBean.class);
            record = dispMShopBean.RECORD;

        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
            Utils.showToast(AllocationActivity.this, "网络异常");
        });
    }

    /**
     * 提交服务器
     * @param xml
     */
    private void submitServer(String xml) {
        Log.i(TAG,"xml:"+xml);
        new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(modUrl).build().
                create(ApiService.class).AllocDispM(xml)
                .compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "s:" + s);
                String json = s.replace("<ns:AllocDispMResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:AllocDispMResponse>", "");
                Log.i(TAG, "s:" + json);
                User user = new Gson().fromJson(json, User.class);
                if ("1".equals(user.RESULT)) {
                    Utils.showToast(AllocationActivity.this,"门店配置成功");
                    Observable.timer(500, TimeUnit.MILLISECONDS).subscribe(aLong -> {finish();});
                }else{
                    Utils.showToast(AllocationActivity.this,"门店配置失败，请重试");
                }
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
            Utils.showToast(AllocationActivity.this, "网络异常");
        });
    }

}

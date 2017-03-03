package com.suntown.smartscreen.shopCenter;

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
import com.suntown.smartscreen.data.AllTaskBean;
import com.suntown.smartscreen.data.DmListBean;
import com.suntown.smartscreen.data.User;
import com.suntown.smartscreen.maintain.allocation.AllocationActivity;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;
import com.suntown.smartscreen.utils.XmlUtils;
import com.suntown.smartscreen.weight.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class AllocatingTaskActivity extends BaseActivity {


    private static final String TAG = "AllocatingTaskActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.button)
    TextView commitButton;
    @BindView(R.id.elv_list)
    ExpandableListView elvList;
    private String modUrl;
    private String userId;
    private LinearLayout llNoData;
    private ExpandableAdapter adapter;
    private HashMap<String,List<DmListBean.RECORDBean>> listHashMap = new HashMap<>();
    private List<AllTaskBean.RECORDBean> list = new ArrayList<>() ;
    private String sid;
    private List<DmListBean.RECORDBean> record ;
    private LoadingDialog dialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_allocating_task;
    }

    @Override
    protected void initView() {
        tvTitle.setText("分配模板");
        dialog = new LoadingDialog(this);
        dialog.show();
        llNoData = (LinearLayout) findViewById(R.id.ll_no_data);
        sid = getIntent().getStringExtra(Constant.USER_ID);
        modUrl = SPUtils.getString(this, Constant.MODURL);
        userId = SPUtils.getString(this, Constant.USER_ID);
        if ("".equals(modUrl)) {
            modUrl = ApiConstant.BASE_URL;
        }
        // 获取待分配模板类型
        new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(modUrl).build().
                create(ApiService.class).getUnAllocedDMTypes(userId, sid)
                .compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
//                Log.i(TAG, "s:" + s);
                String json = s.replace("<ns:getUnAllocedDMTypesResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:getUnAllocedDMTypesResponse>", "");
//                Log.i(TAG, "json:" + json);
                AllTaskBean taskBean = new Gson().fromJson(json, AllTaskBean.class);
                if (0 < taskBean.ROWS) {
                    list = taskBean.RECORD;
                    for (int i = 0; i < list.size(); i++) {
                        AllTaskBean.RECORDBean recordBean = list.get(i);
                        ArrayList<DmListBean.RECORDBean> value = new ArrayList<>();
                        listHashMap.put(recordBean.TYPENAME, value);
                        getDMListFor(list.get(i).TYPE,i);
                    }
                    adapter = new ExpandableAdapter(AllocatingTaskActivity.this, list,listHashMap);
//                    adapter.setOnAdapterCallBack(new );
                    elvList.setAdapter(adapter);
                } else {
                    llNoData.setVisibility(View.VISIBLE);
                }
            }
        }, throwable -> {
            llNoData.setVisibility(View.VISIBLE);
            Log.i(TAG, "throwable:" + throwable);
            Utils.showToast(AllocatingTaskActivity.this, "网络异常,请重试");
        });
    }

    private void getDMListFor(String TYPE, int parentPosition) {
        new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(modUrl).build().
                create(ApiService.class).getDMListFor(userId, sid,TYPE,"")
                .compose(RxSchedulers.io_main()).subscribe(s -> {
                    Log.i(TAG, "s:" + s);
                    String json = s.replace("<ns:getDMListForResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                    json = json.replace("</ns:return></ns:getDMListForResponse>", "");
                    Log.i(TAG, "json:" + json);
                    //TODO
                    DmListBean dmListBean = new Gson().fromJson(json, DmListBean.class);
                    dialog.dismiss();
                    if (0<dmListBean.ROWS) {
                        record = dmListBean.RECORD;
                        String typename = list.get(parentPosition).TYPENAME;
                        if (!listHashMap.containsKey(typename)) {
                            listHashMap.put(typename,record);
                        }else {
                            List<DmListBean.RECORDBean> dmListBeen = listHashMap.get(typename);
                            dmListBeen.clear();
                            dmListBeen.addAll(record);
                        }
                    }else{
                        Utils.showToast(AllocatingTaskActivity.this, "暂无数据");
                    }
                }, throwable -> {
                    Log.i(TAG, "throwable:" + throwable);
                    Utils.showToast(AllocatingTaskActivity.this, "网络异常,请重试");
                });
    }


    @OnClick({R.id.iv_back, R.id.button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.button:
                //TODO
                String xml = XmlUtils.allTaskBean2Xml(sid,list,listHashMap);
                Log.i(TAG,"xml:"+xml);
//                Log.i(TAG,"listHashMap:"+listHashMap.toString());
                submitServer(xml);
                commitButton.setClickable(false);
                commitButton.setBackgroundResource(R.color.grgray);
                break;
        }
    }

    /**
     * 提交数据至服务器
     * @param xml
     */
    private void submitServer(String xml) {
        new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(modUrl).build().
                create(ApiService.class).ShopAllocDispM(xml).compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "s:" + s);
                String json = s.replace("<ns:ShopAllocDispMResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:ShopAllocDispMResponse>", "");
                Log.i(TAG, "json:" + json);
                User user = new Gson().fromJson(json, User.class);
                if ("1".equals(user.RESULT)) {
                    Utils.showToast(AllocatingTaskActivity.this,"数据提交成功");
                    commitButton.setClickable(true);
                    commitButton.setBackgroundResource(R.drawable.login_confirm_selector);
                }else{
                    Utils.showToast(AllocatingTaskActivity.this,"数据提交失败");
                }
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
            Utils.showToast(AllocatingTaskActivity.this, "网络异常,请重试");
        });
    }
}

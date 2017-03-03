package com.suntown.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.adapter.TagDetialAdapter;
import com.suntown.adapter.TagInfoAdapter;
import com.suntown.bean.DeviceInfoBean;
import com.suntown.bean.LoginBean;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeviceListActivity extends BaseActivity implements View.OnClickListener {
    public String TAG = "DeviceListActivity";
    private OkHttpClient client;
    private String memid;
    private String bssid;
    private List<DeviceInfoBean.RECORDBean> list = new ArrayList<>();
//    private TagInfoAdapter adapter = new TagInfoAdapter(this, list);
    private TagDetialAdapter adapter = new TagDetialAdapter(this, list);
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                List<DeviceInfoBean.RECORDBean> InfoAndMemidBean = (List<DeviceInfoBean.RECORDBean>) msg.obj;
                Log.i(TAG, "InfoAndMemidBean:" + InfoAndMemidBean + "");
                list.addAll(InfoAndMemidBean);
                Collections.sort(list, (lhs, rhs) -> {
                    Log.i(TAG, "i:" + lhs.TINYIP + ",i2:" + rhs.TINYIP);
                    String[] i = lhs.TINYIP.split("\\.");
                    String[] i2 = rhs.TINYIP.split("\\.");
                    if (Integer.parseInt(i[i.length - 1]) > Integer.parseInt(i2[i2.length - 1])) {
                        return 1;
                    }
                    return -1;
                });
                Log.i(TAG, "list:" + list + "," + "listSize:" + list.size());
                if (list == null) {
                    llHide.setVisibility(View.VISIBLE);
                    pbLoading.setVisibility(View.GONE);
//                    tvSetting.setClickable(false);
//                    tvSetting.setTextColor(Color.GRAY);
                    return;
                }
                llHide.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);
                lvGoods.setLayoutManager(new LinearLayoutManager(DeviceListActivity.this));
//                lvGoods.setAdapter(chartAdapter);
                lvGoods.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.setOnSwipeAdapterCallBack(onSwipeAdapterCallBack);
            }
        }
    };

    private RecyclerView lvGoods;
//    private ListView lvGoods;
    private ImageView ivBack;
    //    private TextView tvSetting;
    private ProgressBar pbLoading;
    private String ssid;
    private LinearLayout llHide;
    private SwipeRefreshLayout swipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
//        lvGoods = ((ListView) findViewById(R.id.lv_goods));
        lvGoods = ((RecyclerView) findViewById(R.id.lv_goods));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
//        tvSetting = ((TextView) findViewById(R.id.tv_setting));
        pbLoading = ((ProgressBar) findViewById(R.id.pb_loading));
        llHide = ((LinearLayout) findViewById(R.id.ll_hide));
        ivBack.setOnClickListener(this);
//        tvSetting.setOnClickListener(this);
        client = new OkHttpClient();
        memid = SPUtils.getString(this, Constant.MEMID);
        bssid = SPUtils.getString(this, Constant.BSSID);
//        ssid = SPUtils.getString(this, Constant.WIFI_SSID);
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        ssid = Utils.getSsid(wm);
        Log.i("test", "memid:" + memid + "  " + "ssid:" + this.ssid);
//        getUserInfo();
        getDeviceInfo(memid);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefresh.setDistanceToTriggerSync(1000);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        swipeRefresh.setProgressBackgroundColor(R.color.colorWhite); // 设定下拉圆圈的背景
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        swipeRefresh.setOnRefreshListener(() -> {
            list.clear();
            adapter.notifyDataSetChanged();
            ssid = Utils.getSsid(wm);
//            getUserInfo();
            getDeviceInfo(memid);
            new Handler().postDelayed(() -> {
                // 停止刷新
                swipeRefresh.setRefreshing(false);
            }, 2000); // 5秒后发送消息，停止刷新
        });
    }

    private void getDeviceInfo(final String id) {
        Log.i(TAG,"id:"+id);
        FormBody body = new FormBody.Builder()
                .add("arg0", id)
                .build();
        //TODO
        Request reques = new Request.Builder().post(body).url(Constant.BASE_HOST + "Getoked_infoNew").build();
        client.newCall(reques).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    llHide.setVisibility(View.VISIBLE);
                });
                Utils.showToast(DeviceListActivity.this, "请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = response.body().byteStream();
                String json;
                runOnUiThread(() -> pbLoading.setVisibility(View.GONE));
                try {
                    json = new Xml2Json(is).Pull2Xml();
                    final DeviceInfoBean deviceInfoBean = new Gson().fromJson(json, DeviceInfoBean.class);
                        Log.i(TAG,"deviceInfoBean:"+deviceInfoBean.toString());
                    int rows = deviceInfoBean.ROWS;
                    if (rows != 0) {
                        final List<DeviceInfoBean.RECORDBean> record = deviceInfoBean.RECORD;
                        Log.i(TAG,"record:"+record.toString());
                        List<DeviceInfoBean.RECORDBean> deviceInfoAndMemidBeen = new ArrayList<>();
                        deviceInfoAndMemidBeen.addAll(record);
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = deviceInfoAndMemidBeen;
                        handler.sendMessage(msg);
                    } else {
                        llHide.setVisibility(View.VISIBLE);
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //侧滑点击事件
    private int bindItemPosition;
    private TagInfoAdapter.OnSwipeAdapterCallBack onSwipeAdapterCallBack = new TagInfoAdapter.OnSwipeAdapterCallBack() {
        @Override
        public void onItemClick(int position) {
            final DeviceInfoBean.RECORDBean recordBean = list.get(position);
//            Utils.showToast(DeviceListActivity.this,"点击了"+position);
            Intent intent = new Intent(DeviceListActivity.this, DeviceListDatilActivity.class);
            intent.putExtra("recordBean", recordBean);
            startActivity(intent);
        }

        @Override
        public void deleteItemClick(final int position) {
//            Utils.showToast(DeviceListActivity.this,"删除");
            //todo 删除标签 弹出dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(DeviceListActivity.this);
            builder.setTitle("您确定删除该标签吗?").
                    setNegativeButton("取消", null).
                    setPositiveButton("确定", (dialog, which) -> {
                        deleteTag(position);
//                    list.remove(position);
//                    adapter.notifyDataSetChanged();
                    }).show();
        }

        /**
         *   一键配置
         */
        @Override
        public void confrimItemClick(int position) {
//            Utils.showToast(DeviceListActivity.this,"配置");
            Intent intent = new Intent(DeviceListActivity.this, DeviceOneKeyConfigActivity.class);
            intent.putExtra(Constant.TAG_NAME, list.get(position).TINYIP);
            startActivity(intent);
        }

        //绑定商品
        @Override
        public void bindItemClick(int position) {
            bindItemPosition = position;
            final String tinyip = list.get(position).TINYIP;
//            Utils.showToast(DeviceListActivity.this,"绑定");
            Intent intent = new Intent(DeviceListActivity.this, BindGoodsAgainActivity.class);
            intent.putExtra(Constant.TAG_NAME, tinyip);
            startActivityForResult(intent, 200);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 300 && requestCode == 200) {
            String goodsName = data.getStringExtra(Constant.GOODS_NAME);
            DeviceInfoBean.RECORDBean deviceInfoAndMemidBean = list.get(bindItemPosition);
            deviceInfoAndMemidBean.GNAME=goodsName;
            adapter.notifyDataSetChanged();
        }
    }

    private void deleteTag(final int position) {
         DeviceInfoBean.RECORDBean recordBean = list.get(position);
         String tinyip = recordBean.TINYIP;
        RequestBody formBody = new FormBody.Builder()
                .add("arg0", tinyip)
                .add("arg1", memid)
                .build();
        final Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST("delOked_user"))
                .post(formBody)
                .build();
        new Thread(() -> {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Utils.showToast(DeviceListActivity.this, "联网失败，请检查网络");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        String json;
                        try {
                            json = new Xml2Json(is).Pull2Xml();
                            Log.i(TAG, "json:" + json);
                            final LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                            final String result = loginBean.getRESULT();
                            if ("0".equals(result)) {
                                Utils.showToast(DeviceListActivity.this, "删除标签成功");
                                runOnUiThread(() -> {
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                });
                            } else {
                                Utils.showToast(DeviceListActivity.this, "删除标签失败");
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
        }).start();
    }

    @OnClick({R.id.iv_back, R.id.ll_add_device})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_add_device:
                startActivity(new Intent(DeviceListActivity.this, NetWifiActivity.class));
                break;
        }
    }
}

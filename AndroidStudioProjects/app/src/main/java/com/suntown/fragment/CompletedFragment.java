package com.suntown.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.activity.CompletedActivity;
import com.suntown.activity.DeviceListActivity;
import com.suntown.adapter.CompletedAdapter;
import com.suntown.api.ApiService;
import com.suntown.bean.AddressBean;
import com.suntown.bean.BaseBean;
import com.suntown.bean.LoginBean;
import com.suntown.bean.UserInfoBean;
import com.suntown.bean.WaitConfirmBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.Constant;
import com.suntown.utils.DateUtils;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * Created by Administrator on 2016/8/24.
 */
public class CompletedFragment extends Fragment {
    private static final String TAG = "CompletedFragment";
    private View inflate;
    private OkHttpClient client;
    private LinearLayout llLoad;
    private ListView lvFragment;
    private LinearLayout noOrder;
    private Button startDevice;
    private List<String> memidList = new ArrayList<>();
    private List<WaitConfirmBean.RECORDBean> record;
    private List<WaitConfirmBean.RECORDBean>  recordBeanList = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1)
            llLoad.setVisibility(View.GONE);
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==2){
                int position = (int) msg.obj;
                recordBeanList.remove(position);
                completedAdapter.notifyDataSetChanged();
            }
        }
    };
    private CompletedAdapter completedAdapter;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.order_fragment, container, false);
        initUi();
        return inflate;
    }

    private void initUi() {
        client = new OkHttpClient();
        final String ssid = SPUtils.getString(getActivity(), Constant.WIFI_SSID);
        final String memid = SPUtils.getString(getActivity(), Constant.MEMID);
        llLoad = (LinearLayout) inflate.findViewById(R.id.ll_load);
        lvFragment = (ListView) inflate.findViewById(R.id.lv_fragment);
        noOrder = (LinearLayout)inflate.findViewById(R.id.ll_no_device);
        startDevice = (Button)inflate.findViewById(R.id.btn_connDevice);
        startDevice.setOnClickListener(v -> startActivity(new Intent(getActivity(), DeviceListActivity.class)));
//        getUserInfo(ssid,memid);
        requestData(memid);
        completedAdapter = new CompletedAdapter(getActivity(),recordBeanList);
        lvFragment.setAdapter(completedAdapter);
        completedAdapter.setCompletedAdapterCallBack(completedCallBack);
        swipeRefresh = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefresh.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        swipeRefresh.setProgressBackgroundColor(R.color.colorWhite); // 设定下拉圆圈的背景
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        swipeRefresh.setOnRefreshListener(() -> {
                recordBeanList.clear();
                memidList.clear();
//                getUserInfo(ssid,memid);
            requestData(memid);
                new Handler().postDelayed(() -> {
                    {
                        // 停止刷新
                        swipeRefresh.setRefreshing(false);
                    }
                }, 3000); // 5秒后发送消息，停止刷新
            });
        new Handler().postDelayed(() -> llLoad.setVisibility(View.GONE), 2000);
    }

    private void requestData(String memid) {
        //TODO 同一WIFI 环境下所有订单
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, memid);
        params.put(Constant.ARG1, "1");
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).getHistoryOrderNew(params).compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG, "s:" + s);
            String json = s.replace("<ns:getHistoryOrderNewResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            json = json.replace("</ns:return></ns:getHistoryOrderNewResponse>", "");
            WaitConfirmBean waitConfirmBean = new Gson().fromJson(json, WaitConfirmBean.class);
            List<WaitConfirmBean.RECORDBean> record = waitConfirmBean.getRECORD();
            String result = waitConfirmBean.getRESULT();
            if ("0".equals(result)) {
                llLoad.setVisibility(View.GONE);
                if (record == null) {
                    noOrder.setVisibility(View.VISIBLE);
                }
                recordBeanList.addAll(record);
                Collections.sort(recordBeanList, (lhs, rhs) -> {
                    Date date1 = DateUtils.stringToDate(lhs.getADDDATE());
                    Date date2 = DateUtils.stringToDate(rhs.getADDDATE());
                    // 对日期字段进行升序，如果欲降序可采用after方法
                    if (date1.before(date2)) {
                        return 1;
                    }
                    return -1;
                });
                Log.i("test", "list:" + record.size() + "," + "recordList:" + recordBeanList.size());
                noOrder.setVisibility(View.GONE);
                completedAdapter.notifyDataSetChanged();
            } else {
                if(0==recordBeanList.size()){
                    noOrder.setVisibility(View.VISIBLE);
                }
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
            noOrder.setVisibility(View.VISIBLE);
        });
    }
    private CompletedAdapter.CompletedAdapterCallBack completedCallBack = new CompletedAdapter.CompletedAdapterCallBack() {
        @Override
        public void deleteItemClick(int position) {
            final String memid = recordBeanList.get(position).getMEMID();
            final String formno = recordBeanList.get(position).getFORMNO();
            Log.i("test","memid:"+memid+",formno:"+formno);
            deleteOrder(memid,formno,position);
        }

        @Override
        public void onItemCLick(int position) {
            //跳转到商品详情列表
            WaitConfirmBean.RECORDBean recordBean = recordBeanList.get(position);
            Intent intent = new Intent(getActivity(), CompletedActivity.class);
            intent.putExtra("recordBean",recordBean);
            startActivity(intent);
        }
    };

    private void deleteOrder(final String memid, final String formno, final int position) {
        new Thread(() -> {
            {
                RequestBody formBody = new FormBody.Builder()
                        .add("arg0",formno)//TODO 待修改
                        .add("arg1",memid)
                        .build();
                final Request request = new Request.Builder()
                        .url(Constant.BASE_HOST+"deleteOrder")
                        .post(formBody)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        String json;
                        try {
                            json = new Xml2Json(is).Pull2Xml();
                            LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                            if (loginBean.getRESULT().equals("0")) {
//                                Utils.showToast(getActivity(), "删除成功");
                                //TODO 从条目中删除
                                mHandler.obtainMessage(2, position).sendToTarget();
                            } else {
                                Utils.showToast(getActivity(), "请重试");
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

}

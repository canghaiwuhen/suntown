package com.suntown.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.suntown.activity.DeviceListActivity;
import com.suntown.activity.OrderCenterActivity;
import com.suntown.activity.UnDoneOrderDetialActivity;
import com.suntown.activity.WaitPayOrderDetialActivity;
import com.suntown.adapter.UndoneAdapter;
import com.suntown.adapter.WaitPayAdapter;
import com.suntown.api.ApiService;
import com.suntown.bean.LoginBean;
import com.suntown.bean.UserInfoBean;
import com.suntown.bean.WaitConfirmBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.Constant;
import com.suntown.utils.DateUtils;
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
 * Created by Administrator on 2016/8/18.
 */
public class UndoneFragment extends Fragment {
    private static final String TAG = "UndoneFragment";
    private View inflate;
    private OkHttpClient client;
    private ListView lvFragment;
    public List<String> memidList = new ArrayList<>();
    public List<WaitConfirmBean.RECORDBean> recordBeanList = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                llLoad.setVisibility(View.GONE);
                List<WaitConfirmBean.RECORDBean> record = (List<WaitConfirmBean.RECORDBean>) msg.obj;
                Log.i("test","record:"+record+",recordBeanList:"+recordBeanList);
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
                noDevice.setVisibility(View.GONE);

                undoneAdapter.notifyDataSetChanged();
            }
            llLoad.setVisibility(View.GONE);
        }
    };
    private LinearLayout llLoad;
    private LinearLayout noDevice;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    int donePosostion = msg.arg1;
                    recordBeanList.remove(donePosostion);
                    undoneAdapter.notifyDataSetChanged();
                    break;
                case 2:
//                    int deletePosition = msg.arg1;
                    Log.i("deletePosition","deletePosition:"+deletePosition);
                    recordBeanList.remove(deletePosition);
                    undoneAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    private UndoneAdapter undoneAdapter;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.order_fragment, container, false);
        initUi();
        return inflate;
    }

    private void initUi() {
        client = new OkHttpClient();
        llLoad = (LinearLayout) inflate.findViewById(R.id.ll_load);
        lvFragment = (ListView) inflate.findViewById(R.id.lv_fragment);
        noDevice = ((LinearLayout) inflate.findViewById(R.id.ll_no_device));
        Button startDevice = (Button)inflate.findViewById(R.id.btn_connDevice);
        startDevice.setOnClickListener(v -> startActivity(new Intent(getActivity(), DeviceListActivity.class)));
        final String Memid =  ((OrderCenterActivity) getActivity()).memid;
        final String ssid =  ((OrderCenterActivity) getActivity()).ssid;
//        getUserInfo(Memid,ssid);
        requestData(Memid);
        new Handler().postDelayed(() -> llLoad.setVisibility(View.GONE), 2000);
        undoneAdapter = new UndoneAdapter(getActivity(), recordBeanList);
        lvFragment.setAdapter(undoneAdapter);
        undoneAdapter.setOndoneAdapterCallBack(ondoneAdapterCallback);
        swipeRefresh = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefresh.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        swipeRefresh.setProgressBackgroundColor(R.color.colorWhite); // 设定下拉圆圈的背景
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        swipeRefresh.setOnRefreshListener(() -> {
                recordBeanList.clear();
                memidList.clear();
                undoneAdapter.notifyDataSetChanged();
//                getUserInfo(Memid,ssid);
                 requestData(Memid);
                new Handler().postDelayed(() -> swipeRefresh.setRefreshing(false), 3000);
        });
    }


    private void requestData(String memid) {
        //TODO 同一WIFI 环境下所有订单
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, memid);
        params.put(Constant.ARG1, "0");
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
                    noDevice.setVisibility(View.VISIBLE);
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
                noDevice.setVisibility(View.GONE);
                undoneAdapter.notifyDataSetChanged();
            } else {
                if (0 == recordBeanList.size()) {
                    noDevice.setVisibility(View.VISIBLE);
                }
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
            noDevice.setVisibility(View.VISIBLE);
        });
    }

    private int currentPosition;
    private int deletePosition;
    private UndoneAdapter.OndoneAdapterCallBack ondoneAdapterCallback = new UndoneAdapter.OndoneAdapterCallBack() {
        @Override
        public void deleteItemClick(int position) {
            deletePosition = position;
            //向服务器发起删除请求  deleteOrder  FORMNO memid
            final String memid = recordBeanList.get(position).getMEMID();
            final String formno = recordBeanList.get(position).getFORMNO();
            deleteOrder(memid,formno,position);
        }

        @Override
        public void buttonItemCLick(int position) {
            currentPosition = position;

            showDialog();
        }

        @Override
        public void onItemCLick(int position) {
            //跳转到商品详情列表
            WaitConfirmBean.RECORDBean recordBean = recordBeanList.get(position);
            Intent intent = new Intent(getActivity(), UnDoneOrderDetialActivity.class);
            intent.putExtra("recordBean",recordBean);
            startActivity(intent);
        }
    };

    private void deleteOrder(final String memid, final String formno, final int position) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, formno);
        params.put(Constant.ARG1, memid);
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).deleteOrder(params).compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG, "s:" + s);
            String json = s.replace("<ns:deleteOrderResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            json = json.replace("</ns:return></ns:deleteOrderResponse>", "");
            LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
            if (loginBean.getRESULT().equals("0")) {
                Utils.showToast(getActivity(), "删除成功");
                recordBeanList.remove(position);
                undoneAdapter.notifyDataSetChanged();
            }else{
                Utils.showToast(getActivity(), "请重试");
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
        });
    }
//    {
//        Log.i("deletePosition","deletePosition:"+position+",formno:"+formno);
//        new Thread(() -> {
//                RequestBody formBody = new FormBody.Builder()
//                        .add("arg0",formno)//TODO 待修改
//                        .add("arg1",memid)
//                        .build();
//                final Request request = new Request.Builder()
//                        .url(Constant.BASE_HOST+"deleteOrder")
//                        .post(formBody)
//                        .build();
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        InputStream is = response.body().byteStream();
//                        String json;
//                        try {
//                            json = new Xml2Json(is).Pull2Xml();
//                            LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
//                            if (loginBean.getRESULT().equals("0")) {
////                                Utils.showToast(getActivity(), "删除成功");
//                                //TODO 从条目中删除
//                                mHandler.obtainMessage(2, position).sendToTarget();
//                            } else {
//                                Utils.showToast(getActivity(), "请重试");
//                            }
//                        } catch (XmlPullParserException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//        }).start();
//    }

    private void showDialog() {
        if (Utils.isFastClick()) {
//            Utils.showToast(LoginActivity.this,"点击太过频繁");
            return ;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("是否确认收货?");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确认", (dialog, which) -> {
                //TODO 向服务器发起收货请求
                upDateServer();
                dialog.dismiss();
        });
        builder.show();
    }


    private void upDateServer() {
        WaitConfirmBean.RECORDBean recordBean = recordBeanList.get(currentPosition);
        String memid = recordBean.getMEMID();
        String formno = recordBean.getFORMNO();
        String address = recordBean.getADDRESS();
        List<WaitConfirmBean.RECORDBean.ORDERINFOBean> orderinfo = recordBean.getORDERINFO();
        String num = orderinfo.get(0).getNUM();
        Log.i("Undone","memid:"+memid+","+"formno:"+formno);
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, "0");
        params.put(Constant.ARG1,recordBean.getFORMNO());
        params.put(Constant.ARG2, address);
        params.put(Constant.ARG3, num);
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).confirmOrderNew(params).compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "s:"+s);
                String json = s.replace("<ns:confirmOrderNewResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:confirmOrderNewResponse>", "");
                Log.i(TAG,"json:"+json);
                LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                if ("1".equals(loginBean.getRESULT())) {
                    Utils.showToast(getActivity(), "已确认收货");
                  recordBeanList.remove(currentPosition);
                    undoneAdapter.notifyDataSetChanged();
                }
            }
        }, throwable -> {
            Log.i(TAG,"throwable:"+throwable);
        });
    }
//    {
//        String memid = recordBeanList.get(currentPosition).getMEMID();
//        String formno = recordBeanList.get(currentPosition).getFORMNO();
//        String address = recordBeanList.get(currentPosition).getADDRESS();
//        Log.i("Undone","memid:"+memid+","+"formno:"+formno);
//        final RequestBody requestBody = new  FormBody.Builder().add("arg0","2").add("arg1",formno).add("arg2",address).build();
//        final Request request = new Request.Builder().url(Constant.formatBASE_HOST("confirmOrderNew"))
//                .post(requestBody).build();
//        new Thread(() -> {
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        InputStream is = response.body().byteStream();
//                        try {
//                            String json = new Xml2Json(is).Pull2Xml();
//                            Log.i("Undone","json:"+json);
//                            LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
//                            if ("0".equals(loginBean.getRESULT())){
//                                Utils.showToast(getActivity(),"已确认收货");
//                                mHandler.obtainMessage(1,currentPosition).sendToTarget();
//                            }else{
//                                return;
//                            }
//                        } catch (XmlPullParserException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }).start();
//    }

}

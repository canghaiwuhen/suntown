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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.activity.OrderCenterActivity;
import com.suntown.activity.UnDoneOrderDetialActivity;
import com.suntown.activity.WaitPayOrderDetialActivity;
import com.suntown.adapter.UndoneAdapter;
import com.suntown.adapter.WaitPayAdapter;
import com.suntown.bean.LoginBean;
import com.suntown.bean.UserInfoBean;
import com.suntown.bean.WaitConfirmBean;
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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/8/18.
 */
public class UndoneFragment extends Fragment {
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
                undoneAdapter = new UndoneAdapter(getActivity(), recordBeanList);
                noDevice.setVisibility(View.GONE);
                lvFragment.setAdapter(undoneAdapter);
                undoneAdapter.notifyDataSetChanged();
                undoneAdapter.setOndoneAdapterCallBack(ondoneAdapterCallback);
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
        final String Memid =  ((OrderCenterActivity) getActivity()).memid;
        final String ssid =  ((OrderCenterActivity) getActivity()).ssid;
        getUserInfo(Memid,ssid);
        new Handler().postDelayed(() -> llLoad.setVisibility(View.GONE), 2000);
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
                getUserInfo(Memid,ssid);
                new Handler().postDelayed(() -> swipeRefresh.setRefreshing(false), 3000);
        });
    }

    private void getUserInfo(final String memid, final String ssid) {
        new Thread(() -> {
                FormBody body = new FormBody.Builder()
                        //待修改
//                     .add("arg0", bssid)
                        .add("arg0",ssid).add("arg1",memid)
                        .build();
                Request reques = new Request.Builder().post(body).url(Constant.BASE_HOST+"GetMemUsers").build();
                client.newCall(reques).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Utils.showToast(getActivity(),"请检查网络");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        String json;
                        try {
                            json = new Xml2Json(is).Pull2Xml();
                            UserInfoBean userInfoBean = new Gson().fromJson(json, UserInfoBean.class);
                            int rows = userInfoBean.getROWS();
                            if (rows>0){
                                List<UserInfoBean.RECORDBean> record = userInfoBean.getRECORD();
                                for (UserInfoBean.RECORDBean recordBean : record) {
                                    String memid1 = recordBean.getMEMID();
                                    memidList.add(memid1);
                                    Log.i("test",memidList.toString());
                                }
                                if (memidList!=null){
                                    for (int i = 0; i < memidList.size(); i++) {
                                        requestData(memidList.get(i));
                                    }
                                }
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
        }).start();
    }

    private void requestData(String memid) {
        RequestBody formBody = new FormBody.Builder()
                .add("arg0",memid)//TODO 待修改
//                .add("arg0", "1070")
                .add("arg1", "0")
                .build();
        final Request request = new Request.Builder()
                .url(Constant.BASE_HOST + "getHistoryOrder")
                .post(formBody)
                .build();
        new Thread(() -> {
            {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Utils.showToast(getActivity(),"网络连接失败，请检查网络");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        String json;
                        try {
                            json = new Xml2Json(is).Pull2Xml();
                            WaitConfirmBean waitConfirmBean = new Gson().fromJson(json, WaitConfirmBean.class);
                            String result = waitConfirmBean.getRESULT();
                            Log.i("test","waitConfirmBean:"+waitConfirmBean.toString());
                            if("0".equals(result)){
                                List<WaitConfirmBean.RECORDBean> record = waitConfirmBean.getRECORD();
                                if (record!=null){
                                    Message msg = new Message();
                                    msg.obj=record;
                                    msg.what=1;
                                    handler.sendMessage(msg);
                                }
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
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
        Log.i("deletePosition","deletePosition:"+position+",formno:"+formno);
        new Thread(() -> {
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
        }).start();
    }

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
        String memid = recordBeanList.get(currentPosition).getMEMID();
        String formno = recordBeanList.get(currentPosition).getFORMNO();
        String address = recordBeanList.get(currentPosition).getADDRESS();
        Log.i("Undone","memid:"+memid+","+"formno:"+formno);
        final RequestBody requestBody = new  FormBody.Builder().add("arg0","2").add("arg1",formno).add("arg2",address).build();
        final Request request = new Request.Builder().url(Constant.formatBASE_HOST("confirmOrder"))
                .post(requestBody).build();
        new Thread(() -> {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        try {
                            String json = new Xml2Json(is).Pull2Xml();
                            Log.i("Undone","json:"+json);
                            LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                            if ("0".equals(loginBean.getRESULT())){
                                Utils.showToast(getActivity(),"已确认收货");
                                mHandler.obtainMessage(1,currentPosition).sendToTarget();
                            }else{
                                return;
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }).start();
    }

}

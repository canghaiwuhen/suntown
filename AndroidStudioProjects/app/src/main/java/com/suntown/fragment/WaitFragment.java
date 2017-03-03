package com.suntown.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.activity.AddressCenterActivity;
import com.suntown.activity.DeviceListActivity;
import com.suntown.activity.WaitPayOrderDetialActivity;
import com.suntown.adapter.WaitPayAdapter;
import com.suntown.api.ApiService;
import com.suntown.bean.LoginBean;
import com.suntown.bean.UserInfoBean;
import com.suntown.bean.WaitConfirmBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.AlipayUtils;
import com.suntown.utils.Constant;
import com.suntown.utils.DateUtils;
import com.suntown.utils.PayResult;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
 * Created by Administrator on 2016/8/15.
 */
public class WaitFragment extends Fragment {

    private static final String TAG = "WaitFragment";
    private View inflate;
    private OkHttpClient client;
    public List<WaitConfirmBean.RECORDBean> recordBeanList = new ArrayList<>();
    private ListView lvFragment;
    private LinearLayout noOrder;
    private LinearLayout llLoad;
    private Button startDevice;
    public List<String> memidList = new ArrayList<>();
    private List<WaitConfirmBean.RECORDBean> record;
    private String gname;

    private WaitPayAdapter waitPayAdapter;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.order_fragment, container, false);
        initUi();
        return inflate;
    }

    private void initUi() {
        client = new OkHttpClient();
        final String ssid = SPUtils.getString(getActivity(), Constant.WIFI_SSID);
        Log.i(TAG, "ssid:" + ssid);
        final String memid = SPUtils.getString(getActivity(), Constant.MEMID);
        llLoad = (LinearLayout) inflate.findViewById(R.id.ll_load);
        lvFragment = (ListView) inflate.findViewById(R.id.lv_fragment);
        noOrder = (LinearLayout) inflate.findViewById(R.id.ll_no_device);
        startDevice = (Button) inflate.findViewById(R.id.btn_connDevice);
        waitPayAdapter = new WaitPayAdapter(getActivity(), recordBeanList);
        lvFragment.setAdapter(waitPayAdapter);
        waitPayAdapter.setOnWaitAdapterCallBack(onWaitAdapterCallBack);
        swipeRefresh = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefresh.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        swipeRefresh.setProgressBackgroundColor(R.color.colorWhite); // 设定下拉圆圈的背景
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        swipeRefresh.setOnRefreshListener(() -> {
            recordBeanList.clear();
            memidList.clear();
//            getUserInfo(ssid, memid);
            requestData(memid);
            waitPayAdapter.notifyDataSetChanged();
            new Handler().postDelayed(() -> {
                // 停止刷新
                swipeRefresh.setRefreshing(false);
            }, 3000); // 5秒后发送消息，停止刷新
        });

        startDevice.setOnClickListener(v -> startActivity(new Intent(getActivity(), DeviceListActivity.class)));
//        getUserInfo(ssid, memid);
        requestData(memid);
        new Handler().postDelayed(() -> llLoad.setVisibility(View.GONE), 2000);
    }

    private int currentPosition = 0;
    private String money;
    private int itemCliskPosition = 0;
    private int deletePosition;
    public WaitPayAdapter.OnWaitAdapterCallBack onWaitAdapterCallBack = new WaitPayAdapter.OnWaitAdapterCallBack() {
        @Override
        public void deleteItemClick(int position) {
            //向服务器发起删除请求  deleteOrder  FORMNO memid
            deletePosition = position;
            final String memid = recordBeanList.get(position).getMEMID();
            final String formno = recordBeanList.get(position).getFORMNO();
            Log.i("WaitFragment", "memid:" + memid + ",formno:" + formno);
            deleteOrder(memid, formno, position);
        }

        @Override
        public void buttonItemCLick(int position) {
            currentPosition = position;
            Log.i("WaitFragment", "currentPosition:" + currentPosition);
            // todo 调用支付宝付款请求  完成后和向服务器核对
            WaitConfirmBean.RECORDBean recordBean = recordBeanList.get(position);
            WaitConfirmBean.RECORDBean.ORDERINFOBean orderinfoBean = recordBean.getORDERINFO().get(0);
            gname = orderinfoBean.getGNAME();
            money = recordBean.getMONEY();
            String address = recordBean.getADDRESS();
            if (!Utils.isFastClick()) {
                Intent intent = new Intent(getActivity(), WaitPayOrderDetialActivity.class);
                intent.putExtra("recordBean", recordBean);
                intent.putExtra("position", itemCliskPosition);
                startActivityForResult(intent, 100);
            }

        }

        @Override
        public void onItemCLick(int position) {
            itemCliskPosition = position;
            //跳转到商品详情列表
            WaitConfirmBean.RECORDBean recordBean = recordBeanList.get(position);
            Intent intent = new Intent(getActivity(), WaitPayOrderDetialActivity.class);
            intent.putExtra("recordBean", recordBean);
            intent.putExtra("position", itemCliskPosition);
            startActivityForResult(intent, 100);
        }
    };

    //获取地址的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 150) {
            recordBeanList.remove(itemCliskPosition);
            waitPayAdapter.notifyDataSetChanged();
        }
    }

    //支付完成  将地址信息发送给服务器
//    private void getUserInfo(final String ssid, final String memid) {
//        //@"arg0":wifiID,@"arg1":memid
//        Map<String, String> params = new HashMap<>();
//        params.put(Constant.ARG0, ssid);
//        params.put(Constant.ARG1, memid);
//        String ip = Constant.BASE_HOST;
//        Retrofit retrofit = new Retrofit.Builder().
//                addConverterFactory(ScalarsConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
//        retrofit.create(ApiService.class).GetMemUsers(params).compose(RxSchedulers.io_main()).subscribe(s -> {
//            Log.i(TAG, "s:" + s);
//            String json = s.replace("<ns:GetMemUsersResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
//            json = json.replace("</ns:return></ns:GetMemUsersResponse>", "");
//            UserInfoBean userInfoBean = new Gson().fromJson(json, UserInfoBean.class);
//            int rows = userInfoBean.ROWS;
//            if (rows > 0) {
//                List<UserInfoBean.RECORDBean> record1 = userInfoBean.RECORD;
//                for (UserInfoBean.RECORDBean recordBean : record1) {
//                    String memid1 = recordBean.MEMID;
//                    memidList.add(memid1);
//                    Log.i("test", memidList.toString());
//                }
//                if (memidList != null) {
//                    for (int i = 0; i < memidList.size(); i++) {
//                        requestData(memidList.get(i));
//                    }
//                }
//            }
//        }, throwable -> {
//            Log.i(TAG, "throwable:" + throwable);
//        });
//    }

    private void requestData(String memid) {
        //TODO 同一WIFI 环境下所有订单
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, memid);
        params.put(Constant.ARG1, "4");
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
                waitPayAdapter.notifyDataSetChanged();
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

    //删除订单
    public void deleteOrder(final String memid, final String formno, final int position) {
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
                waitPayAdapter.notifyDataSetChanged();
            }else{
                Utils.showToast(getActivity(), "请重试");
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
        });
    }

}

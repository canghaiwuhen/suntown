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
import com.suntown.bean.LoginBean;
import com.suntown.bean.UserInfoBean;
import com.suntown.bean.WaitConfirmBean;
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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/15.
 */
public class WaitFragment extends Fragment{

    private View inflate;
    private OkHttpClient client;
    public List<WaitConfirmBean.RECORDBean> recordBeanList= new ArrayList<>();
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                record = (List<WaitConfirmBean.RECORDBean>) msg.obj;
                Log.i("test","record:"+ record);
                if (record ==null){
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
                waitPayAdapter = new WaitPayAdapter(getActivity(), recordBeanList);
                Log.i("test","list:"+record.size()+","+"recordList:"+recordBeanList.size());
                noOrder.setVisibility(View.GONE);
                lvFragment.setAdapter(waitPayAdapter);
                waitPayAdapter.notifyDataSetChanged();
                waitPayAdapter.setOnWaitAdapterCallBack(onWaitAdapterCallBack);
            }
            llLoad.setVisibility(View.GONE);
            if (msg.what==1){
                int deletePosition = msg.arg1;
                record.remove(deletePosition);
                waitPayAdapter.notifyDataSetChanged();
            }
        }
    };
    private ListView lvFragment;
    private LinearLayout noOrder;
    private LinearLayout llLoad;
    private Button startDevice;
    public List<String> memidList = new ArrayList<>();
    private List<WaitConfirmBean.RECORDBean> record;
    private String gname;
    private Handler mHandler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1: {
                        PayResult payResult = new PayResult((String) msg.obj);
                        /**
                         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                         * docType=1) 建议商户依赖异步通知
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
//                            Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                            Log.i("WaitFragment","支付成功:currentPosition:"+currentPosition);
                            uploadServer(currentPosition);
                        } else {
                            // 判断resultStatus 为非"9000"则代表可能支付失败
                            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                Toast.makeText(getActivity(), "支付结果确认中", Toast.LENGTH_SHORT).show();
                            } else {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    }
                    case 2:
                        int donePosostion = msg.arg1;
                        Utils.showToast(getActivity(),"支付成功，正在安排发货");
                        recordBeanList.remove(donePosostion);
                        waitPayAdapter.notifyDataSetChanged();
                        break;
                    case 3:
//                        int deletePosostion = msg.arg1;
                        recordBeanList.remove(deletePosition);
                        waitPayAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
    };
    private String returnAddress;
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
        final String memid = SPUtils.getString(getActivity(), Constant.MEMID);
        llLoad = (LinearLayout) inflate.findViewById(R.id.ll_load);
        lvFragment = (ListView) inflate.findViewById(R.id.lv_fragment);
        noOrder = (LinearLayout)inflate.findViewById(R.id.ll_no_device);
        startDevice = (Button)inflate.findViewById(R.id.btn_connDevice);
        swipeRefresh = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefresh.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        swipeRefresh.setProgressBackgroundColor(R.color.colorWhite); // 设定下拉圆圈的背景
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        swipeRefresh.setOnRefreshListener(() -> {
                recordBeanList.clear();
                memidList.clear();
                getUserInfo(ssid,memid);
                waitPayAdapter.notifyDataSetChanged();
                new Handler().postDelayed(() -> {
                    // 停止刷新
                    swipeRefresh.setRefreshing(false);
                }, 3000); // 5秒后发送消息，停止刷新
        });

        startDevice.setOnClickListener(v -> startActivity(new Intent(getActivity(), DeviceListActivity.class)));

        getUserInfo(ssid,memid);
        new Handler().postDelayed(() -> llLoad.setVisibility(View.GONE), 2000);
    }

    private int currentPosition =0;
    private String money;
    private int itemCliskPosition =0;
    private int deletePosition;
    public WaitPayAdapter.OnWaitAdapterCallBack onWaitAdapterCallBack = new WaitPayAdapter.OnWaitAdapterCallBack() {
        @Override
        public void deleteItemClick(int position) {
            //向服务器发起删除请求  deleteOrder  FORMNO memid
            deletePosition = position;
            final String memid = recordBeanList.get(position).getMEMID();
            final String formno = recordBeanList.get(position).getFORMNO();
            Log.i("WaitFragment","memid:"+memid+",formno:"+formno);
            deleteOrder(memid,formno,position);
        }

        @Override
        public void buttonItemCLick(int position) {
            currentPosition = position;
            Log.i("WaitFragment","currentPosition:"+currentPosition);
            // todo 调用支付宝付款请求  完成后和向服务器核对
            WaitConfirmBean.RECORDBean recordBean = recordBeanList.get(position);
            WaitConfirmBean.RECORDBean.ORDERINFOBean orderinfoBean = recordBean.getORDERINFO().get(0);
            gname = orderinfoBean.getGNAME();
            money = recordBean.getMONEY();
            String address = recordBean.getADDRESS();
            if (Utils.isFastClick()) {
//            Utils.showToast(LoginActivity.this,"点击太过频繁");
                return ;
            }
            if ("".equals(address)){
                Intent intent = new Intent(getActivity(), AddressCenterActivity.class);
                intent.putExtra("isWaitPay",true);
                startActivityForResult(intent,200);
            }else{
				Alipay();
//                uploadServer(position);
            }
        }

        @Override
        public void onItemCLick(int position) {
            itemCliskPosition = position;
            //跳转到商品详情列表
            WaitConfirmBean.RECORDBean recordBean = recordBeanList.get(position);
            Intent intent = new Intent(getActivity(), WaitPayOrderDetialActivity.class);
            intent.putExtra("recordBean",recordBean);
            intent.putExtra("position",itemCliskPosition);
            startActivityForResult(intent,100);
        }
    };
    //获取地址的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200 && resultCode==300){
            //TODO
//            Intent intent = getActivity().getIntent();
            returnAddress = data.getStringExtra(Constant.ADDRESS);
            if (!"".equals(returnAddress)) {
                Log.i("text", "address:" + returnAddress);
                Alipay();
//                uploadServer(currentPosition);
            }
        }
        if(requestCode == 100 && resultCode== 150) {
            recordBeanList.remove(itemCliskPosition);
            waitPayAdapter.notifyDataSetChanged();
        }
    }
    //支付完成  将地址信息发送给服务器
    private void Alipay() {
        String Alipaysfid = SPUtils.getString(getActivity(), Constant.ALIPAYSFID);
        String Alipaysfzh = SPUtils.getString(getActivity(), Constant.ALIPAYSFZH);
        String Alipaysfrsa = SPUtils.getString(getActivity(), Constant.ALIPAYSFRSA);

        if (TextUtils.isEmpty(Alipaysfid) || TextUtils.isEmpty(Alipaysfrsa) || TextUtils.isEmpty(Alipaysfzh)) {
            new AlertDialog.Builder(getActivity()).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", (dialog, which) -> {

                    }).show();
            return;
        }
        String orderInfo = AlipayUtils.getOrderInfo(gname, gname, money, Alipaysfid, Alipaysfzh);
//        String orderInfo = AlipayUtils.getOrderInfo(gname, gname, "0.01", Alipaysfid, Alipaysfzh);
        String sign = AlipayUtils.sign(orderInfo,Alipaysfrsa);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + AlipayUtils.getSignType();
        Runnable payRunnable = () -> {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(getActivity());
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    private void getUserInfo(final String ssid, final String memid) {
        //@"arg0":wifiID,@"arg1":memid
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
                                List<UserInfoBean.RECORDBean> record1 = userInfoBean.getRECORD();
                                for (UserInfoBean.RECORDBean recordBean : record1) {
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
        //TODO 同一WIFI 环境下所有订单
        Log.i("test","memid:"+memid);
        RequestBody formBody = new FormBody.Builder()
                .add("arg0",memid)//TODO 待修改
//                .add("arg0","1070")
                .add("arg1","4")
                .build();
        final Request request = new Request.Builder()
                .url(Constant.BASE_HOST+"getHistoryOrder")
                .post(formBody)
                .build();
        new Thread(() -> {
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
                            if("0".equals(result)){
                                List<WaitConfirmBean.RECORDBean> record1 = waitConfirmBean.getRECORD();
                                Message msg = new Message();
                                msg.obj= record1;
                                msg.what=1;
                                handler.sendMessage(msg);
                            }
//                            else{
//                                Message msg = new Message();
//                                msg.what=2;
//                                handler.sendMessage(msg);
//                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
        }).start();
    }
    //删除订单
    public void deleteOrder(final String memid, final String formno, final int position) {
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
                        Utils.showToast(getActivity(),"网络连接失败，请检查网络");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        String json;
                        try {
                            json = new Xml2Json(is).Pull2Xml();
                            LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                            if (loginBean.getRESULT().equals("0")){
                                Utils.showToast(getActivity(),"删除成功");
                                //TODO 从条目中删除
                                mHandler.obtainMessage(3,position).sendToTarget();
                            }else{
                                Utils.showToast(getActivity(),"请重试");
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }).start();
    }
    //付款成功，发送给服务器地址
    private void uploadServer(final int currposition) {
//        String formno = record.get(currposition).getFORMNO();
        String formno = recordBeanList.get(currentPosition).getFORMNO();
        Log.i("WaitFragment","formno:"+formno+"address:"+returnAddress);
        RequestBody body = new FormBody.Builder()
                //待修改
                .add("arg0","0").add("arg1",formno).add("arg2", returnAddress)
                .build();
//        confirmOrder?arg0=0&arg1=436275087546910&arg2=hangzhou
        final Request request = new Request.Builder().
                url(Constant.formatBASE_HOST("confirmOrder"))
                .post(body).build();
        new Thread(() -> {
                client.newCall(request).enqueue(new Callback() {
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
                            Log.i("Undone","json:"+json);
                            LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                            if ("0".equals(loginBean.getRESULT())){
                                mHandler.obtainMessage(2,currposition).sendToTarget();
                            }else{
//                                uploadServer(currposition);
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }).start();
    }

}

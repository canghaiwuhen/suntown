package com.suntown.scannerproject.input.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.suntown.scannerproject.R;
import com.suntown.scannerproject.input.InputAndOutputActivity;
import com.suntown.scannerproject.input.adapter.Check1Adapter;
import com.suntown.scannerproject.input.adapter.CheckAdapter;
import com.suntown.scannerproject.input.bean.BooleanBean;
import com.suntown.scannerproject.input.bean.PDBean;
import com.suntown.scannerproject.input.bean.PDDetailBean;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.Utils;
import com.suntown.scannerproject.utils.Xml2Bean;
import com.suntown.scannerproject.utils.XmlUtils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;


/**
 * Created by Administrator on 2016/10/28.
 */
public class CheckFragment extends Fragment {
    private static final int SCANNIN_GREQUEST_CODE = 1;
    private static final String TAG = "CheckFragment";
    private boolean isToggleOn = true;
    private View inflate;
    private LinearLayout llNormal;
    private TextView tvNum;
    private RelativeLayout rlTitle;
    private LinearLayout llMain;
    private RecyclerView lvItem;
    private String serverIP;
    private String checkNum ="";
    private OkHttpClient client;
    private String userId;
    private List<PDBean> pdBeanList = new ArrayList<>();
    private Check1Adapter checkAdapter;
    private static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    private static final String SCN_CUST_EX_SCODE = "scannerdata";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    checkNum = (String) msg.obj;
                    if ("".equals(checkNum)) {
                        //TODO 生成PDDH
                        productNum();
                    } else {
                        tvNum.setText(checkNum);
                    }
                    Log.i(TAG, "oddNum:" + checkNum);
                    break;
                //TODO 获取到盘点的商品明细信息 更新
                case 2:
                    llMain.setVisibility(View.VISIBLE);
                    rlTitle.setVisibility(View.VISIBLE);
                    PDDetailBean pdDetailBean = (PDDetailBean) msg.obj;
                    PDBean pdBean = new PDBean(pdDetailBean.PDID, pdDetailBean.SPDID, pdDetailBean.PDNO,
                            pdDetailBean.BARCODE, pdDetailBean.GOODSNAME, pdDetailBean.D4, pdDetailBean.D7, "", "",isToggleOn);
                    rlTitle.setVisibility(View.VISIBLE);
                    llNormal.setVisibility(View.GONE);
                    pdBeanList.add(0,pdBean);
                    checkAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    checkNum = (String) msg.obj;
                    tvNum.setText(checkNum);
                    break;
                case 4:
                    String xml = (String) msg.obj;
                    if ("1".equals(xml)) {
                        Utils.showToast(getActivity(), "盘点更新成功");
                        rlTitle.setVisibility(View.GONE);
                        llNormal.setVisibility(View.VISIBLE);
                        checkNum = "";
                        scanner.clear();
                        pdBeanList.clear();
                        checkAdapter.notifyDataSetChanged();
                        getNum();
                    } else {
                        Utils.showToast(getActivity(), "盘点更新失败,请确认后提交!");
                    }
                    break;
            }
        }
    };
    private String sid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.check_layout, container, false);
        initUi();
        return inflate;
    }


    private void initUi() {
        inflate.findViewById(R.id.rl_main).setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        serverIP = ((InputAndOutputActivity) getActivity()).serverIP;
        client = ((InputAndOutputActivity) getActivity()).client;
        userId = ((InputAndOutputActivity) getActivity()).userid;
        llNormal = (LinearLayout) inflate.findViewById(R.id.ll_normal);// 无数据布局
        llMain = (LinearLayout) inflate.findViewById(R.id.ll_main);//无数据隐藏
        tvNum = (TextView) inflate.findViewById(R.id.tv_num);//盘点单号
        lvItem = (RecyclerView) inflate.findViewById(R.id.lv_item);//listView
        rlTitle = (RelativeLayout) inflate.findViewById(R.id.rl_title);//无数据隐藏标题
        sid = SPUtils.getString(getActivity(), Constant.SID);
        llNormal.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        rlTitle.setVisibility(View.GONE);

        checkAdapter = new Check1Adapter( R.layout.check_item, pdBeanList);
        lvItem.setHasFixedSize(true);
        lvItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        lvItem.setAdapter(checkAdapter);
        inflate.findViewById(R.id.tg_choose).setOnClickListener(view -> {
            //更改 toggle状态
            isToggleOn = !isToggleOn;
            Log.i(TAG, "isToggleOn-" + isToggleOn);
            for (PDBean pdBean : pdBeanList) {
                pdBean.isToggle=isToggleOn;
            }
            checkAdapter.notifyDataSetChanged();
        });
        inflate.findViewById(R.id.tv_commit).setOnClickListener(view -> {
            //发送盘点
            String xml = XmlUtils.PDList2XML(pdBeanList, sid);
            sendPD(xml);
        });
        if ("".equals(checkNum)) {
            getNum();
        }else{
            tvNum.setText(checkNum);
        }
        checkAdapter.SetOnItemClickCallBack(position -> {
            PDBean pdBean = pdBeanList.get(position);
            pdBeanList.remove(position);
            scanner.remove(pdBean.BARCODE);
            checkAdapter.notifyDataSetChanged();
        });
    }


    List<String> scanner = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    /**
     * 返回盘点的商品明细信息
     */
    private void initData(String resultStr) {
        Log.i(TAG, "sid-" + sid + ",resultStr-" + resultStr + ", pdno-" + checkNum);
                //pdno 盘点单号
        RequestBody formBody = new FormBody.Builder().
                add(Constant.SID, sid).
                add("barcode", resultStr).
                add(Constant.PDNO, checkNum).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/axis2/services/STPdaService2/GetPDDetail")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String xml = response.body().string();
                    xml = xml.replace("<ns:GetPDDetailResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "").
                            replace("</ns:return></ns:GetPDDetailResponse>", "");
                    xml = xml.replaceAll("&lt;", "<");
                    xml = xml.replaceAll("&gt;", ">");
                    xml = xml.replaceAll("&#xd;", "");
                    Log.i(TAG, "xml-" + xml);
                    try {
                        PDDetailBean pdDetailBean = new Xml2Bean(xml).PullPDXML();
                        if (null ==pdDetailBean.GOODSNAME || null == pdDetailBean.BARCODE) {
                            Utils.showToast(getActivity(), "没有该货物信息");
                        } else {
                            Log.i(TAG, "pdDetailBean-" + pdDetailBean.toString());
                            Message msg = new Message();
                            msg.obj = pdDetailBean;
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                        Log.i(TAG, "解析xml失败");
                    }
                }
            });
        }).start();
    }


    /**
     * 获取盘点单号
     */
    private void getNum() {
        Log.i(TAG,"sid:"+sid+". serverIP:"+serverIP);
        RequestBody formBody = new FormBody.Builder().
                add(Constant.SID, sid).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/axis2/services/STPdaService2/GetLastestPDNO")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String xml = response.body().string();
                    xml = xml.replace("<ns:GetLastestPDNOResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                    xml = xml.replace("</ns:return></ns:GetLastestPDNOResponse>", "");
                    Log.i(TAG, "xml-" + xml);
                    Message msg = new Message();
                    msg.obj = xml;
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            });
        }).start();
    }

    /**
     * 生成盘点单号
     */
    private void productNum() {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.SID, sid)
                .add(Constant.USER_ID, userId)
                .build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/axis2/services/STPdaService2/StartPD")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String xml = response.body().toString();
                    xml = xml.replace("<ns:StartPDResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "").
                            replace("</ns:return></ns:StartPDResponse>", "");
                    Log.i(TAG, "xml-" + xml);
                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = xml;
                    handler.sendMessage(msg);
                }
            });
        }).start();
    }

    /**
     * 更新某个商品的仓库盘点数量和卖场盘点数量
     * @param xml
     */
    private void sendPD(String xml) {
        Log.i(TAG, "xml-" + xml);
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml)
                .build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/axis2/services/STPdaService2/UpdatePDDetails")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    result = result.replace("<ns:UpdatePDDetailsResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "").
                            replace("</ns:return></ns:UpdatePDDetailsResponse>", "");
                    Message msg = new Message();
                    msg.what = 4;
                    msg.obj = result;
                    handler.sendMessage(msg);
                }
            });
        }).start();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isSend=true;
            Log.i(TAG,"第三个input显示");
        }else{
            isSend=false;
            Log.i(TAG,"第三个input隐藏");
        }
    }


    private boolean isSend = false;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(SCN_CUST_EX_SCODE);
                //TODO
                if (isSend) {
                    if (!scanner.contains(message)) {
                        scanner.add(message);
                        Log.i(TAG, "resultStr-" + message);
                        if ("".equals(checkNum)){
                            getNum();
                        }else{
                            initData(message);
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }
}

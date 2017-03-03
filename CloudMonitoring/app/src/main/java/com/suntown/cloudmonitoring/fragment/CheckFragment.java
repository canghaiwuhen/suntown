package com.suntown.cloudmonitoring.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.CreamaActivity;
import com.suntown.cloudmonitoring.activity.InputAndOutputActivity;
import com.suntown.cloudmonitoring.adapter.CheckAdapter;
import com.suntown.cloudmonitoring.bean.BooleanBean;
import com.suntown.cloudmonitoring.bean.PDBean;
import com.suntown.cloudmonitoring.bean.PDDetailBean;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.utils.Xml2Bean;
import com.suntown.cloudmonitoring.utils.XmlUtils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


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
    private ListView lvItem;
    private String resultStr;
    private String serverIP;
    private String sid;
    private String checkNum ="";
    private OkHttpClient client;
    private String userId;
    private List<PDBean> pdBeanList = new ArrayList<>();
    private CheckAdapter checkAdapter;
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
                        SPUtils.put(getActivity(),Constant.CHECK_NUM,checkNum);
                    }
                    Log.i(TAG, "oddNum:" + checkNum);
                    break;
                //TODO 获取到盘点的商品明细信息 更新
                case 2:
                    llMain.setVisibility(View.VISIBLE);
                    rlTitle.setVisibility(View.VISIBLE);
                    PDDetailBean pdDetailBean = (PDDetailBean) msg.obj;
                    PDBean pdBean = new PDBean(pdDetailBean.PDID, pdDetailBean.SPDID, pdDetailBean.PDNO,
                            pdDetailBean.BARCODE, pdDetailBean.GOODSNAME, pdDetailBean.D4, pdDetailBean.D7, "", "");
                    rlTitle.setVisibility(View.VISIBLE);
                    llNormal.setVisibility(View.GONE);
                    pdBeanList.add(pdBean);
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
                        SPUtils.put(getActivity(),Constant.CHECK_NUM,"");
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.check_layout, container, false);
        initUi();
        return inflate;
    }

    BooleanBean booleanBean = new BooleanBean(isToggleOn);

    private void initUi() {
        checkAdapter = new CheckAdapter(getActivity(), pdBeanList, booleanBean);
        serverIP = ((InputAndOutputActivity) getActivity()).serverIP;
        sid = ((InputAndOutputActivity) getActivity()).sid;
        client = ((InputAndOutputActivity) getActivity()).client;
        userId = ((InputAndOutputActivity) getActivity()).userid;
        llNormal = (LinearLayout) inflate.findViewById(R.id.ll_normal);// 无数据布局
        llMain = (LinearLayout) inflate.findViewById(R.id.ll_main);//无数据隐藏
        tvNum = (TextView) inflate.findViewById(R.id.tv_num);//盘点单号
        lvItem = (ListView) inflate.findViewById(R.id.lv_item);//listView
        rlTitle = (RelativeLayout) inflate.findViewById(R.id.rl_title);//无数据隐藏标题

        lvItem.setAdapter(checkAdapter);
        llNormal.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        rlTitle.setVisibility(View.GONE);
        inflate.findViewById(R.id.fab_scanner).setOnClickListener(view -> {
            //TODO 跳转扫码界面
            String num = SPUtils.getString(getActivity(),Constant.CHECK_NUM);
            if (!"".equals(num)) {
                Log.i(TAG,"checkNum:"+num);
                Intent intent = new Intent(getActivity(), CreamaActivity.class);
                intent.putExtra(Constant.IS_ON_SCANN, true);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }else{
                getNum();
                Utils.showToast(getActivity(), "数据加载中，请稍后");
            }

        });
        inflate.findViewById(R.id.tg_choose).setOnClickListener(view -> {
            //更改 toggle状态
            isToggleOn = !isToggleOn;
            booleanBean.istoogleon = isToggleOn;
            Log.i(TAG, "isToggleOn-" + isToggleOn);
            checkAdapter.notifyDataSetChanged();
        });
        inflate.findViewById(R.id.tv_commit).setOnClickListener(view -> {
            //发送盘点
            String xml = XmlUtils.PDList2XML(pdBeanList, sid);
            Log.i(TAG,"xml:"+xml);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == -1) {
                    Bundle bundle = data.getExtras();
                    resultStr = bundle.getString(Constant.RESULT_CODE);
                    if (!scanner.contains(resultStr)) {
                        scanner.add(resultStr);
                        Log.i(TAG, "resultStr-" + resultStr);
                        initData();
                    }
                }
                break;
        }
    }

    /**
     * 返回盘点的商品明细信息
     */
    private void initData() {
        Log.i(TAG, "sid-" + sid + ",resultStr-" + resultStr + ", pdno-" + checkNum);
        RequestBody formBody = new FormBody.Builder().
                add(Constant.SID, sid).
                //pdno 盘点单号
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
     *
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
}

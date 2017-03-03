package com.suntown.scannerproject.input.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
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
import com.suntown.scannerproject.bean.Item2;
import com.suntown.scannerproject.input.InputAndOutputActivity;
import com.suntown.scannerproject.input.OutputCheckActivity;
import com.suntown.scannerproject.input.adapter.Note1Adapter;
import com.suntown.scannerproject.input.adapter.OutNumAdapter;
import com.suntown.scannerproject.input.bean.InOutBean;
import com.suntown.scannerproject.input.bean.InputBean;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.Utils;
import com.suntown.scannerproject.utils.Xml2Json;
import com.suntown.scannerproject.utils.XmlUtils;

import org.xmlpull.v1.XmlPullParserException;
import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
public class OutputFragment extends Fragment {
    private static final int SCANNIN_GREQUEST_CODE = 1;
    private static final String TAG = "OutputFragment";
    private View inflate;
    public List<InputBean> inputBeanList = new ArrayList<>();
    public List<String> stringList;
    private List<InOutBean> inOutBelist = new ArrayList<>();
    private Note1Adapter adapter;
    private OutNumAdapter outAdapter;
    private OkHttpClient client;
    private String serverIP;
    private String userId;
    private DbManager db;
    private LinearLayout llNormal;
    private LinearLayout llMain;
    private TextView tvNumTitle;
    private TextView tvNum;
    private RelativeLayout rlTitle;
    private RecyclerView lvItem;
    private ListView lvOldItem;
    private String oddNum ;
    private String sid;

    private static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    private static final String SCN_CUST_EX_SCODE = "scannerdata";
    private static final String SCN_CUST_ACTION_CANCEL = "android.intent.action.SCANNER_BUTTON_UP";
    private static final String SCN_CUST_ACTION_START = "android.intent.action.SCANNER_BUTTON_DOWN";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    oddNum = (String) msg.obj;
                    tvNum.setText(oddNum);
                    SPUtils.put(getActivity(), Constant.OUT_NUM,oddNum);
                    Log.i(TAG, "oddNum:" + oddNum);
                    break;
                case 2:
                    lvItem.setVisibility(View.VISIBLE);
                    rlTitle.setVisibility(View.VISIBLE);
                    llMain.setVisibility(View.VISIBLE);
                    llNormal.setVisibility(View.GONE);
                    tvNumTitle.setText("出库单号:");
                    inputBeanList = (List<InputBean>) msg.obj;
                    Log.i(TAG, "updateList:" + inputBeanList.toString());
                    adapter.notifyDataSetChanged();
                    break;
                case 3:
                    stringList.add(oddNum);
                    //TODO 保存数据库  并加入到集合
                    try {
                        for (InputBean inputBean : inputBeanList) {
                            InOutBean inOutBean = new InOutBean(oddNum,inputBean.Gname,inputBean.Barcode,
                                    2,inputBean.num,inputBean.boxNum,inputBean.Date,sid,userId);
                            db.save(inOutBean);
                            inOutBelist.add(inOutBean);
                        }
                        SPUtils.put(getActivity(),Constant.OUT_NUM,"");
                        oddNum="";
                        getNum();
                    } catch (DbException e) {
                        e.printStackTrace();
                        Utils.showToast(getActivity(), "保存数据库失败");
                    }
                    scanner.clear();
                    inputBeanList.clear();
                    Collections.sort(stringList, (s, t1) -> {
                        if (Integer.parseInt(s)>Integer.parseInt(t1)) {
                            return 1;
                        }
                        return -1;
                    });
                    adapter.notifyDataSetChanged();
                    outAdapter.notifyDataSetChanged();
                    rlTitle.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private boolean isSend = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.input_layout, container, false);
        stringList = new ArrayList<>();
        initUi();
        return inflate;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//    }




    private void initUi() {
        inflate.findViewById(R.id.rl_main).setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        outAdapter = new OutNumAdapter(getActivity(),stringList);
        client = ((InputAndOutputActivity) getActivity()).client;
        serverIP = ((InputAndOutputActivity) getActivity()).serverIP;
        userId = ((InputAndOutputActivity) getActivity()).userid;
        sid = SPUtils.getString(getActivity(), Constant.SID);
        db = ((InputAndOutputActivity) getActivity()).db;
        //名称  单号
        llNormal = (LinearLayout) inflate.findViewById(R.id.ll_normal);
        llMain = (LinearLayout) inflate.findViewById(R.id.ll_main);
        tvNumTitle = (TextView) inflate.findViewById(R.id.tv_num_title);
        tvNum = (TextView) inflate.findViewById(R.id.tv_num);
        rlTitle = (RelativeLayout) inflate.findViewById(R.id.rl_title);
        inflate.findViewById(R.id.tv_delete).setVisibility(View.GONE);
        //扫描数据
        lvItem = (RecyclerView) inflate.findViewById(R.id.lv_item);
        //历史数据
        lvOldItem = (ListView) inflate.findViewById(R.id.lv_old_item);
        lvItem.setHasFixedSize(true);
        lvItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Note1Adapter(R.layout.input_item,inputBeanList);
        lvItem.setAdapter(adapter);
        //
        lvOldItem.setAdapter(outAdapter);
        llNormal.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);

//        inflate.findViewById(R.id.fab_saoyisao).setOnClickListener(view -> {
////            String s = tvNumTitle.getText().toString();
////            isSend=true;
//            String num = SPUtils.getString(getActivity(),Constant.OUT_NUM);
//            if(!"".equals(num)) {
//                Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> {
//                    Intent scannerIntent = new Intent(SCN_CUST_ACTION_START);
//                    getActivity().sendBroadcast(scannerIntent);
//                });
//            }else{
//                getNum();
//                Utils.showToast(getActivity(), "数据加载中，请稍后");
//            }
//        });

        //TODO 点击跳转
        lvOldItem.setOnItemClickListener((adapterView, view, i, l) -> {
            Log.i(TAG,"inOutBelist-"+inOutBelist.toString());
            String s = stringList.get(i);
            Intent intent = new Intent(getActivity(), OutputCheckActivity.class);
            intent.putParcelableArrayListExtra(Constant.INOUTBEAN_LIST, (ArrayList<? extends Parcelable>) inOutBelist);
            intent.putExtra(Constant.STRING,s);
            startActivityForResult(intent,200);
        });

        //点击删除条目
        adapter.SetOnItemClickCallBack(position -> {
            InputBean inputBean = inputBeanList.get(position);
            inputBeanList.remove(position);
                scanner.remove(inputBean.Barcode);
                adapter.notifyDataSetChanged();
                if (inputBeanList.size()==0) {
                    rlTitle.setVisibility(View.GONE);
                }
        });

        inflate.findViewById(R.id.tv_commit).setOnClickListener(view -> {
            //TODO  出库提交
            if (inputBeanList.size()>0) {
                String xml = XmlUtils.List2Xml2(inputBeanList, userId, oddNum, sid);
                Log.i(TAG,"xml-"+xml);
                commitInStore(xml);
            }else{
                Utils.showToast(getActivity(),"数据不能为空");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        getActivity().registerReceiver(receiver, intentFilter);
        oddNum = SPUtils.getString(getActivity(),Constant.OUT_NUM);
        if ("".equals(oddNum)){
            getNum();
        }else{
            tvNum.setText(oddNum);
        }
        //TODO 数据库取出数据
        inOutBelist = queryDataBase();
        if (null != inOutBelist && 0 <inOutBelist.size()) {
            Log.i(TAG,"inOutBeanList-"+inOutBelist.toString());
            for (InOutBean inOutBean : inOutBelist) {
                String orderNum = inOutBean.orderNum;
                Log.i(TAG,"orderNum-"+orderNum);
                if (!stringList.contains(orderNum)){
                    stringList.add(orderNum);
                }
            }
            llNormal.setVisibility(View.GONE);
            llMain.setVisibility(View.VISIBLE);
            lvOldItem.setVisibility(View.VISIBLE);
            outAdapter.notifyDataSetChanged();
        }else{
            llNormal.setVisibility(View.VISIBLE);
        }
        if (null!=stringList) {
            Log.i(TAG,"stringList:"+stringList);
            Collections.sort(stringList, (s, t1) -> {
                if (Integer.parseInt(s)>Integer.parseInt(t1)) {
                    return 1;
                }
                return -1;
            });
        }
    }
    private void initData(String resultStr) {
        Log.i(TAG, "sid:" + sid + ",resultStr" + resultStr);
        if ("".equals(resultStr)) {
            llNormal.setVisibility(View.VISIBLE);
            llMain.setVisibility(View.GONE);
        } else {
            if (!resultStr.contains(".")) {
                //TODO 查询服务器 获取出库单号，商品详情
                getGoodsDatial(resultStr);
            } else {
                Utils.showToast(getActivity(), "请扫描商品条码");
            }
        }
    }
    /**
     * 获取商品详情
     */
    private void getGoodsDatial(String goodsCode) {
        RequestBody formBody = new FormBody.Builder().
//                add(Constant.BARCODE, goodsCode).
//                add(Constant.SID, Sid).build();
                add("arg1", goodsCode).
                add("arg0", sid).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/axis2/services/STPdaService2/GetGoodsInfo3")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String xml = response.body().string();
                        xml = xml.replace("<ns:GetGoodsInfo3Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                        xml = xml.replace("</ns:return></ns:GetGoodsInfo3Response>", "");
                        xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
//                        List<ShopXmlBean> shopXmlBeanList = null;
//                        shopXmlBeanList = new Xml2Json(xml).PullXml();
//                        Log.i(TAG, shopXmlBeanList.toString());
                        Item2 item2 = new Xml2Json(xml).PullGoodsXml();
                        Log.i(TAG, item2.toString());
                        String gName = item2.GName;
                        String barcode = item2.Barcode;
                        if (null!=(gName) && null!=barcode) {
                            String time = Utils.Time();
                            Log.i(TAG, "barcode:" + barcode + ",+gName:" + gName + ",time:" + time);
                            InputBean bean = new InputBean(barcode, gName, "", "", time);
                            if (!inputBeanList.contains(bean)) {
                                inputBeanList.add(0,bean);
                            }
                            Message msg = new Message();
                            msg.obj = inputBeanList;
                            msg.what = 2;
                            handler.sendMessage(msg);
                        } else {
                            Utils.showToast(getActivity(), "尚无此商品信息");
                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }

                }
            });
        }).start();
    }
    /**
     * 出库
     * @param xml
     */
    private void commitInStore(String xml) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/axis2/services/STPdaService2/GoodsOutStore")
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
                    xml = xml.replace("<ns:GoodsOutStoreResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>","");
                    xml = xml.replace("</ns:return></ns:GoodsOutStoreResponse>","");
                    if ("1".equals(xml)){
                        Utils.showToast(getActivity(),"提交成功");
                        Message msg = new Message();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }else {
                        Utils.showToast(getActivity(),"提交失败");
                    }
                }
            });
        }).start();
    }

    /**
     * 查询数据库
     * @return
     */
    private List<InOutBean> queryDataBase() {
        List<InOutBean> list ;
        List<InOutBean> inOutBeanList = new ArrayList<>();
        try {
            list = db.selector(InOutBean.class).orderBy("orderNum").findAll();
            if (list !=null&&list.size()>0) {
                Log.i(TAG,"list:"+list.toString());
                for (InOutBean inOutBean : list) {
                    if (inOutBean.moudleName==2 && sid.equals(inOutBean.sid)&&userId.equals(inOutBean.userId)) {
                        inOutBeanList.add(inOutBean);
                    }
                }
            }
            return inOutBeanList;
        } catch (DbException e) {
            e.printStackTrace();
            Log.i(TAG,"查询失败");
        }
        return inOutBeanList;
    }

    /**
     * 获取出库单号
     */
    private void getNum() {
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/axis2/services/STPdaService2/GetGoodsOutSheetNo")
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String xml = response.body().string();
                    xml = xml.replace("<ns:GetGoodsOutSheetNoResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "").
                            replace("</ns:return></ns:GetGoodsOutSheetNoResponse>", "");
                    if ("" != xml) {
                        Message msg = new Message();
                        msg.obj = xml;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }
                @Override
                public void onFailure(Call call, IOException e) {
                }

            });
        }).start();
    }

    List<String> scanner = new ArrayList<>();
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 200:
                if (resultCode==300){
                    String num = data.getStringExtra(Constant.NUM);
                    Log.i(TAG,"num:"+num);
                    if (!"".equals(num)){
                        Log.i(TAG,"num:"+num);
                        stringList.remove(num);
                        if (stringList.size()==0) {
                            llNormal.setVisibility(View.VISIBLE);
                        }
                        outAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isSend=true;
            Log.i(TAG,"第二个input显示");
        }else{
            isSend=false;
            Log.i(TAG,"第二个input隐藏");
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(SCN_CUST_EX_SCODE);
                //TODO
                if (isSend) {
//                    isSend = !isSend;
                    if(!"".equals(oddNum)){
                        if (!scanner.contains(message)) {
                            scanner.add(message);
                            Log.i(TAG, "message-" + message);
                            initData(message);
                        }
                    }else{
                        getNum();
                        Utils.showToast(getActivity(), "数据加载中，请稍后");
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

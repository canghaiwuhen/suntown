package com.suntown.cloudmonitoring.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
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
import com.suntown.cloudmonitoring.activity.OutputCheckActivity;
import com.suntown.cloudmonitoring.adapter.NoteAdapter;
import com.suntown.cloudmonitoring.adapter.OutNumAdapter;
import com.suntown.cloudmonitoring.bean.InOutBean;
import com.suntown.cloudmonitoring.bean.InputBean;
import com.suntown.cloudmonitoring.bean.Item2;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.utils.Xml2Json;
import com.suntown.cloudmonitoring.utils.XmlUtils;

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
    private NoteAdapter adapter;
    private OutNumAdapter outAdapter;
    private OkHttpClient client;
    private String serverIP;
    private String userId;
    private String Sid;
    private DbManager db;
    private LinearLayout llNormal;
    private LinearLayout llMain;
    private TextView tvNumTitle;
    private TextView tvNum;
    private RelativeLayout rlTitle;
    private ListView lvItem;
    private ListView lvOldItem;
    private String oddNum ;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    oddNum = (String) msg.obj;
                    tvNum.setText(oddNum);
                    SPUtils.put(getActivity(),Constant.OUT_NUM,oddNum);
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
                                    2,inputBean.num,inputBean.boxNum,inputBean.Date,Sid,userId);
                            db.save(inOutBean);
                            Log.i(TAG,"保存数据库成功");
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
    private String resultStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.input_layout, container, false);
        stringList = new ArrayList<>();
        initUi();
        return inflate;
    }

    private void initUi() {
        adapter = new NoteAdapter(getActivity(), inputBeanList);
        outAdapter = new OutNumAdapter(getActivity(),stringList);
        client = ((InputAndOutputActivity) getActivity()).client;
        serverIP = ((InputAndOutputActivity) getActivity()).serverIP;
        userId = ((InputAndOutputActivity) getActivity()).userid;
        Sid = ((InputAndOutputActivity) getActivity()).sid;
        db = ((InputAndOutputActivity) getActivity()).db;
        //名称  单号
        llNormal = (LinearLayout) inflate.findViewById(R.id.ll_normal);
        llMain = (LinearLayout) inflate.findViewById(R.id.ll_main);
        tvNumTitle = (TextView) inflate.findViewById(R.id.tv_num_title);
        tvNum = (TextView) inflate.findViewById(R.id.tv_num);
        rlTitle = (RelativeLayout) inflate.findViewById(R.id.rl_title);
        inflate.findViewById(R.id.tv_delete).setVisibility(View.GONE);
        //扫描数据
        lvItem = (ListView) inflate.findViewById(R.id.lv_item);
        //历史数据
        lvOldItem = (ListView) inflate.findViewById(R.id.lv_old_item);
        lvItem.setAdapter(adapter);
        //
        lvOldItem.setAdapter(outAdapter);
        llNormal.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);

        inflate.findViewById(R.id.fab_saoyisao).setOnClickListener(view -> {
//            String s = tvNumTitle.getText().toString();
            String num = SPUtils.getString(getActivity(),Constant.OUT_NUM);
            if(!"".equals(num)) {
                Intent intent = new Intent(getActivity(), CreamaActivity.class);
                intent.putExtra(Constant.IS_ON_SCANN, true);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }else{
                getNum();
                Utils.showToast(getActivity(), "数据加载中，请稍后");
            }
        });

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
                String xml = XmlUtils.List2Xml2(inputBeanList, userId, oddNum, Sid);
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
        //TODO 数据库取出数据
        inOutBelist = queryDataBase();
        oddNum = SPUtils.getString(getActivity(),Constant.OUT_NUM);
        if ("".equals(oddNum)){
            getNum();
        }else{
            tvNum.setText(oddNum);
        }
        if (null != inOutBelist && 0 <inOutBelist.size()) {
            Log.i(TAG,"inOutBeanList-"+inOutBelist.toString());
            for (InOutBean inOutBean : inOutBelist) {
                String orderNum = inOutBean.orderNum;
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
        Collections.sort(stringList, (s, t1) -> {
            if (Integer.parseInt(s)>Integer.parseInt(t1)) {
                return 1;
            }
            return -1;
        });
    }
    private void initData() {
        Log.i(TAG, "sid:" + Sid + ",resultStr" + resultStr);
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
                add("arg0", Sid).build();
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
                            InputBean bean = new InputBean(barcode, gName, "2", "1", time);
                            if (!inputBeanList.contains(bean)) {
                                inputBeanList.add(bean);
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
                    if (inOutBean.moudleName==2 && Sid.equals(inOutBean.sid)&&userId.equals(inOutBean.userId)) {
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
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == -1) {
                    Bundle bundle = data.getExtras();
                    resultStr = bundle.getString(Constant.RESULT_CODE);
                    if (!scanner.contains(resultStr)){
                        scanner.add(resultStr);
                        Log.i(TAG,"resultStr-"+resultStr);
                        initData();
                    }
                }
                break;
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
}

package com.suntown.scannerproject.input;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntown.scannerproject.R;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.base.BaseApplication;
import com.suntown.scannerproject.bean.Item2;
import com.suntown.scannerproject.bean.ShopXmlBean;
import com.suntown.scannerproject.input.adapter.Note1Adapter;
import com.suntown.scannerproject.input.bean.InOutBean;
import com.suntown.scannerproject.input.bean.InputBean;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.Utils;
import com.suntown.scannerproject.utils.Xml2Json;
import com.suntown.scannerproject.utils.XmlUtils;
import com.suntown.scannerproject.weight.LoadingDialog;

import org.xmlpull.v1.XmlPullParserException;
import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InputCheckActivity extends BaseActivity {

    private static final String TAG = "InputCheckActivity";
    private static final int SCANNIN_GREQUEST_CODE = 1;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.lv_item)
    RecyclerView lvItem;
    public List<InputBean> inputBeanList;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_num_title)
    TextView tvNumTitle;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.main)
    LinearLayout main;
    private Note1Adapter adapter;
    private OkHttpClient client;
    private String sid;
    private String serverIP;
    private DbManager db;
    private String orderNum;
    private String userId;
    boolean isClear = false;
    private LinearLayout llNormal;
    private String str;
    private static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    private static final String SCN_CUST_EX_SCODE = "scannerdata";
    private static final String SCN_CUST_ACTION_CANCEL = "android.intent.action.SCANNER_BUTTON_UP";
    private static final String SCN_CUST_ACTION_START = "android.intent.action.SCANNER_BUTTON_DOWN";
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_check);
        ButterKnife.bind(this);
        llNormal = (LinearLayout) findViewById(R.id.ll_normal);
        tvNumTitle.setText("入库单号");
        tvShopName.setText("入库管理");
        dialog = new LoadingDialog(this);
        IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        registerReceiver(receiver, intentFilter);
        client = new OkHttpClient();
        serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        userId = SPUtils.getString(this, Constant.USER_CODE);
        if ("".equals(serverIP)) {
            serverIP = ApiConstant.BASE_URL;
        }
        main.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });
        
        db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
        Intent intent = getIntent();
        List<InOutBean> inOutBelist = intent.getParcelableArrayListExtra(Constant.INOUTBEAN_LIST);
        str = intent.getStringExtra(Constant.STRING);
        Log.i(TAG, "inOutBelist-" + inOutBelist.toString());
        inputBeanList = new ArrayList<>();
        for (InOutBean inOutBean : inOutBelist) {
            orderNum = inOutBean.orderNum;
            sid = inOutBean.sid;
            userId = inOutBean.userId;
            String gname = inOutBean.gname;
            if (str.equals(orderNum)) {
                String barcode = inOutBean.barcode;
                InputBean bean = new InputBean(barcode, gname, inOutBean.boxNum, inOutBean.goodsNum, inOutBean.puductDate);
                inputBeanList.add(bean);
                scanner.add(barcode);
            }
            this.scanner.add(gname);
        }
        adapter = new Note1Adapter(R.layout.input_item, inputBeanList);
        tvNum.setText(str);
        lvItem.setHasFixedSize(true);
        lvItem.setLayoutManager(new LinearLayoutManager(this));
        lvItem.setAdapter(adapter);
        adapter.SetOnItemClickCallBack(position -> {
            InputBean inputBean = inputBeanList.get(position);
            String barcode = inputBean.Barcode;
            InOutBean outBean = null;
            try {
                Log.i(TAG," barcode:"+barcode+" sid:"+sid+" userId:"+userId);
                List<InOutBean> moudleName = db.selector(InOutBean.class).where("barcode", "=", barcode).where("moudleName", "=", "1").
                        where("sid", "=", sid).where("userId", "=", userId).findAll();
                Log.i(TAG,moudleName.toString());
//                List<InOutBean> moudleName = db.selector(InOutBean.class).where("moudleNme", "=", "1").findAll();
                for (InOutBean inOutBean : moudleName) {
                    if (barcode.equals(inOutBean.barcode)) {
                        db.delete(inOutBean);
                    }
                }
//                db.delete(InOutBean.class, WhereBuilder.b("orderNum", "=", str));
            } catch (DbException e) {
                e.printStackTrace();
            }
            inputBeanList.remove(position);
            if (0 == inputBeanList.size() || null == inputBeanList) {
                isClear=true;
                rlTitle.setVisibility(View.GONE);
                llNormal.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
        });

    }

    @OnClick({R.id.iv_back, R.id.tv_delete, R.id.tv_commit})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                intent = new Intent();
                if (isClear) {
                    intent.putExtra(Constant.NUM, str);
                } else {
                    intent.putExtra(Constant.NUM, "");
                }
                setResult(300, intent);
                finish();
                break;
            case R.id.tv_delete:
                try {
                    db.delete(InOutBean.class, WhereBuilder.b("orderNum", "=", str));
                    rlTitle.setVisibility(View.GONE);
                    inputBeanList.clear();
                    llNormal.setVisibility(View.VISIBLE);
                    isClear = true;
                    adapter.notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_commit:
                //TODO 提交审核
                if (inputBeanList.size() > 0) {
                    String xml = XmlUtils.List2Xml(inputBeanList, userId, str, sid);
                    Log.i(TAG, "xml-" + xml);
                    commitInStore(xml);
                } else {
                    Utils.showToast(this, "数据不能为空");
                }
                break;
        }
    }

    List<String> scanner = new ArrayList<>();

    /**
     * 审核提交
     *
     * @param xml
     */
    private void commitInStore(String xml) {
        dialog.show();
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/axis2/services/STPdaService2/GoodsInCommit")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> dialog.dismiss());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String xml = response.body().string();
                    Log.i(TAG, "xml-" + xml);
                    xml = xml.replace("<ns:GoodsInCommitResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                    Log.i(TAG, "xml-" + xml);
                    xml = xml.replace("</ns:return></ns:GoodsInCommitResponse>", "");
                    Log.i(TAG, "xml-" + xml);
                    if ("1".equals(xml)) {
                        Utils.showToast(InputCheckActivity.this, "提交成功");
                        //TODO 删除数据
                        db.delete(InOutBean.class, WhereBuilder.b("orderNum", "=", str));
                        runOnUiThread(() -> {
                            dialog.dismiss();
                            isClear = true;
                            inputBeanList.clear();
                            rlTitle.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        });
                    } else {
                        runOnUiThread(() -> dialog.dismiss());
                        Utils.showToast(InputCheckActivity.this, "提交失败");
                    }
                }
            });
        }).start();
    }

    private void getGoodsDatial(String resultStr) {
        Log.i(TAG, "goodsCode:" + resultStr + " sid:" + sid);
        RequestBody formBody = new FormBody.Builder().
                add(Constant.ARG1, resultStr).
                add(Constant.ARG0, sid).build();
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
                        Log.i(TAG, "xml-" + xml);
                        List<ShopXmlBean> shopXmlBeanList = null;
                        Item2 item2 = new Xml2Json(xml).PullGoodsXml();
                        String gName1 = item2.GName;
                        String barcode = item2.Barcode;
                        if (null != (gName1) && null != barcode) {
                            String time = Utils.Time();
//                            ShopXmlBean shopXmlBean = shopXmlBeanList.get(0);
//                            Log.i(TAG, shopXmlBean.toString());
//                            String barcode = shopXmlBean.Barcode;
//                            String gName = shopXmlBean.GName;
                            Log.i(TAG, "barcode:" + barcode + ",+gName:" + gName1 + ",time:" + time);
                            InputBean bean = new InputBean(barcode, gName1, "1", "1", time);
                            if (!inputBeanList.contains(bean)) {
                                inputBeanList.add(0,bean);
                            }
                            runOnUiThread(() -> adapter.notifyDataSetChanged());
                        } else {
                            Utils.showToast(InputCheckActivity.this, "尚无此商品信息");
                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }

                }
            });
        }).start();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(SCN_CUST_EX_SCODE);
                //TODO
                if (!isClear) {
                    if (!scanner.contains(message)) {
                        scanner.add(message);
                        if (!message.contains(".")) {
                            Log.i(TAG, "resultStr-" + message);
                            //TODO 查询服务器 商品详情
                            getGoodsDatial(message);
                        } else {
                            Utils.showToast(InputCheckActivity.this, "请扫描商品条码");
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (isClear) {
            intent.putExtra(Constant.NUM, str);
        } else {
            intent.putExtra(Constant.NUM, "");
        }
        setResult(300, intent);
        finish();
    }

    // 点击空白区域 自动隐藏软键盘
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}

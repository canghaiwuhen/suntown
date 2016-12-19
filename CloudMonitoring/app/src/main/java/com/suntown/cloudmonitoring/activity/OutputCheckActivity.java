package com.suntown.cloudmonitoring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.adapter.NoteAdapter;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.base.BaseApplication;
import com.suntown.cloudmonitoring.bean.InOutBean;
import com.suntown.cloudmonitoring.bean.Item2;
import com.suntown.cloudmonitoring.bean.ShopXmlBean;
import com.suntown.cloudmonitoring.bean.inputBean;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.utils.Xml2Json;
import com.suntown.cloudmonitoring.utils.XmlUtils;

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

public class OutputCheckActivity extends BaseActivity {

    private static final String TAG = "OutputCheckActivity";
    private static final int SCANNIN_GREQUEST_CODE = 1;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.lv_item)
    ListView lvItem;
    public List<inputBean> inputBeanList;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    private NoteAdapter adapter;
    private OkHttpClient client;
    private String sid;
    private String userid;
    private String serverIP;
    private DbManager db;
    private String orderNum;
    private String userId;
    boolean isClear = false;
    private LinearLayout llNormal;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_check);
        llNormal = (LinearLayout) findViewById(R.id.ll_normal);
        findViewById(R.id.fab_saoyisao).setOnClickListener(view -> {
            Intent intent = new Intent(OutputCheckActivity.this, CreamaActivity.class);
            intent.putExtra(Constant.IS_ON_SCANN, true);
            startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
        });
        ButterKnife.bind(this);
        client = new OkHttpClient();
        serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverIP)) {
            userid = SPUtils.getString(this, Constant.USER_ID);
            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        } else {
            userid = SPUtils.getString(this, Constant.SUB_USER_ID);
        }
        db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
        Intent intent = getIntent();
        List<InOutBean> inOutBelist = intent.getParcelableArrayListExtra(Constant.INOUTBEAN_LIST);
        str = intent.getStringExtra(Constant.STRING);
        Log.i(TAG,"inOutBelist-"+inOutBelist.toString());
        inputBeanList = new ArrayList<>();
        for (InOutBean inOutBean : inOutBelist) {
            orderNum = inOutBean.orderNum;
            sid = inOutBean.sid;
            userId = inOutBean.userId;
            String gname = inOutBean.gname;
            if (str.equals(orderNum)){
                String barcode = inOutBean.barcode;
                inputBean bean = new inputBean(barcode, gname, inOutBean.boxNum, inOutBean.goodsNum, inOutBean.puductDate);
                inputBeanList.add(bean);
                scanner.add(barcode);
            }
            this.scanner.add(gname);
        }
        adapter = new NoteAdapter(this, inputBeanList);
        tvNum.setText(str);
        lvItem.setAdapter(adapter);
        adapter.SetOnItemClickCallBack(position -> {
                inputBean inputBean = inputBeanList.get(position);
                String barcode = inputBean.Barcode;
                InOutBean outBean = null;
                try {
//                    outBean = db.selector(InOutBean.class).where("barcode", "=", barcode).where("moudleName", "=", "2").
//                            where("sid", "=", sid).where("userId", "=", userid).findFirst();
//                    db.delete(outBean);
                    List<InOutBean> moudleName = db.selector(InOutBean.class).where("moudleNme", "=", "2").findAll();
                    for (InOutBean inOutBean : moudleName) {
                        if (barcode.equals(inOutBean.barcode)) {
                            db.delete(outBean);
                        }
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                inputBeanList.remove(position);
                adapter.notifyDataSetChanged();
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_delete, R.id.tv_commit,R.id.iv_scanner})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                intent = new Intent();
                if (isClear){
                    intent.putExtra(Constant.NUM,str);
                }else{
                    intent.putExtra(Constant.NUM,"");
                }
                setResult(300,intent);
                finish();
                break;
            case R.id.tv_delete:
                try {
                    db.delete(InOutBean.class, WhereBuilder.b("orderNum", "=", str));
                    rlTitle.setVisibility(View.GONE);
                    inputBeanList.clear();
                    llNormal.setVisibility(View.VISIBLE);
                    isClear=true;
                    adapter.notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_commit:
                //TODO 提交审核
                if (inputBeanList.size()>0) {
                    String xml = XmlUtils.List2Xml(inputBeanList, userId, str, sid);
                    Log.i(TAG,"xml-"+xml);
                    commitInStore(xml);
                }else{
                    Utils.showToast(this,"数据不能为空");
                }
                break;
            case R.id.iv_scanner:
                intent = new Intent(OutputCheckActivity.this, CreamaActivity.class);
                intent.putExtra(Constant.IS_ON_SCANN, true);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
        }
    }

    List<String> scanner = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String resultStr = bundle.getString(Constant.RESULT_CODE);
                    if (!isClear){
                        if (!scanner.contains(resultStr)) {
                            scanner.add(resultStr);
                            if (!resultStr.contains(".")) {
                                Log.i(TAG,"resultStr-"+resultStr);
                                //TODO 查询服务器 商品详情
                                getGoodsDatial(resultStr);
                            } else {
                                Utils.showToast(OutputCheckActivity.this, "请扫描商品条码");
                            }
                        }
                    }
                }
        }
    }

    /**
     * 审核提交
     * @param xml
     */
    private void commitInStore(String xml) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/axis2/services/STPdaService2/GoodsOutCommit")
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
                    Log.i(TAG,"xml-"+xml);
                    xml = xml.replace("<ns:GoodsOutCommitResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>","");
                    Log.i(TAG,"xml-"+xml);
                    xml = xml.replace("</ns:return></ns:GoodsOutCommitResponse>","");
                    Log.i(TAG,"xml-"+xml);
                    if ("1".equals(xml)){
                        Utils.showToast(OutputCheckActivity.this,"提交成功");
                        //TODO 删除数据
                        db.delete(InOutBean.class, WhereBuilder.b("orderNum", "=", str));
                        runOnUiThread(() -> {
                            isClear=true;
                            inputBeanList.clear();
                            rlTitle.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        });
                    }else {
                        Utils.showToast(OutputCheckActivity.this,"提交失败");
                    }
                }
            });
        }).start();
    }
    private void getGoodsDatial(String resultStr) {
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
                        Log.i(TAG,"xml-"+xml);
//                        List<ShopXmlBean> shopXmlBeanList = null;
//                        shopXmlBeanList = new Xml2Json(xml).PullXml();
//                        Log.i(TAG, shopXmlBeanList.toString());
//                        if (shopXmlBeanList != null && shopXmlBeanList.size() != 0) {
//                            ShopXmlBean shopXmlBean = shopXmlBeanList.get(0);
//                            Log.i(TAG, shopXmlBean.toString());
//                            String barcode = shopXmlBean.Barcode;
//                            String gName = shopXmlBean.GName;
                        Item2 item2 = new Xml2Json(xml).PullGoodsXml();
                        Log.i(TAG, "item2:"+item2.toString());
                        String gName1 = item2.GName;
                        String barcode = item2.Barcode;
                        if (null!=(gName1) && null!=barcode) {
                            String time = Utils.Time();
                            Log.i(TAG, "barcode:" + barcode + ",+gName:" + gName1 + ",time:" + time);
                            inputBean bean = new inputBean(barcode, gName1, "1", "1", time);
                            if (!inputBeanList.contains(bean)) {
                                inputBeanList.add(bean);
//                                db.save(bean);
                            }
                            runOnUiThread(() -> adapter.notifyDataSetChanged());
                        } else {
                            Utils.showToast(OutputCheckActivity.this, "尚无此商品信息");
                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }

                }
            });
        }).start();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (isClear){
            intent.putExtra(Constant.NUM,str);
        }else{
            intent.putExtra(Constant.NUM,"");
        }
        setResult(300,intent);
        finish();
    }

    // 点击空白区域 自动隐藏软键盘
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
    }
}

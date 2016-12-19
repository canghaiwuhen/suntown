package com.suntown.scannerproject.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.suntown.scannerproject.R;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.bean.Item1;
import com.suntown.scannerproject.bean.Item2;
import com.suntown.scannerproject.scanner.adapter.ScannerAdapter;
import com.suntown.scannerproject.scanner.bean.PDA;
import com.suntown.scannerproject.scanner.bean.SCANDATA;
import com.suntown.scannerproject.scanner.bean.SCANDATAS;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.FormatString;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.Utils;
import com.suntown.scannerproject.utils.Xml2Json;
import com.suntown.scannerproject.weight.LoadingDialog;
import com.thoughtworks.xstream.XStream;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;

public class NoGoodsShelfActivity extends BaseActivity {
    private static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    private static final String SCN_CUST_EX_SCODE = "scannerdata";
    private static final String SCN_CUST_ACTION_CANCEL = "android.intent.action.SCANNER_BUTTON_UP";
    private static final String SCN_CUST_ACTION_START = "android.intent.action.SCANNER_BUTTON_DOWN";
    private static final int SCANNIN_GREQUEST_CODE = 1;
    private static final String TAG = "NoGoodsShelfActivity";
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.et_goods_tag)
    EditText etGoodsTag;
    @BindView(R.id.et_tag)
    EditText etTag;
    @BindView(R.id.lv_list)
    ListView lvList;
    private InputMethodManager systemService;
    private OkHttpClient client;
    //标签ip
    private String IPTag = "";
    //商品名称
    private String goodsTag = "";
    private String sid;
    private List<Item2> lvBeanList = new ArrayList<>();
    private List<String> stringTag = new ArrayList<>();
    private ScannerAdapter adapter = new ScannerAdapter(this, lvBeanList);
    private String userId;
    private String serverIp;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_goods_shelf);
        ButterKnife.bind(this);
        initView();
        Item1 item = getIntent().getParcelableExtra(Constant.ITEM);
        sid = item.sid;
        tvShopName.setText(item.sname);
        client = new OkHttpClient();
        systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        registerReceiver(receiver, intentFilter);
        serverIp = SPUtils.getString(this, Constant.SUBSERVER_IP);
        userId = SPUtils.getString(this, Constant.USER_CODE);
        if ("".equals(serverIp)) {
            serverIp = ApiConstant.BASE_URL;
        }
        dialog = new LoadingDialog(this);
    }

    private void initView() {
//        findViewById(R.id.tv_saoyisao).setOnClickListener(view -> {
//            //TODO 扫描
//            Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> {
//                Intent scannerIntent = new Intent(SCN_CUST_ACTION_START);
//                sendBroadcast(scannerIntent);
//            });
//        });
        lvList.setAdapter(adapter);
        adapter.SetOnItemClickCallBack( position -> {
            if (!Utils.isFastClick()) {
                Log.i(TAG,"lvBeanList:"+lvBeanList.size());
                String tinyip = lvBeanList.get(position).tinyip;
                lvBeanList.remove(position);
                stringTag.remove(tinyip);
                adapter.notifyDataSetChanged();
            }
        });


        etGoodsTag.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                IPTag = etTag.getText().toString();
                goodsTag = etGoodsTag.getText().toString();
                if (!"".equals(IPTag) && !"".equals(goodsTag)) {
                    getGoodsName(goodsTag);
                }
            }
            return false;
        });
        etTag.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                IPTag = etTag.getText().toString();
                goodsTag = etGoodsTag.getText().toString();
                if (!"".equals(IPTag) && !"".equals(goodsTag)) {
                    getGoodsName(goodsTag);
                }
            }
            return false;
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (0==lvBeanList.size()){
                    finish();
                }else{
                    showDialog();
                }
                break;
            case R.id.tv_confirm:
                //TODO 提交
                commitData();
                break;
        }
    }


    //提交
    private void commitData() {
        if (lvBeanList.size() != 0) {
            ArrayList<SCANDATA> scandataList = new ArrayList<>();
            for (Item2 item2 : lvBeanList) {
                SCANDATA scandata = new SCANDATA();
                scandata.TINYIP = item2.tinyip;
                scandata.BARCODE = item2.Barcode;
                scandataList.add(scandata);
            }
            PDA pda = new PDA();
            pda.SID = sid;
            SCANDATAS scandatas = new SCANDATAS();
            XStream stream = new XStream();
            stream.alias("SCANDATAS", SCANDATAS.class);
            stream.alias("SCANDATA", SCANDATA.class);
            stream.alias("PDA", PDA.class);
            scandatas.scandata = scandataList;
            scandatas.PDA = pda;
            String xml = stream.toXML(scandatas);
            xml = xml.replace("<scandata>", "").replace("</scandata>", "");
            Log.i(TAG, "sendXml:" + xml);
            submitData(xml);
        } else {
            Utils.showToast(this, "提交的数据不能为空");
        }
    }

    //向服务器 提交数据
    private void submitData(String xml) {
        dialog.show();
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/commitNonShelf")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                    try {
                    String xml = response.body().string();
                    xml = xml.replace("<ns:commitNonShelfResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                    xml = xml.replace("</ns:return></ns:commitNonShelfResponse>", "");
                    Log.i(TAG, "xml-" + xml);
                    if ("1".equals(xml)) {
                        Utils.showToast(NoGoodsShelfActivity.this, "提交成功");
                        runOnUiThread(() -> {
                            dialog.dismiss();
                            lvBeanList.clear();
                            adapter.notifyDataSetChanged();
                        });
                    } else {
                        runOnUiThread(() -> dialog.dismiss());
                        Utils.showToast(NoGoodsShelfActivity.this, "提交失败");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    dialog.dismiss();
                    Utils.showToast(NoGoodsShelfActivity.this, "提交失败，请检查网络");
                }
            });
        }).start();
    }

    //获取商品信息
    private void getGoodsName(String resultStr) {
        Log.i(TAG, serverIp + "   " + resultStr + "  " + sid + " resultStr " + resultStr);
        adapter.notifyDataSetChanged();
        RequestBody formBody = new FormBody.Builder().
                add("arg1", resultStr).
                add("arg0", sid).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/GetGoodsInfo3")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        ResponseBody body = response.body();
                        String xml = body.string();
//                        xml=new String(xml.getBytes("UTF-8"),"gb2312");
                        xml = xml.replace("<ns:GetGoodsInfo3Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                        xml = xml.replace("</ns:return></ns:GetGoodsInfo3Response>", "");
                        Log.i(TAG, "xml --" + xml);
                        xml = xml.replaceAll("&lt;", "<");
                        xml = xml.replaceAll("&gt;", ">");
                        xml = xml.replaceAll("&#xd;", "");
                        Log.i(TAG, "xml --" + xml);
                        Item2 item2 = new Xml2Json(xml).PullGoodsXml();
                        if (lvBeanList.contains(item2)) {
                            return;
                        }
                        Log.i(TAG, "lvBeanList --" + lvBeanList.toString());
                        if (!"".equals(item2.GName) && null != item2.GName) {
                            Log.i(TAG, item2.toString());
                            runOnUiThread(() -> {
//                                if (IPTag.equals(item2.tinyip)) {
//                                    lvBeanList.remove(item2);
//                                }
                                item2.tinyip = IPTag;
                                lvBeanList.add(0,item2);
                                stringTag.add(IPTag);
                                etTag.setText("");
                                etGoodsTag.setText("");
                                goodsTag = "";
                                IPTag = "";
                                adapter.notifyDataSetChanged();
                            });
                        } else {
                            Utils.showToast(NoGoodsShelfActivity.this, "暂无此货物信息");
                        }

                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    Utils.showToast(NoGoodsShelfActivity.this, "尚无此商品信息");
                }
            });
        }).start();

    }

    //回收键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                systemService.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 返回键按下
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (0==lvBeanList.size()){
                finish();
            }else{
                showDialog();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 弹出对话框
     */
    private void showDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("提示")
                .setContentText("您还没有提交数据，是否确认退出")
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true)
                .setCancelClickListener(sDialog -> sDialog.dismiss()).setConfirmClickListener(sDialog -> {
            finish();
            sDialog.dismiss();
        })
                .show();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(SCN_CUST_EX_SCODE);
                //TODO
                if ("".equals(message)) {
                    Log.i(TAG, "message:" + message);
                    return;
                }
                if (message.startsWith(".")) {
                    //                                      16
                    //转成标签 Integer.parseInt(String s, int radix)
                    String tinyip = FormatString.fromTinyip(message);
                    message = tinyip;
                }
                //TODO  查询服务器，获取信息
                if (message.contains(".")) {
                    etTag.setText(message);
                    IPTag = message;
                } else {
                    etGoodsTag.setText(message);
                    goodsTag = message;
                }
                Log.i(TAG, "tag:" + etTag.toString() + ",goodsTag:" + goodsTag.toString());
                if (!"".equals(IPTag) && !"".equals(goodsTag)) {
                    for (Item2 item2 : lvBeanList) {
                        if (item2.tinyip.equals(IPTag)) {
                            lvBeanList.remove(item2);
                            break;
                        }
                    }
                    getGoodsName(goodsTag);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}




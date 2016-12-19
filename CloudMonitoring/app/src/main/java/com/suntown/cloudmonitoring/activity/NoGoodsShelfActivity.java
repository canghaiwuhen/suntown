package com.suntown.cloudmonitoring.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.adapter.ScannerAdapter;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.Item2;
import com.suntown.cloudmonitoring.bean.XmlBean.PDA;
import com.suntown.cloudmonitoring.bean.XmlBean.SCANDATA;
import com.suntown.cloudmonitoring.bean.XmlBean.SCANDATAS;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.FormatString;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.utils.Xml2Json;
import com.thoughtworks.xstream.XStream;
import org.xmlpull.v1.XmlPullParserException;
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
import okhttp3.ResponseBody;

public class NoGoodsShelfActivity extends BaseActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_goods_shelf);
        ButterKnife.bind(this);
        initView();
        sid = getIntent().getStringExtra(Constant.SID);
        String sname = getIntent().getStringExtra(Constant.SNAME);
        client = new OkHttpClient();
        systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tvShopName.setText(sname);

        userId = SPUtils.getString(this, Constant.SUB_USER_ID);
        if ("".equals(userId)) {
            userId = SPUtils.getString(this, Constant.USER_ID);
            serverIp = SPUtils.getString(this, Constant.SERVER_IP);
        } else {
            serverIp = SPUtils.getString(this, Constant.SUBSERVER_IP);
        }
    }

    private void initView() {
        findViewById(R.id.tv_saoyisao).setOnClickListener(view -> {
            Intent intent = new Intent(this, CreamaActivity.class);
            intent.putExtra(Constant.IS_ON_SCANN, true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
        });
        lvList.setAdapter(adapter);
        adapter.SetOnItemClickCallBack(position -> {
            String tinyip = lvBeanList.get(position).tinyip;
            lvBeanList.remove(position);
            stringTag.remove(tinyip);
            adapter.notifyDataSetChanged();
        });
        etGoodsTag.setOnEditorActionListener((textView, i, keyEvent) -> {
            IPTag = etTag.getText().toString();
            goodsTag = etGoodsTag.getText().toString();
            if (!"".equals(IPTag) && !"".equals(goodsTag)) {
                getGoodsName(goodsTag);
            }
            return false;
        });
        etTag.setOnEditorActionListener((textView, i, keyEvent) -> {
            IPTag = etTag.getText().toString();
            goodsTag = etGoodsTag.getText().toString();
            if (!"".equals(IPTag) && !"".equals(goodsTag)) {
                getGoodsName(goodsTag);
            }
            return false;
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                //TODO 提交
                commitData();
                break;
        }
    }


    //二维码扫描
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String resultStr = bundle.getString(Constant.RESULT_CODE);
                    Log.i(TAG, "resultStr:" + resultStr);
                    if ("".equals(resultStr)) {
                        return;
                    }
                    if (resultStr.startsWith(".")) {
                        //                                      16
                        //转成标签 Integer.parseInt(String s, int radix)
                        String tinyip = FormatString.fromTinyip(resultStr);
                        resultStr = tinyip;
                    }
                    //TODO  查询服务器，获取信息
                    if (resultStr.contains(".")) {
                        etTag.setText(resultStr);
                        IPTag = resultStr;
                        goodsTag=etGoodsTag.getText().toString().trim();
                    } else {
                        IPTag= etTag.getText().toString().trim();
                        etGoodsTag.setText(resultStr);
                        goodsTag = resultStr;
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
//                            Utils.showToast(this,"该标签已扫描");
                    }
                }
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
        }
    }

    //向服务器 提交数据
    private void submitData(String xml) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML,xml ).build();
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
                        xml = xml.replace("<ns:commitNonShelfResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>","");
                        xml = xml.replace("</ns:return></ns:commitNonShelfResponse>","");
                        Log.i(TAG,"xml-"+xml);
                        if ("1".equals(xml)) {
                            Utils.showToast(NoGoodsShelfActivity.this, "提交成功");
                            runOnUiThread(() -> {
                                lvBeanList.clear();
                                adapter.notifyDataSetChanged();
                            });
                        } else {
                            Utils.showToast(NoGoodsShelfActivity.this, "提交失败");
                        }
//                    } catch (XmlPullParserException e) {
//                        e.printStackTrace();
//                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    Utils.showToast(NoGoodsShelfActivity.this, "提交失败，请检查网络");
                }
            });
        }).start();
    }

    //获取商品信息
    private void getGoodsName(String resultStr) {
        adapter.notifyDataSetChanged();
        RequestBody formBody = new FormBody.Builder().
                add("arg1", resultStr).
                add("arg0", sid).build();
        Log.i(TAG, serverIp + "   " + resultStr + "  " + sid);
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
                        if (lvBeanList.contains(item2)){
                            return;
                        }
                        if (!"".equals(item2.GName) && null != item2.GName) {
                            Log.i(TAG, item2.toString());
                            runOnUiThread(() -> {
//                                if (IPTag.equals(item2.tinyip)) {
//                                    lvBeanList.remove(item2);
//                                }
                                item2.tinyip = IPTag;
                                lvBeanList.add(item2);
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

}

package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.adapter.TinyipAdapter;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.Person;
import com.suntown.cloudmonitoring.bean.ShopXmlBean;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.utils.Xml2Json;

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

public class GoodsDetialActivity extends Activity {

    private static final String TAG = "GoodsDetialActivity";
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_old_price)
    TextView tvOldPrice;
    @BindView(R.id.tv_now_price)
    TextView tvNowPrice;
    @BindView(R.id.lv_list)
    ListView lvList;
    @BindView(R.id.tv_qh)
    TextView tvQh;
    private String barcode;
    private String sid;
    private String userId;
    private String serverIp;
    private OkHttpClient client;
    private List<ShopXmlBean> shopList = new ArrayList<>();
    private TinyipAdapter adapter = new TinyipAdapter(this, shopList);
    boolean isQH = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detial);
        ButterKnife.bind(this);
        Person person = getIntent().getParcelableExtra(Constant.PERSON);
        Log.i(TAG, "person-" + person.toString());
        barcode = person.barcode;
        sid = person.sid;
        init();
        queryData();
        lvList.setAdapter(adapter);
//        lvList.setOnItemClickListener((adapterView, view, i, l) -> {
//            ShopXmlBean shopXmlBean = shopList.get(i);
//            String tinyIp = shopXmlBean.TinyIp;
//            InfoBean infoBean = new InfoBean(sid,tinyIp);
//            Log.i(TAG, "sid-" + person.sid + ",ip-" + person.ip);
//            Intent intent = new Intent(GoodsDetialActivity.this, TagDetialActivity.class);
//            intent.putExtra(Constant.INFO_BEAN, infoBean);
//            startActivity(intent);
//        });
    }

    private void init() {
        client = new OkHttpClient();
        userId = SPUtils.getString(this, Constant.SUB_USER_ID);
        if ("".equals(userId)) {
            userId = SPUtils.getString(this, Constant.USER_ID);
            serverIp = SPUtils.getString(this, Constant.SERVER_IP);
        } else {
            serverIp = SPUtils.getString(this, Constant.SUBSERVER_IP);
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_qh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_qh:
                //设置缺货 shopList
                if (shopList.size() > 0) {
                    if (isQH){
                        setLoseGoods(0);
                    }else{
                        setLoseGoods(1);
                    }
                } else {
                    Utils.showToast(this, "数据不能为空");
                }
                break;
        }
    }

    private void setLoseGoods(int i) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SetAllQH>";
        //0 正常  1  缺货
        for (ShopXmlBean shopXmlBean : shopList) {
            String ip = shopXmlBean.TinyIp;
            Log.i(TAG, "ip-" + ip);
            xml += "<Data>" + "<TINYIP>" + ip + "</TINYIP>" + "<STATUS>" + i + "</STATUS></Data>";
        }
        xml += "</SetAllQH>";
        //TODO 设置缺货
        Log.i(TAG, "xml--" + xml);
        submitLoseGoods(xml);
    }


    /**
     * 设置缺货
     *
     * @param xml
     */
    private void submitLoseGoods(String xml) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/SetAllQH")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    result = result.replace("<ns:SetAllQHResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                    result = result.replace("</ns:return></ns:SetAllQHResponse>", "");
                    Log.i(TAG,"result-"+result);
                    if (result.equals("0")) {
                        Utils.showToast(GoodsDetialActivity.this, "提交任务成功");
                        runOnUiThread(() -> {
                            isQH=!isQH;
                            if (isQH){
                                tvQh.setText("取消缺货");
                                for (ShopXmlBean shopXmlBean : shopList) {
                                    shopXmlBean.GStatus="1";
                                }
                            }else{
                                tvQh.setText("设置缺货");
                                for (ShopXmlBean shopXmlBean : shopList) {
                                    shopXmlBean.GStatus="0";
                                }
                            }
                            adapter.notifyDataSetChanged();
                        });
                    } else {
                        Utils.showToast(GoodsDetialActivity.this, "提交任务失败");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                }
            });
        }).start();
    }

    private void queryData() {
        RequestBody formBody = new FormBody.Builder().
                add("Barcode", barcode).
                add("Sid", sid).build();
        Log.i(TAG, "barcode-" + barcode + ",sid" + sid+" serverIp"+serverIp);
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/GetGoodsInfo2")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String xml = response.body().string();
                        xml = xml.replace("<ns:GetGoodsInfo2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                        Log.i(TAG, "xml --" + xml);
                        xml = xml.replace("</ns:return></ns:GetGoodsInfo2Response>", "");
                        xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
                        Log.i(TAG, "xml --" + xml);
                        List<ShopXmlBean> shopXmlBeanList = new Xml2Json(xml).Xml2Bean();
                        for (ShopXmlBean shopXmlBean : shopXmlBeanList) {
                            String tinyip = shopXmlBean.TinyIp;
                            if (!"".equals(tinyip) && null!=tinyip) {
                                Log.i(TAG,"tinyip-"+tinyip);
//                                if (tinyip.length() > 5) {
//                                    tinyip = tinyip.substring(0, 5) + "\n" + tinyip.substring(5, tinyip.length() - 1);
//                                    shopXmlBean.setTinyIp(tinyip);
//                                }
                                shopList.add(shopXmlBean);
                                Log.i(TAG,"shopList-"+shopList.toString());
                            }
                        }
                        if (0<shopXmlBeanList.size()) {
                            runOnUiThread(() -> {
                                ShopXmlBean shopXmlBean = shopXmlBeanList.get(0);
                                String gStatus = shopXmlBean.GStatus;
                                if ("1".equals(gStatus)) {
                                    Log.i(TAG,"gStatus-"+gStatus);
                                    isQH=true;
                                    tvQh.setText("取消缺货");
                                }else if("0".equals(gStatus)){
                                    tvQh.setText("设置缺货");
                                    isQH=false;
                                }else{
                                    tvQh.setClickable(false);
                                    tvQh.setText("其他状态");
                                }

                                tvCode.setText(shopXmlBean.Barcode);
                                tvName.setText(shopXmlBean.GName);
                                tvOldPrice.setText(shopXmlBean.Oriprice);
                                tvNowPrice.setText(shopXmlBean.uptPrice);
                                adapter.notifyDataSetChanged();
                            });
                            Log.i(TAG, "shopXmlBeanList-" + shopXmlBeanList.toString());
                        }

                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call call, IOException e) {
//                    Utils.showToast(QueryActivity.this, "尚无此商品信息");
                }
            });
        }).start();
    }
}

package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.InfoBean;
import com.suntown.cloudmonitoring.bean.Item2;
import com.suntown.cloudmonitoring.bean.Person;
import com.suntown.cloudmonitoring.bean.ShelfItemBean;
import com.suntown.cloudmonitoring.bean.ShopXmlBean;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class HasGoodsDetialActivity extends Activity {

    private static final int SCANNIN_TAG = 0;
    private static final int SCANNIN_BARCODE = 1;
    @BindView(R.id.tv_ip)
    TextView tvIp;
    @BindView(R.id.et_ip)
    EditText etIp;
    @BindView(R.id.ll_change_tag)
    LinearLayout llChangeTag;
    @BindView(R.id.tv_barcode)
    TextView tvBarcode;
    @BindView(R.id.et_barcode)
    EditText etBarcode;
    @BindView(R.id.ll_change_barcode)
    LinearLayout llChangeBarcode;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_qh)
    TextView tvQh;
    @BindView(R.id.tv_off)
    TextView tvOff;
    @BindView(R.id.tv_set_off)
    TextView tvSetOff;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_standard)
    TextView tvStandard;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_old_price)
    TextView tvOldPrice;
    @BindView(R.id.tv_now_price)
    TextView tvNowPrice;
    @BindView(R.id.tv_battery)
    TextView tvBattery;
    @BindView(R.id.tv_stock)
    TextView tvStock;
    @BindView(R.id.tv_batch)
    TextView tvBatch;
    @BindView(R.id.tv_last_time)
    TextView tvLastTime;
    @BindView(R.id.tv_change_tag)
    TextView tvChangeTag;
    @BindView(R.id.tv_change_barcode)
    TextView tvChangeBarcode;
    private boolean isChangeTag = false;
    private boolean isChangeBarcode = false;
    private OkHttpClient client;
    private String userId;
    private String serverIp;
    private String TAG="HasGoodsDetialActivity";
    private ShopXmlBean shopXmlBean;
    private String sid;
    private String tinyip;
    private boolean isQH=false;
    private String barcode;
    private String gname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_goods_detial);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        Intent intent = getIntent();
        InfoBean infoBean = intent.getParcelableExtra(Constant.INFO_BEAN);
        barcode = intent.getStringExtra(Constant.BARCODE);
        gname = intent.getStringExtra(Constant.NAME);
        sid = infoBean.sid;
        tinyip = infoBean.tinyip;
        tvBarcode.setText(barcode);
        tvIp.setText(tinyip);
        tvName.setText(gname);
        queryService();
        Log.i(TAG,"sid+"+sid+",tinyip"+tinyip);
        init(sid,tinyip);
        etBarcode.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String barcode1 = etBarcode.getText().toString().trim();
                if(!"".equals(barcode1)){
                    if (!Utils.isFastClick()) {
                        getGoodsName(barcode1);
                    }
                }
                return true;
            }
            return false;
        });
    }
    private void queryService() {
        userId = SPUtils.getString(this, Constant.SUB_USER_ID);
        if ("".equals(userId)) {
            userId = SPUtils.getString(this, Constant.USER_ID);
            serverIp = SPUtils.getString(this, Constant.SERVER_IP);
        } else {
            serverIp = SPUtils.getString(this, Constant.SUBSERVER_IP);
        }
    }

    private void init(String sid,String tinyip) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.TINYIP, tinyip).
                add(Constant.SID, sid).
//                add(Constant.TINYIP, "-0.0.125.216").
//                add(Constant.SID, "571002002").
                build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/GetLabStatus2")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String xml = response.body().string();
                    xml = xml.replace("<ns:GetLabStatus2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>","");
                    Log.i(TAG,"xml:"+xml);
                    xml = xml.replace("</ns:return></ns:GetLabStatus2Response>","");
                    xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
                    Log.i(TAG,"xml:"+xml);
                    try {
                        shopXmlBean = new Xml2Json(xml).pullXml2Bean();
                        Log.i(TAG,"shopXmlBean:"+shopXmlBean);
                        runOnUiThread(() -> {
                           if(null==shopXmlBean.TinyIp){
                               //TODO
                           }else{
                               tvIp.setText(shopXmlBean.TinyIp);
                               tvBarcode.setText(shopXmlBean.Barcode);
                               tvName.setText(shopXmlBean.GName);
                               String gStatus = shopXmlBean.GStatus;
                               Log.i(TAG,"gStatus:"+gStatus);
                               if ("1".equals(gStatus)){
                                   isQH=true;
                                   tvQh.setText("取消缺货");
                                   tvState.setText("缺货");
                               }else if("0".equals(gStatus)){
                                   isQH=false;
                                   tvQh.setText("设置缺货");
                                   tvState.setText("正常");
                               }else{
                                   tvQh.setVisibility(View.GONE);
                                   tvState.setText("其他状态");
                                   tvQh.setTextColor(Color.GRAY);
                               }
                               String powerOff = shopXmlBean.PowerOff;
                               if ("1".equals(powerOff)) {
                                   tvSetOff.setText("关机");
                                   tvSetOff.setTextColor(Color.GRAY);
                                   tvSetOff.setClickable(false);
                                   tvOff.setText("已关机");
                               }else if("0".equals(powerOff)){
                                   tvSetOff.setText("关机");
                                   tvOff.setText("已开机");
                               }else{
                                   tvSetOff.setVisibility(View.GONE);
                                   tvOff.setText("其他状态");
                               }
                               tvCode.setText(shopXmlBean.GCode);
                               tvType.setText(shopXmlBean.DispStr);
                               tvUnit.setText(shopXmlBean.Unit);
                               tvStandard.setText(shopXmlBean.Spec);
                               tvArea.setText(shopXmlBean.Origin);
                               tvOldPrice.setText(shopXmlBean.Oriprice);
                               tvNowPrice.setText(shopXmlBean.uptPrice);
                               tvBattery.setText(shopXmlBean.Battery);
                               tvLastTime.setText(shopXmlBean.LastDate);
                               tvStock.setText(shopXmlBean.CurStore);
                               tvBatch.setText(shopXmlBean.VBatchNO);
                           }
                        });
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call call, IOException e) {

                }
            });
        }).start();
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm, R.id.tv_change_tag, R.id.tv_scanner_tag, R.id.tv_change_barcode,
            R.id.tv_qh,R.id.tv_scanner_barcode,R.id.tv_set_off})
    public void onClick(View view) {
        Intent intent = new Intent(this, CreamaActivity.class);
        switch (view.getId()) {
            case R.id.iv_back:
                Intent mIntent = new Intent();
                Log.i(TAG,"barcode:"+barcode+",tinyip:"+tinyip);
                mIntent.putExtra(Constant.BARCODE, barcode);
                mIntent.putExtra(Constant.TINYIP, tinyip);
                mIntent.putExtra(Constant.NAME,gname);
                // 设置结果，并进行传送
                setResult(RESULT_OK, mIntent);
                finish();
                break;
            //TODO 提交货架信息
            case R.id.tv_confirm:
                String ip = etIp.getText().toString().trim();
                String Barcode = etBarcode.getText().toString().trim();
                if (!"".equals(ip)){
                    tinyip = ip;
                }
                if ("".equals(Barcode)){
                    Barcode = barcode ;
                }
                Log.i(TAG,"ip:"+ip+",barcode:"+Barcode);
                    if(!tinyip.contains(".")){
                        Utils.showToast(this,"更换的标签格式不正确");
                    }else{
                        commitBarcode(Barcode);
                        commitData(tinyip,Barcode);
                    }
                break;
            case R.id.tv_change_tag:
                isChangeTag = !isChangeTag;
                if (isChangeTag) {
                    llChangeTag.setVisibility(View.VISIBLE);
                    tvChangeTag.setText("取消更换");

                }else{
                    llChangeTag.setVisibility(View.GONE);
                    tvChangeTag.setText("标签更换");
                }
                break;
            case R.id.tv_scanner_tag:
                intent.putExtra(Constant.IS_ON_SCANN, true);
                startActivityForResult(intent, SCANNIN_TAG);
                break;
            case R.id.tv_change_barcode:
                isChangeBarcode = !isChangeBarcode;
                if (isChangeBarcode) {
                    tvChangeBarcode.setText("取消更换");
                    llChangeBarcode.setVisibility(View.VISIBLE);
                }else{
                    tvChangeBarcode.setText("条码更换");
                    llChangeBarcode.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_scanner_barcode:
                intent.putExtra(Constant.IS_ON_SCANN, true);
                startActivityForResult(intent, SCANNIN_BARCODE);
                break;
            case R.id.tv_qh:
                String gStatus = shopXmlBean.GStatus;
                if ("0".equals(gStatus)||"1".equals(gStatus)){
                    isQH=!isQH;
                    if (isQH){
                        setLoseGoods(1);
                        tvState.setText("缺货");
                        tvQh.setText("取消缺货");
                    }else{
                        setLoseGoods(0);
                        tvState.setText("正常");
                        tvQh.setText("设置缺货");
                    }
                }
                break;
            case R.id.tv_set_off:
                String powerOff = shopXmlBean.PowerOff;
                if ("0".equals(powerOff)){
                    offTag();
                }
                break;
        }
    }
    //提交更换商品
    private void commitBarcode(String mun) {
        Log.i(TAG,"sid:"+sid+",tinyip:"+tinyip+",barcode:"+mun);
        RequestBody formBody = new FormBody.Builder().
                add(Constant.SID, sid).
                add(Constant.IP, tinyip).
                add("barcode", mun).
                build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/ShelfLabGoodsCHG2")
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
                    xml=xml.replace("<ns:ShelfLabGoodsCHG2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>","");
                    xml=xml.replace("</ns:return></ns:ShelfLabGoodsCHG2Response>","");
                    Log.i(TAG,"xml-"+xml);
                    if ("1".equals(xml)) {
                        Log.i(TAG,"提交更换商品成功");
                        Utils.showToast(HasGoodsDetialActivity.this,"提交更换商品成功");
                        runOnUiThread(() -> {
                            etBarcode.setText("");
                            tvBarcode.setText(mun);
                            barcode = mun;

                        });
                    }else if("-1".equals(xml)){
                        Log.i(TAG,"提交更换商品失败");
                        Utils.showToast(HasGoodsDetialActivity.this,"没有商品信息");
                    }else {
                        Log.i(TAG,"提交更换商品失败");
                        Utils.showToast(HasGoodsDetialActivity.this,"更换绑定商品失败，请重试");
                    }
                }
            });
        }).start();
    }

    /**
     * 缺货提交
     * @param i
     */
    private void setLoseGoods(int i) {
        String xml = "<SetAllQH><Data>" + "<TINYIP>" + shopXmlBean.TinyIp + "</TINYIP>" + "<STATUS>" + i + "</STATUS></Data></SetAllQH>";
        Log.i(TAG,"xml:"+xml);
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
                        Utils.showToast(HasGoodsDetialActivity.this, "提交任务成功");
                        runOnUiThread(() -> {
                            if (isQH){
                                tvQh.setText("取消缺货");
                                shopXmlBean.GStatus="1";
                            }else{
                                tvQh.setText("设置缺货");
                                shopXmlBean.GStatus="0";
                            }
                        });
                    } else {
                        Utils.showToast(HasGoodsDetialActivity.this, "提交任务失败");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                }
            });
        }).start();
    }

    /**
     * 提交信息
     */

//    标签商品更换提交
//    String ShelfLabGoodsCHG2(String sid,String ip,String barcode); 返回值 1 表示成功 0 表示失败
    private void commitData(String Ip ,String num) {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sDateFormat =new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");
        String date =  sDateFormat.format(currentTime);
//        String xml = "<SCANDATAS><PDA><SID>"+sid+"</SID></PDA><SCANDATA><TINYIP>"+ip+"</TINYIP><BARCODE>"+barcode+"</BARCODE></SCANDATA>";
            String tinyIp = shopXmlBean.TinyIp;
            Log.i(TAG,"ip:"+Ip+",tinyIp"+tinyIp);
        if (!Ip.equals(tinyIp)) {
            String xml = "<LabCHG><oldIP>"+ tinyIp +"</oldIP><NewIP>"+Ip+"</NewIP><ScanDate>"+date+"</ScanDate><SID>"+sid+"</SID><UID>"+userId+"</UID><SCANID>"+sid+num+"</SCANID></LabCHG>";
            //返回值 1 表示成功 0 表示失败
//        <LabCHG><oldIP>0.0.56.73</oldIP><NewIP>0.0.56.73</NewIP><ScanDate>2016-11-24   05:33:33</ScanDate><SID>571002002</SID><UID>suntown</UID><SCANID>5710020026922145800526</SCANID></LabCHG>
            Log.i(TAG,"xml:"+xml);
            RequestBody formBody = new FormBody.Builder().
                    add(Constant.XML,xml).build();
            Request request = new Request.Builder()
                    .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/Commit_LabCHG")
                    .post(formBody)
                    .build();
            new Thread(() -> {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String xml = response.body().string();
                        xml = xml.replace("<ns:Commit_LabCHGResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>","");
                        xml = xml.replace("</ns:return></ns:Commit_LabCHGResponse>","");
                        Log.i(TAG,"xml-"+xml);
                        if ("0".equals(xml)) {
                            Utils.showToast(HasGoodsDetialActivity.this, "提交成功");
                            runOnUiThread(() -> {
                                etBarcode.setText("");
                                etIp.setText("");
                                tvIp.setText(Ip);
                                tvBarcode.setText(num);
                                tinyip=Ip;
                                barcode = num;
                            });
                        } else {
                            Utils.showToast(HasGoodsDetialActivity.this, "提交失败");
                        }
                    }
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                });
            }).start();
        }
    }


    /**
     * 关机
     */
    private void offTag() {
        String xml = "<ESLOFF><TINYIP>" + shopXmlBean.TinyIp + "</TINYIP></ESLOFF>";
        submitService(xml);
    }

    private void submitService(String xml) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/ESLOFF")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    result = result.replace("<ns:ESLOFFResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>","").
                            replace("</ns:return></ns:ESLOFFResponse>","");
                    if (result.equals("0")) {
                        Utils.showToast(HasGoodsDetialActivity.this, "提交关机任务成功");
                        runOnUiThread(() -> {
                            tvSetOff.setClickable(false);
                            tvSetOff.setBackgroundResource(R.color.colorGary);
                            shopXmlBean.PowerOff="1";
                            tvOff.setText("已关机");
                        });
                    } else {
                        Utils.showToast(HasGoodsDetialActivity.this, "提交关机任务失败");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                }
            });
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_TAG:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String resultStr = bundle.getString(Constant.RESULT_CODE);
                    if (resultStr.contains(".")) {
                        etIp.setText(resultStr);
                    } else {
                        Utils.showToast(this, "请扫码标签");
                    }
                }
                break;
            case SCANNIN_BARCODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String resultStr = bundle.getString(Constant.RESULT_CODE);
                    if (!resultStr.contains(".")) {
                        etBarcode.setText(resultStr);
                        //查询条码信息
                        getGoodsName(resultStr);
                    } else {
                        Utils.showToast(this, "请扫码商品条码");
                    }
                }
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent mIntent = new Intent();
            mIntent.putExtra(Constant.BARCODE, barcode);
            mIntent.putExtra(Constant.TINYIP, tinyip);
            mIntent.putExtra(Constant.NAME,gname);
            // 设置结果，并进行传送
            setResult(RESULT_OK, mIntent);
            finish();
            return true;
        }
        return false;
    }

    /**
     * 查询商品信息
     * @param barcode
     */
    private void getGoodsName(String barcode) {
        Log.i(TAG, "barcode=" + barcode + ",  sid" + sid + ", tag=");
        RequestBody formBody = new FormBody.Builder().
                add("arg1", barcode).
                add("arg0", sid).build();
//        add(Constant.SID, "571002002").build();
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
                        xml = xml.replace("<ns:GetGoodsInfo3Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                        Log.i(TAG, "xml --" + xml);
                        xml = xml.replace("</ns:return></ns:GetGoodsInfo3Response>", "");
                        Log.i(TAG, "xml --" + xml);
                        xml = xml.replaceAll("&lt;", "<");
                        xml = xml.replaceAll("&gt;", ">");
                        Log.i(TAG, "xml --" + xml);
                        xml = xml.replaceAll("&#xd;", "");
//                        List<ShopXmlBean> shopXmlBeanList = new Xml2Json(xml).PullXml();
                        Item2 item2 = new Xml2Json(xml).PullGoodsXml();
                        String gName = item2.GName;
                        runOnUiThread(() -> {
                            tvName.setText(gName);
                            gname=gName;
                        });

                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {

                }
            });
        }).start();
    }
}

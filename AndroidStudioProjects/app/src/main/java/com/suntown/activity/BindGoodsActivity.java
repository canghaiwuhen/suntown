package com.suntown.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.bean.BarCodeBean;
import com.suntown.bean.GNameBean;
import com.suntown.bean.GoodsBean;
import com.suntown.bean.LoginBean;
import com.suntown.bean.ServerInfoBean;
import com.suntown.utils.Constant;
import com.suntown.utils.HexStr;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BindGoodsActivity extends Activity implements View.OnClickListener {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private ImageView ivBack;
    private TextView tvSave;
    private ImageView ivZXing;
    private TextView tvGoodsName;
    private TextView etGoodsName;
    private ListView lvGoodsName;
    private OkHttpClient client;
    private String module_ip;
    public String TAG="BindGoodsActivity";
    private String  tagNum;
    private Map<String,String> goodsMap = new HashMap<>();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                List<BarCodeBean> barCodeBeanList = (List<BarCodeBean>) msg.obj;
                final List<String> barcodeList = new ArrayList<>();
                final List<String> gnameList = new ArrayList<>();
                for (BarCodeBean barCodeBean : barCodeBeanList) {
                    String barcode = barCodeBean.getBarcode();
                    String gname = barCodeBean.getGname();
                    barcodeList.add(barcode);
                    gnameList.add(gname);
                }
                lvGoodsName.setAdapter(new ArrayAdapter<String>(BindGoodsActivity.this, android.R.layout.simple_list_item_1,gnameList));
                lvGoodsName.setOnItemClickListener((parent, view, position, id) -> {
                    //TODO 获取当前条目的barcodeName
                    String barcodeName = barcodeList.get(position);
                    etGoodsName.setText(gnameList.get(position));
                    goodsMap.put(gnameList.get(position),barcodeName);
                    Log.i(TAG, barcodeName);
                });
                if(msg.what==2){
                    GoodsBean goodsBean = (GoodsBean) msg.obj;
                }
            }
        }
    };
    private boolean isDeviceList;
    private InputMethodManager systemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_goods);
        tagNum = getIntent().getStringExtra(Constant.TAG_NAME);
        isDeviceList = getIntent().getBooleanExtra(Constant.IS_DEVICE_LIST,false);
        Log.i(TAG,tagNum+" "+isDeviceList);
        if (tagNum==null){
            tagNum = SPUtils.getString(this, Constant.TAG_IP);
        }
        systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        init();
    }
    private void init() {
        client = new OkHttpClient();
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        ivZXing = ((ImageView) findViewById(R.id.iv_zxing));
        tvSave = ((TextView) findViewById(R.id.tv_save));
        tvGoodsName = ((TextView) findViewById(R.id.tv_good_name));
        lvGoodsName = ((ListView) findViewById(R.id.lv_goodsName));
        etGoodsName = ((TextView) findViewById(R.id.et_goodsName));
        ivZXing.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvGoodsName.setText(tagNum);
        takeServerInfo();
        etGoodsName.addTextChangedListener(watcher);
    }
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String str = s.toString();
            Log.d("ontextchanged", str);
            queryInfo(str);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    //TODO 点击绑定商品
    public void bindGoods(View view) {
        final String goodsName = etGoodsName.getText().toString();
        if ("".equals(goodsName)||"".equals(tagNum)){
            Utils.showToast(this,"商品或标签不能为空");
        }
        final String s = goodsMap.get(goodsName);
        if ("".equals(s)||s==null){
            Utils.showToast(this,"请输入正确的商品");
            return;
        }
        RequestBody formBody = new FormBody.Builder()
                .add("arg0", tagNum)
                .add("arg1",s)
                .build();
        final Request request = new Request.Builder()
                .url(Constant.BASE_HOST+"Set_oked_goods")
                .post(formBody)
                .build();
        try {
            new Thread(() -> {
                {
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Utils.showToast(BindGoodsActivity.this,"绑定失败，请检查网络");
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            InputStream is = response.body().byteStream();
                            String json = null;
                            try {
                                json = new Xml2Json(is).Pull2Xml();
                                Log.i("loginActivity",json+"");
                                LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                                Log.d("LoginActivity", "loginBean:" + loginBean.getRESULT());
                                String result = loginBean.getRESULT();
                                if (result.equals("0")){
                                    Utils.showToast(BindGoodsActivity.this,"绑定商品成功");
                                    new Thread(() -> {
                                        if (!isDeviceList){
                                            startActivity(new Intent(BindGoodsActivity.this,MainActivity.class));
                                        }else{
                                            Intent intent = new Intent();
                                            intent.putExtra(Constant.GOODS_NAME,goodsName);
                                            setResult(300, intent);
                                        }
                                        finish();
                                    }).start();
                                }else {
                                    Utils.showToast(BindGoodsActivity.this,"绑定商品失败，请重试");
                                }
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取服务器信息
    private void takeServerInfo() {
        final Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST("takeServerInfo"))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.showToast(BindGoodsActivity.this, "联网失败，请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = response.body().byteStream();
                Log.i(TAG, is.toString());
                String json = null;
                try {
                    json = new Xml2Json(is).Pull2Xml();
                    Log.i(TAG, json + "");
                    ServerInfoBean serverInfoBean = new Gson().fromJson(json, ServerInfoBean.class);
                    String result = serverInfoBean.getRESULT();
                    Log.i(TAG, "loginBean:" + serverInfoBean.getRESULT());
                    if (result.equals("0")) {
                        String server_ip = serverInfoBean.getSERVER_IP();
                        SPUtils.put(BindGoodsActivity.this, Constant.SERVER_IP, server_ip);
                        Log.i(TAG, server_ip);
                        String server_portno = serverInfoBean.getSERVER_PORTNO();
                        SPUtils.put(BindGoodsActivity.this, Constant.SERVER_PORTNO, server_portno);
                        Log.i(TAG, server_portno);
                    } else {
//                        Utils.showToast(BindGoodsActivity.this, "绑定失败");
                        Log.i(TAG, "获取服务器信息失败");
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.iv_zxing:
                startActivityForResult(new Intent(this, CreamaActivity.class), SCANNIN_GREQUEST_CODE);
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
                    String resultStr = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.i(TAG,"resultStr:"+resultStr);
//                    etGoodsName.setText(resultStr);
                    sendTAGInfo(resultStr);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mhandler.removeCallbacks(null);
//        handler.removeCallbacks(null);
    }

    //向服务器发送标签信息
    private void sendTAGInfo(String resultStr) {
        RequestBody formBody = new FormBody.Builder().add("arg0", resultStr).build();
        final Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST("getGoods_Info"))
                .post(formBody)
                .build();
        new Thread(() -> {
            {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Utils.showToast(BindGoodsActivity.this, "联网失败，请检查网络");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        String json = null;
                        try {
                            json = new Xml2Json(is).Pull2Xml();
//                            LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                            final GoodsBean goodsBean = new Gson().fromJson(json, GoodsBean.class);
                            if ("".equals(goodsBean.getGNAME())) {
                                return;
                            }
                            final String gname = goodsBean.getGNAME();
                            if ("" != gname) {

                                Log.i(TAG, "gname:"+gname);
                                Log.i(TAG, "barcode:"+ goodsBean.getBARCODE());
//                                Message msg = new Message();
//                                msg.what=2;
//                                msg.obj=goodsBean;
//                                handler.sendMessage(msg);
                                runOnUiThread(() -> {
                                    etGoodsName.setText(goodsBean.getGNAME());
                                    goodsMap.put(goodsBean.getGNAME(),goodsBean.getBARCODE());
                                });
                            } else {
                                Utils.showToast(BindGoodsActivity.this, "暂无此货物信息");
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    private void queryInfo(String str) {
        RequestBody formBody = new FormBody.Builder().
                        //TODO http://www.suntowngis.com:8080/axis2/services/sunteslwebservice/Getalikegoods_info?gname=1&startIndex=0&length=20&type=0&kid=0
                        add("gname", str).
                        add("startIndex", "0").
                        add("length", "20").
                        add("type", "0").
                        add("kid", "0").
                        build();
        Log.i(TAG, "etGoodsName:" + etGoodsName.getText().toString());
        final Request request = new Request.Builder()
                .url(Constant.QUERY_HOST + "Getalikegoods_info")
                .post(formBody)
                .build();
        new Thread(() -> {
            {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Utils.showToast(BindGoodsActivity.this, "联网失败，请检查网络");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        String json = null;
                        try {
                            json = new Xml2Json(is).Pull2Xml();
//                            LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                            final GNameBean gNameBean = new Gson().fromJson(json, GNameBean.class);
                            Log.i(TAG, "gNameBean:" + gNameBean + "");
                            if (gNameBean == null) {
                                return;
                            }
                            final int rows = gNameBean.getROWS();
                            Log.i(TAG, "rows:" + rows + "");
                            if (rows != 0) {
                                runOnUiThread(() -> {
                                    //TODO 获取条目信息显示
                                    ArrayList<BarCodeBean> barCodeBeanList = new ArrayList<>();
//                                        ArrayList<String> ganmeList = new ArrayList<>();
                                    for (int i = 0; i < rows; i++) {
                                        GNameBean.RECORDBean recordBean = gNameBean.getRECORD().get(i);
                                        String barcode = recordBean.getBARCODE();
                                        String gname = recordBean.getGNAME();
                                        BarCodeBean barCodeBean = new BarCodeBean(barcode, gname);
                                        barCodeBeanList.add(barCodeBean);
                                    }
                                    Message msg = new Message();
                                    msg.what=1;
                                    msg.obj=barCodeBeanList;
                                    handler.sendMessage(msg);
//                                        setAdapter(barcodeList, ganmeList);
                                });
                            } else {
                                Utils.showToast(BindGoodsActivity.this, "暂无此货物信息");
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                systemService.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
}


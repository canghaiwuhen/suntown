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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.api.ApiService;
import com.suntown.bean.BarCodeBean;
import com.suntown.bean.GNameBean;
import com.suntown.bean.GoodsBean;
import com.suntown.bean.LoginBean;
import com.suntown.bean.ServerInfoBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.Constant;
import com.suntown.utils.HexStr;
import com.suntown.utils.SPUtils;
import com.suntown.utils.SmartSocketUtils;
import com.suntown.utils.SocThread;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;
import com.suntown.widget.LoadingDialog;
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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class BindGoodsActivity extends BaseActivity implements View.OnClickListener {
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
    private String tagNum;
    private Map<String,String> goodsMap = new HashMap<>();
    private SocThread socketThread;
    private boolean isDeviceList;
    private InputMethodManager systemService;
    private LinearLayout llMain;
    private LoadingDialog dialog;
    private String versionName;
    private String batValueName;
    private Button btnBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_goods);
        isDeviceList = getIntent().getBooleanExtra(Constant.IS_DEVICE_LIST,false);
        dialog = new LoadingDialog(this);
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
        tvGoodsName = ((TextView) findViewById(R.id.tv_good_name));
        lvGoodsName = ((ListView) findViewById(R.id.lv_goodsName));
        etGoodsName = ((TextView) findViewById(R.id.et_goodsName));
        btnBind = ((Button) findViewById(R.id.btn_bind));
        llMain = (LinearLayout) findViewById(R.id.ll_bind_goods_main);
        ivZXing.setOnClickListener(this);
//        tvSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvGoodsName.setText(tagNum);
//        takeServerInfo();
        etGoodsName.addTextChangedListener(watcher);
        llMain.setVisibility(View.GONE);
        //TODO 连接标签，获取标签号，查询标签地址，设置标签地址
        module_ip = SPUtils.getString(this, Constant.MODULE_IP);
        startSocket(module_ip);
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

    /**
     * 连接Socket
     * @param module_ip
     */
    private void startSocket(String module_ip) {
        final byte[] bytes = SmartSocketUtils.makeBytes(Constant.GET_TAGINFO, new byte[]{0x00}, this);
//        final byte[] bytes = SmartSocketUtils.makeBytes(Constant.HIBERNATE_TAG, new byte[]{0x0A});
        Log.i(TAG, "bytes:" + bytes);
        // 设置ip端口，连接超时时长 TODO 创建socket 获取标签IP
        new Thread(() -> {
            socketThread = new SocThread(mhandler, mhandlerSend, this, module_ip, bytes);
            socketThread.start();
//                socketThread.send(bytes);
        }).start();
    }
    /**
     * 获取标签号
     */
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                Log.i(TAG, "mhandler接收到msg=" + msg.what);
                if (msg.obj != null) {
                    byte[] buffer = (byte[]) msg.obj;
                    String s = HexStr.bytesToHexString(buffer);
                    if (buffer.length > 0) {
                        Log.i(TAG, "mhandler接收到obj=" + s);
                        Log.i(TAG, "开始更新UI");
                        Log.i(TAG, "buffer" + buffer + "");
                        //截取负载，标签（前6个byte,后一个byte） 0000000714a10a16
                        Log.i(TAG, s.substring(12, s.length() - 2));
                        //获取到负载
                        String num = s.substring(12, s.length() - 2).substring(0, 8);
                        Log.i(TAG, "num:" + num);
                        String substring = num.substring(0, 2);
                        if (!num.endsWith("0")) {
                            tagNum = SmartSocketUtils.append(num);
                            tvGoodsName.setText(tagNum);
                            llMain.setVisibility(View.VISIBLE);
                        }
                        final String load = s.substring(12, s.length() - 2).substring(8, 16);
                        Log.i(TAG, "load:" + load);
                        String vsersionName = load.substring(0, 4);
                        String substring3 = vsersionName.substring(0, 2);
                        String substring4 = vsersionName.substring(2, 4);
                        int i3 = Integer.parseInt(substring3, 16);
                        int i4 = Integer.parseInt(substring4, 16);
                        versionName = i3+""+i4;
//                        Log.i(TAG,"vsersionName:"+ vsersionName);
                        final String BatValue = load.substring(4, 8);
                        final String substring1 = BatValue.substring(0, 2);
                        final String substring2 = BatValue.substring(2, 4);
                        final int i1 = Integer.parseInt(substring1, 16);
                        final int i2 = Integer.parseInt(substring2, 16);
                        Log.i(TAG, "i1:" + i1 + " " + "s2:" + i2);
                        batValueName= i1+""+i2;
                        Log.i(TAG, "BatValueName:" + batValueName);
                        stopSocket();
                        Log.i(TAG, "tagNum:" + tagNum);
                    } else {
                        Log.i(TAG, "没有数据返回不更新");
                    }
                }
            } catch (Exception e) {
                Log.i(TAG, "加载过程出现异常");
                e.printStackTrace();
            }
        }
    };



    /**
     * 标签关机
     */
    private void closeTag() {
        final byte[] buff = SmartSocketUtils.makeBytes(Constant.HIBERNATE_TAG, new byte[]{0x5A}, this);
        Log.i(TAG, "bytes:" + buff);
        // 设置ip端口，连接超时时长 TODO 创建socket 获取标签IP
        new Thread(() -> {
            socketThread = new SocThread(vhandler, mhandlerSend, this, module_ip, buff);
            socketThread.start();
//                socketThread.send(bytes);
        }).start();
    }

    /**
     * 标签关机
     * 关机成功绑定商品
     */
    private Handler vhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
//                    String s = msg.obj.toString();
                byte[] buffer = (byte[]) msg.obj;
                if (buffer.length > 0) {
                    stopSocket();
                    BindPhoneAndTAG();
                }
            }
        }
    };
    Handler mhandlerSend = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                Log.i(TAG, "mhandlerSend接收到msg.what=" + msg.what);
//                String b = msg.obj.toString();
                byte[] b = (byte[]) msg.obj;
                if (msg.what == 1) {
                    Log.i(TAG, " 发送成功" + b);
                } else {
                    Log.i(TAG, " 发送失败" + b);
                }
            } catch (Exception e) {
                Log.i(TAG, "加载过程出现异常");
                e.printStackTrace();
            }
        }
    };

    private void BindPhoneAndTAG() {
        String memid = SPUtils.getString(this, Constant.MEMID);
        Log.i(TAG,"tagNum:"+tagNum+",memid:"+memid+",versionName:"+versionName+",batValueName:"+batValueName);
        RequestBody formBody = new FormBody.Builder().
                add("arg0", tagNum).
                add("arg1", memid).
                add("arg2", versionName).
                add("arg3", batValueName).build();
        final Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST("Set_oked_user"))
                .post(formBody)
                .build();
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
                    Log.i("MainActivity", json + "");
                    LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                    String result = loginBean.getRESULT();
                    Log.d("LoginActivity", "loginBean:" + loginBean.getRESULT());
                    if ("0".equals(result)) {
                        Utils.showToast(BindGoodsActivity.this, "绑定成功");
                        runOnUiThread(() -> {
                            if (!"".equals(tagNum)) {
                                dialog.dismiss();
                                Intent intent = new Intent(BindGoodsActivity.this, SettingTinyipActivity.class);
                                intent.putExtra(Constant.TAG_NAME,tagNum);
                                startActivity(intent);
                            } else {
                                Utils.showToast(BindGoodsActivity.this, "标签通讯错误，请重新配置");
                                Log.i(TAG, "标签为空，不能跳转绑定商品");
                            }
                        });
                    } else {
                        Utils.showToast(BindGoodsActivity.this, "绑定失败");
                        return;
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * TODO 点击绑定商品
     * @param view
     */
    public void bindGoods(View view) {
        dialog.show();
        if (Utils.isFastClick()) {
            Utils.showToast(this,"请不要重复点击");
            return;
        }
        if ("".equals(tvGoodsName.toString())) {
            Utils.showToast(this,"标签为空，请重新配置");
            return;
        }
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
                                            if (!"".equals(tagNum)){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //标签关机，跳转绑定地址
                                                        closeTag();
                                                    }
                                                });

                                            }else{
                                                Utils.showToast(BindGoodsActivity.this,"正在加载中，请稍后");
                                            }
                                        }else{
                                            Intent intent = new Intent();
                                            intent.putExtra(Constant.GOODS_NAME,goodsName);
                                            setResult(300, intent);
                                            finish();
                                        }
                                    }).start();
                                }else {
                                    Utils.showToast(BindGoodsActivity.this,"绑定商品失败，请重试");
                                }
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            }

                        }
                    });
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

    /**
     * 获取服务器信息
     */
    private void getServerInfo() {
        final Request request = new Request.Builder()
                .url(Constant.BASE_HOST + "takeServerInfo")
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Utils.showToast(BindGoodsActivity.this, "请检查网络");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = response.body().byteStream();
                    String json;
                    try {
                        json = new Xml2Json(is).Pull2Xml();
                        ServerInfoBean serverInfoBean = new Gson().fromJson(json, ServerInfoBean.class);
                        String result = serverInfoBean.getRESULT();
                        if ("0".equals(result)) {
                            SPUtils.put(BindGoodsActivity.this, Constant.SERVER_IP, serverInfoBean.getSERVER_IP());
                            SPUtils.put(BindGoodsActivity.this, Constant.SERVER_PORTNO, serverInfoBean.getSERVER_PORTNO());
                            startConfigSocket();
                        } else {
                            Utils.showToast(BindGoodsActivity.this, "请检查网络");
                            Log.i(TAG, "获取后台ip地址失败");
                            finish();
                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }
            });
        }).start();
    }
    private void startConfigSocket() {
        String serverIp = SPUtils.getString(this, Constant.SERVER_IP);
        String portno = SPUtils.getString(this, Constant.SERVER_PORTNO);
        Log.i(TAG, "serverIP:" + serverIp + ",portno:" + portno);
        byte[] buff = HexStr.ResolveIP(portno, serverIp);
        final byte[] buffer = SmartSocketUtils.makeBytes(Constant.SETUP_SVR_INFOR, buff, this);
        Log.i(TAG, "buffer:" + buffer);
        String string = "";
        for (int i = 0; i < buffer.length; i++) {
            string += buffer[i];
        }
        Log.i("test", "String:" + string);
        new Thread(() -> {
            socketThread = new SocThread(shandler, shandlerSend, this, module_ip, buffer);
            socketThread.start();
//                socketThread.send(buffer);
        }).start();
    }
    /**
     * 配置服务handler
     */
    Handler shandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                Log.i(TAG, "mhandler接收到msg=" + msg.what);
                if (msg.obj != null) {
//                    String s = msg.obj.toString();
                    byte[] buffer = (byte[]) msg.obj;
                    String s = HexStr.bytesToHexString(buffer);
//                    if (s.trim().length() > 0) {
                    if (buffer.length > 0) {
                        Log.i(TAG, "mhandler接收到obj=" + s);
                        Log.i(TAG, "开始更新UI");
                        Log.i(TAG, "buffer" + buffer + "");
//                      5a5a080900010000000714a10a16ae
                        //截取负载，标签（前6个byte,后一个byte）
                        //获取到负载
                        String num = s.substring(12, s.length() - 2).substring(0, 10);
                        //返回负载检验值  01为失败 00 成功
                        if (num.substring(0, 2).equals("00")) {
                            stopSocket();
                            String TAGNum = num.substring(2, 10);
                            tagNum = SmartSocketUtils.append(TAGNum);
                            SPUtils.put(BindGoodsActivity.this, Constant.TAG_IP, tagNum);
                            Log.i(TAG, "num:" + BindGoodsActivity.this.tagNum);
                        }
                    } else {
                        Log.i(TAG, "没有数据返回不更新");
                    }
                }
            } catch (Exception e) {
                Log.i(TAG, "加载过程出现异常");
                e.printStackTrace();
            }
        }
    };
    Handler shandlerSend = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                Log.i(TAG, "mhandlerSend接收到msg.what=" + msg.what);
//                String b = msg.obj.toString();
                byte[] b = (byte[]) msg.obj;
                if (msg.what == 1) {
                    Log.i(TAG, " 发送成功" + b);
                } else {
                    Log.i(TAG, " 发送失败" + b);
                }
            } catch (Exception e) {
                Log.i(TAG, "加载过程出现异常");
                e.printStackTrace();
            }
        }
    };

    /**
     * 向服务器发送标签信息
     * @param resultStr
     */
    private void sendTAGInfo(String resultStr) {
        RequestBody formBody = new FormBody.Builder().add("arg0", resultStr).build();
        final Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST("getGoods_Info"))
                .post(formBody)
                .build();
        new Thread(() -> {
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
        }).start();
    }

    /**
     * 查询商品信息
     * @param str
     */
    private void queryInfo(String str) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.GNAME, str);
        params.put(Constant.STARTINDEX, "0");
        params.put(Constant.LENGTH, "20");
        params.put(Constant.TYPE, "0");
        params.put(Constant.KID, "0");
        String ip = Constant.QUERY_HOST;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).getLikeGoodsInfo(params).compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG,"s:"+s);
                String json = s.replace("<ns:Getalikegoods_infoResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:Getalikegoods_infoResponse>", "");
                GNameBean gNameBean = new Gson().fromJson(json, GNameBean.class);
                Log.i(TAG, "gNameBean:" + gNameBean + "");
                if (null == gNameBean) {
                    return;
                }
                int rows = gNameBean.getROWS();
                if (0 != rows) {
                    //TODO 获取条目信息显示
                    ArrayList<BarCodeBean> barCodeBeanList = new ArrayList<>();
                    for (int i = 0; i < rows; i++) {
                        GNameBean.RECORDBean recordBean = gNameBean.getRECORD().get(i);
                        String barcode = recordBean.getBARCODE();
                        String gname = recordBean.getGNAME();
                        BarCodeBean barCodeBean = new BarCodeBean(barcode, gname);
                        barCodeBeanList.add(barCodeBean);
                    }
                    List<String> barcodeList = new ArrayList<>();
                    List<String> gnameList = new ArrayList<>();
                    for (BarCodeBean barCodeBean : barCodeBeanList) {
                        String barcode = barCodeBean.getBarcode();
                        String gname = barCodeBean.getGname();
                        barcodeList.add(barcode);
                        gnameList.add(gname);
                    }
                    lvGoodsName.setAdapter(new ArrayAdapter<>(BindGoodsActivity.this, android.R.layout.simple_list_item_1,gnameList));
                    lvGoodsName.setOnItemClickListener((parent, view, position, id) -> {
                        //TODO 获取当前条目的barcodeName
                        String barcodeName = barcodeList.get(position);
                        etGoodsName.setText(gnameList.get(position));
                        goodsMap.put(gnameList.get(position),barcodeName);
                        Log.i(TAG, barcodeName);
                    });
                }
            }
        }, throwable -> {
            Utils.showToast(BindGoodsActivity.this, "联网失败，请检查网络");
        });

    }

    /**
     * 关闭socket
     */
    private void stopSocket() {
        if (socketThread != null) {
            socketThread.isRun = false;
            socketThread.close();
            socketThread = null;
            Log.i(TAG, "Socket已终止");
        }
    }

    /**
     * 点击隐藏软键盘
     * @param event
     * @return
     */
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


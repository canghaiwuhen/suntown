package com.suntown.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.bean.LoginBean;
import com.suntown.bean.ServerInfoBean;
import com.suntown.utils.Constant;
import com.suntown.utils.HexStr;
import com.suntown.utils.SPUtils;
import com.suntown.utils.SmartSocketUtils;
import com.suntown.utils.SocThread;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeviceSettingActivity extends Activity {
    private Context ctx;
    public String TAG="DeviceSettingActivity";
    private String module_ip;
    private String tagNum;
    private OkHttpClient client;
    private String bateVaue;
    private String vsersionName;
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
                        Log.i(TAG,"buffer"+buffer+"");
                        //截取负载，标签（前6个byte,后一个byte） 0000000714a10a16
                        Log.i(TAG,s.substring(12, s.length() - 2));
                        //获取到负载
                        String num = s.substring(12, s.length() - 2).substring(0,8);
                        Log.i(TAG,"num:"+num);
                        String substring = num.substring(0, 2);
                        if (!num.endsWith("0")){
                            tagNum = SmartSocketUtils.append(num);
                        }
                        final String load = s.substring(12, s.length() - 2).substring(8, 16);
                        Log.i(TAG,"load:"+load);
                        vsersionName = load.substring(0, 4);
                        Log.i(TAG,"vsersionName:"+ vsersionName);
                        final String BatValueName = load.substring(4, 8);
                        Log.i(TAG,"BatValueName:"+BatValueName);
                        final String substring1 = BatValueName.substring(0, 2);
                        final String substring2 = BatValueName.substring(2, 4);
                        final int i1 = Integer.parseInt(substring1, 16);
                        final int i2 = Integer.parseInt(substring2, 16);
                        bateVaue = i1+""+i2;
                        Log.i(TAG,"i1:"+i1+" "+"s2:"+i2);
                        llMain.setClickable(true);
                        //绑定手机和标签
                        BindPhoneAndTAG();
                        stopSocket();
//                        SPUtils.put(DeviceSettingActivity.this,Constant.TAG_IP,tagNum);
                        Log.i(TAG,"tagNum:"+ tagNum);
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

    private Handler bhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                byte[] closeTagBuffer = (byte[]) msg.obj;
                String s = HexStr.bytesToHexString(closeTagBuffer);
                Log.i("test","S:"+s);
                if (s.length() > 0) {
                    stopSocket();
                    SPUtils.put(DeviceSettingActivity.this,Constant.IS_OFF,true);
                    Intent intent = new Intent(DeviceSettingActivity.this, BindGoodsActivity.class);
                    intent.putExtra(Constant.TAG_NAME,tagNum);
                    SPUtils.put(DeviceSettingActivity.this,Constant.TAG_NAME,tagNum);
                    startActivity(intent);
                    finish();
                }
            }
        }
    };

    private Handler shandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                byte[] buffer = (byte[]) msg.obj;
                String s = HexStr.bytesToHexString(buffer);
                Log.i(TAG,"0x0B:"+s);
                if (buffer.length > 0) {
                    stopSocket();
                    SPUtils.put(DeviceSettingActivity.this,Constant.IS_OFF,true);
                    startActivity(new Intent(DeviceSettingActivity.this, MainActivity.class));
                    finish();
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

    private SocThread socketThread;
    private LinearLayout llMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);
        llMain = ((LinearLayout) findViewById(R.id.ll_main));
        llMain.setClickable(false);
        Intent intent = getIntent();
//        SPUtils.put(this,Constant.TAG_NAME,"");
        module_ip = SPUtils.getString(this, Constant.MODULE_IP);
        ctx = this;
        Log.i(TAG,module_ip);
        client = new OkHttpClient();
        getServerInfo();
        SPUtils.put(DeviceSettingActivity.this,Constant.IS_OFF,false);
    }

    public void overBind(View view) {
        boolean is_off = SPUtils.getBoolean(this, Constant.IS_OFF);
        Log.i("DeviceSettingActivity","IS_OFF:"+is_off);
        if (is_off){
            startActivity(new Intent(DeviceSettingActivity.this, MainActivity.class));
        }else{
            overTAG();
        }
    }
    public void bindGoods(View view) {
        boolean is_off = SPUtils.getBoolean(this, Constant.IS_OFF);
        Log.i("DeviceSettingActivity","IS_OFF:"+is_off);
        if (is_off){
            Intent intent = new Intent(DeviceSettingActivity.this, BindGoodsActivity.class);
            intent.putExtra(Constant.TAG_NAME,tagNum);
            SPUtils.put(DeviceSettingActivity.this,Constant.TAG_NAME,tagNum);
            startActivity(intent);
            finish();
        }else{
            stopBindTAG();
        }
    }

    //配置服务
    public void configService(View view) {
        startActivity(new Intent(this,DeviceConfigActivity.class));
        finish();
        stopSocket();
    }

    private void startSocket() {
        final byte[]  bytes = SmartSocketUtils.makeBytes(Constant.GET_TAGINFO, new byte[]{0x00},this);
//        final byte[] bytes = SmartSocketUtils.makeBytes(Constant.HIBERNATE_TAG, new byte[]{0x0A});
        Log.i(TAG, "bytes:" + bytes);
        // 设置ip端口，连接超时时长 TODO 创建socket 获取标签IP
        new Thread(() -> {
                socketThread = new SocThread(mhandler, mhandlerSend, ctx,module_ip,bytes);
                socketThread.start();
//                socketThread.send(bytes);
        }).start();
    }
    private void stopSocket() {
        if(socketThread!=null) {
            socketThread.isRun = false;
            socketThread.close();
            socketThread = null;
            Log.i(TAG, "Socket已终止");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "start onRestart~~~");
        startSocket();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "start onStop~~~");
        stopSocket();
        shandler.removeCallbacks(null);
        mhandler.removeCallbacks(null);
        mhandlerSend.removeCallbacks(null);
        bhandler.removeCallbacks(null);
    }
    //获取服务器信息
    private void getServerInfo() {
        final Request request = new Request.Builder()
                            .url(Constant.BASE_HOST+"takeServerInfo")
                            .build();
                    new Thread(() -> {
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Utils.showToast(DeviceSettingActivity.this,"请检查网络");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    InputStream is = response.body().byteStream();
                                    String json;
                                    try {
                                        json = new Xml2Json(is).Pull2Xml();
                                        ServerInfoBean serverInfoBean = new Gson().fromJson(json, ServerInfoBean.class);
                                        String result = serverInfoBean.getRESULT();
                                        if ("0".equals(result)){
                                            SPUtils.put(DeviceSettingActivity.this,Constant.SERVER_IP,serverInfoBean.getSERVER_IP());
                                            SPUtils.put(DeviceSettingActivity.this,Constant.SERVER_PORTNO,serverInfoBean.getSERVER_PORTNO());
                                            startSocket();
                                        }
                                    } catch (XmlPullParserException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    }).start();
    }
    private void BindPhoneAndTAG() {
        RequestBody formBody = new FormBody.Builder().
                add("arg0",tagNum).
                add("arg1",SPUtils.getString(this,Constant.MEMID)).
                add("arg2",vsersionName).
                add("arg3",bateVaue).build();
        Log.i("memid","memid:"+SPUtils.getString(this,Constant.MEMID));
        final Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST("Set_oked_user"))
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.showToast(DeviceSettingActivity.this, "联网失败，请检查网络");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = response.body().byteStream();
                Log.i("MainActivity", is.toString());
                String json ;
                try {
                    json = new Xml2Json(is).Pull2Xml();
                    Log.i("MainActivity", json + "");
                    LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                    String result = loginBean.getRESULT();
                    Log.d("LoginActivity", "loginBean:" + loginBean.getRESULT());
                    if ("0".equals(result)) {
                        Utils.showToast(DeviceSettingActivity.this, "绑定成功");
                    }else{
                        Utils.showToast(DeviceSettingActivity.this, "绑定失败");
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void stopBindTAG() {
        final byte[] bytes = SmartSocketUtils.makeBytes(Constant.HIBERNATE_TAG, new byte[]{0x5A},this);
        new Thread(() -> {
                socketThread = new SocThread(bhandler, mhandlerSend, ctx,module_ip,bytes);
                socketThread.start();
//                socketThread.send(bytes);
        }).start();
    }
    private void overTAG() {
        final byte[] bytes = SmartSocketUtils.makeBytes(Constant.HIBERNATE_TAG, new byte[]{0x5A},this);
        new Thread(() -> {
                socketThread = new SocThread(shandler, mhandlerSend, ctx,module_ip,bytes);
                socketThread.start();
//                socketThread.send(bytes);
        }).start();
    }
}

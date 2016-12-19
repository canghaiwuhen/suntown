package com.suntown.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hiflying.smartlink.OnSmartLinkListener;
import com.hiflying.smartlink.SmartLinkedModule;
import com.hiflying.smartlink.v3.SnifferSmartLinker;
import com.suntown.R;
import com.suntown.bean.LoginBean;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeviceOneKeyConfigActivity extends Activity implements OnSmartLinkListener {
    private boolean mIsConncting = false;
    private SnifferSmartLinker mSnifferSmartLinker;
    protected Handler mViewHandler = new Handler();
    private ImageView ivBack;
    private String ssid;
    private String TAG ="DeviceOneKeyConfigActivity";
    private String tagNum;
    private String vsersionName;
    private String bateVaue;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                Log.i(TAG, "mhandler接收到msg=" +msg.what);
                if (msg.obj != null) {
//                    String s = msg.obj.toString();
                    byte[] buffer = (byte[]) msg.obj;
                    String s = HexStr.bytesToHexString(buffer);
//                    if (s.trim().length() > 0) {
                    if (buffer.length > 0) {
                        Log.i(TAG, "mhandler接收到obj=" + s);
                        Log.i(TAG, "开始更新UI");
                        Log.i(TAG,"buffer"+buffer+"");
//                      5a5a080900010000000714a10a16ae
//                      0000000714a10a16
                        //截取负载，标签（前6个byte,后一个byte）
                        Log.i(TAG,s.substring(12, s.length() - 2));
                        //获取到负载
                        String num = s.substring(12, s.length() - 2).substring(0,8);
                        String substring = num.substring(0, 2);
                        if (tagName.lastIndexOf(tagName.length())!=num.lastIndexOf(num.length())){
                            Utils.showToast(DeviceOneKeyConfigActivity.this,"标签不一致");
                        }
                        if ("00".equals(substring)){
                            stopSocket();
                            getTagInfo();
                        }else{
                            Log.i(TAG,"绑定标签失败");
                            return;
                        }
                        Log.i(TAG,"num:"+num);

                    } else {
                        Log.i(TAG, "没有数据返回不更新");
                    }
                }
            } catch (Exception e) {
               Log.i(TAG,"加载过程出现异常");
                e.printStackTrace();
            }
        }
    };
    Handler mhandlerSend = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                Log.i(TAG, "mhandlerSend接收到msg.what=" + msg.what);
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
    private Handler mhandler = new Handler(){
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
                        Log.i(TAG,"tagNum:"+ tagNum);
                        stopSocket();
//                        BindPhoneAndTAG();
                        stopTag();

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
    private Handler shandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                byte[] buffer = (byte[]) msg.obj;
                String s = HexStr.bytesToHexString(buffer);
                Log.i(TAG,"0x0B:"+s);
                if (buffer.length > 0) {
                    stopSocket();
                    BindPhoneAndTAG();
                }
            }
        }
    };



    private SocThread socketThread;
    private String tagName;
    private OkHttpClient client;
    private Context ctx;
    private ProgressDialog mWaitingDialog;
    private String wifi_password;
    private String serverIp;
    private String portno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_one_key_config);
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        ctx = this;
        client = new OkHttpClient();
        tagName = getIntent().getStringExtra(Constant.TAG_NAME);
        init();
        ivBack.setOnClickListener(v -> finish());

    }

    private void init() {
        //1,判断wifi连接状态
        ssid = getSsid();
        //判断标签号是否相同
        //获取服务器信息
        if ("".equals(ssid)){
            Utils.showToast(ctx,getString(R.string.hiflying_smartlinker_no_wifi_connectivity));
            return;
        }
        serverIp = SPUtils.getString(this, Constant.SERVER_IP);
        portno = SPUtils.getString(this, Constant.SERVER_PORTNO);
        wifi_password = SPUtils.getString(this, Constant.WIFI_PASSWORD);
        mSnifferSmartLinker = SnifferSmartLinker.getInstence();
        mWaitingDialog = new ProgressDialog(this);
        mWaitingDialog.setMessage(getString(R.string.hiflying_smartlinker_waiting));
        mWaitingDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), (dialog, which) -> {

        });
        mWaitingDialog.setOnDismissListener(dialog -> {
                mSnifferSmartLinker.setOnSmartLinkListener(null);
                mSnifferSmartLinker.stop();
                mIsConncting = false;
        });
    }

    public void configWifi(View view) {
        if (!mIsConncting) {
            try {
                mSnifferSmartLinker.setOnSmartLinkListener(this);
                mSnifferSmartLinker.start(getApplicationContext(), wifi_password, ssid);
                mIsConncting = true;
                mWaitingDialog.show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void getTagInfo() {
        final byte[]  bytes = SmartSocketUtils.makeBytes(Constant.GET_TAGINFO, new byte[]{0x00},this);
        Log.i(TAG, "bytes:" + bytes);
        // TODO 创建socket 获取标签IP,电量，版本
        new Thread(() -> {
                socketThread = new SocThread(mhandler, mhandlerSend, ctx,moduleIP,bytes);
                socketThread.start();
//                socketThread.send(bytes);
        }).start();
    }


    private String getSsid() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        if (wm != null) {
            WifiInfo wi = wm.getConnectionInfo();
            if (wi != null) {
                String ssid = wi.getSSID();
                String bssid = wi.getBSSID();
                Log.i("WIFI",bssid);
                if (ssid.length() > 2 && ssid.startsWith("\"") && ssid.endsWith("\"")) {
                    return ssid.substring(1, ssid.length() - 1);
                } else {
                    return ssid;
                }
            }
        }
        return "";
    }
    private String moduleIP;
    private String mac;
    @Override
    public void onLinked(final SmartLinkedModule smartLinkedModule) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onLinked");
        mViewHandler.post(() -> {
                mac = smartLinkedModule.getMac();
                moduleIP = smartLinkedModule.getModuleIP();
                Log.i("Config", mac);
                Log.i("Config", moduleIP);
                Utils.showToast(DeviceOneKeyConfigActivity.this, "连接成功，请勿触碰屏幕，等待配置完成");
                mWaitingDialog.dismiss();
                configService();
        });
    }

    @Override
    public void onCompleted() {
        mViewHandler.post(() -> {
                // TODO Auto-generated method stub
                Utils.showToast(getApplicationContext(), "惠屏连接完成");
                mWaitingDialog.dismiss();
                configService();
                mIsConncting = false;
        });
    }
    //配置服务 和标签创建连接
    private void configService() {
        //开始连接socket
        startSocket();
    }

    private void startSocket() {
        Log.i(TAG,"server_portno:"+portno+","+"server_ip:"+serverIp);
        byte[] buff = HexStr.ResolveIP(portno, serverIp);
        final byte[] buffer = SmartSocketUtils.makeBytes(Constant.SETUP_SVR_INFOR,buff,DeviceOneKeyConfigActivity.this);
        new Thread(() -> {
                socketThread = new SocThread(handler, mhandlerSend, DeviceOneKeyConfigActivity.this,moduleIP,buffer);
                socketThread.start();
        }).start();
    }
    @Override
    public void onTimeOut() {
        mViewHandler.post(() -> {
                // TODO Auto-generated method stub
                Utils.showToast(getApplicationContext(),"连接超时");
                mWaitingDialog.dismiss();
                mIsConncting = false;
        });
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mSnifferSmartLinker.setOnSmartLinkListener(null);
    }
    private void stopSocket() {
        if(socketThread!=null){
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
    }
    private void BindPhoneAndTAG() {
        RequestBody formBody = new FormBody.Builder().
                add("arg0",tagName).
                add("arg1",SPUtils.getString(this,Constant.MEMID)).
                add("arg2",vsersionName).
                add("arg3",bateVaue).build();
        final Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST("Set_oked_user"))
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.showToast(DeviceOneKeyConfigActivity.this, "联网失败，请检查网络");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = response.body().byteStream();
                Log.i("MainActivity", is.toString());
                String json = null;
                try {
                    json = new Xml2Json(is).Pull2Xml();
                    Log.i("MainActivity", json + "");
                    LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                    String result = loginBean.getRESULT();
                    Log.d("LoginActivity", "loginBean:" + loginBean.getRESULT());
                    if ("0".equals(result)) {
                        Utils.showToast(DeviceOneKeyConfigActivity.this, "绑定成功");
                        runOnUiThread(() -> finish());
                    }else{
                        Utils.showToast(DeviceOneKeyConfigActivity.this, "绑定失败");
                        return;
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });



    }
    private void stopTag() {
        final byte[] buff = SmartSocketUtils.makeBytes(Constant.HIBERNATE_TAG, new byte[]{0x5A},this);
        Log.i(TAG, "bytes:" + buff);
        // 设置ip端口，连接超时时长 TODO 创建socket 获取标签IP
        new Thread(() -> {
                socketThread = new SocThread(shandler, mhandlerSend, DeviceOneKeyConfigActivity.this,moduleIP,buff);
                socketThread.start();
//                socketThread.send(bytes);
        }).start();
    }

}

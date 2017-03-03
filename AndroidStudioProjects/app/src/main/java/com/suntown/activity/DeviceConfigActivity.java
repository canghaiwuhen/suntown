package com.suntown.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.suntown.R;
import com.suntown.utils.Constant;
import com.suntown.utils.HexStr;
import com.suntown.utils.SPUtils;
import com.suntown.utils.SmartSocketUtils;
import com.suntown.utils.SocThread;


public class DeviceConfigActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llSuccsee;
    private LinearLayout llLoading;
    private TextView tvBindGoods;
    private ImageView ivBack;
    private String TAG="DeviceConfigActivity";
    private String tagNum;
    Handler mhandler = new Handler() {
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
                        Log.i(TAG,"buffer"+buffer+"");
//                      5a5a080900010000000714a10a16ae
                        //截取负载，标签（前6个byte,后一个byte）
                        //获取到负载
                        String num = s.substring(12, s.length() - 2).substring(0,10);
                        //返回负载检验值  01为失败 00 成功
                        if (num.substring(0,2).equals("00")){
                            stopSocket();
                            closeTag();
                            String TAGNum = num.substring(2, 10);
                            tvBindGoods.setClickable(true);
                            tvBindGoods.setTextColor(Color.RED);
                            tagNum = SmartSocketUtils.append(TAGNum);
                            SPUtils.put(DeviceConfigActivity.this,Constant.TAG_IP,tagNum);
                            Log.i(TAG,"num:"+ DeviceConfigActivity.this.tagNum);
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

    private Handler vhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
//                    String s = msg.obj.toString();
                byte[] buffer = (byte[]) msg.obj;
                if (buffer.length > 0) {
                    stopSocket();
                    SPUtils.put(DeviceConfigActivity.this,Constant.IS_OFF,true);
                    Log.i("DeviceSettingActivity","IS_OFF:"+ SPUtils.getBoolean(DeviceConfigActivity.this,Constant.IS_OFF));
                    llLoading.setVisibility(View.GONE);
                    llSuccsee.setVisibility(View.VISIBLE);
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
    private String module_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_config);
        init();
    }
    private void init() {
        llSuccsee = ((LinearLayout) findViewById(R.id.ll_bindSuccess));
        llLoading = ((LinearLayout) findViewById(R.id.ll_loading));
        tvBindGoods = ((TextView) findViewById(R.id.tv_bindGoods));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        ivBack.setOnClickListener(this);
        tvBindGoods.setOnClickListener(this);
        llLoading.setVisibility(View.VISIBLE);
        llSuccsee.setVisibility(View.GONE);

        tvBindGoods.setClickable(false);
        tvBindGoods.setTextColor(Color.GRAY);
        module_ip = SPUtils.getString(this, Constant.MODULE_IP);
        SPUtils.put(DeviceConfigActivity.this,Constant.IS_OFF,false);
        Log.i(TAG,"module_ip:"+module_ip);
        startSocket();
    }

    private void startSocket() {
        String serverIp = SPUtils.getString(this, Constant.SERVER_IP);
        String portno = SPUtils.getString(this, Constant.SERVER_PORTNO);
        Log.i(TAG,"serverIP:"+serverIp+",portno:"+portno);
        byte[] buff = HexStr.ResolveIP(portno, serverIp);
        final byte[] buffer = SmartSocketUtils.makeBytes(Constant.SETUP_SVR_INFOR, buff,this);
        Log.i(TAG,"buffer:"+buffer);
        String string = "";
        for (int i = 0; i < buffer.length; i++) {
            string+=buffer[i];
        }
        Log.i("test","String:"+string);
        new Thread(() -> {
            socketThread = new SocThread(mhandler, mhandlerSend, DeviceConfigActivity.this,module_ip,buffer);
            socketThread.start();
//                socketThread.send(buffer);
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
//                finish();
                break;
            case R.id.tv_bindGoods:
                if (tagNum==null){
                    Intent intent = new Intent(DeviceConfigActivity.this, BindGoodsActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(DeviceConfigActivity.this, BindGoodsActivity.class);
                    startActivity(intent);
                    intent.putExtra(Constant.TAG_NAME,tagNum);
                    finish();
                }
                break;
            default:
                break;
        }
    }
    private void closeTag() {
        final byte[] buff = SmartSocketUtils.makeBytes(Constant.HIBERNATE_TAG, new byte[]{0x5A},this);
        Log.i(TAG, "bytes:" + buff);
        // 设置ip端口，连接超时时长 TODO 创建socket 获取标签IP
        new Thread(() -> {
            socketThread = new SocThread(vhandler, mhandlerSend, DeviceConfigActivity.this,module_ip,buff);
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
    protected void onDestroy() {
        super.onDestroy();
        mhandler.removeCallbacks(null);
        mhandlerSend.removeCallbacks(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "start onStop~~~");
        stopSocket();
    }
}

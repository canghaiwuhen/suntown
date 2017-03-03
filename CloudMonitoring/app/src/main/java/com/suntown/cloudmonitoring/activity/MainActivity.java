package com.suntown.cloudmonitoring.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.form.FormActivity;
import com.suntown.cloudmonitoring.api.ApiClient;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.SmsTaskBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.ExampleUtil;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.weight.LoadingDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.iv_user)
    ImageView ivUser;
    @BindView(R.id.tv_change_server)
    TextView tvChangeServer;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.rl_ap_monitoring)
    RelativeLayout rlApMonitoring;
    @BindView(R.id.rl_change_price)
    RelativeLayout rlChangePrice;
    @BindView(R.id.rl_register_monitoring)
    RelativeLayout rlRegisterMonitoring;
    @BindView(R.id.rl_Battery_monitoring)
    RelativeLayout rlBatteryMonitoring;
    @BindView(R.id.rl_Battery_danger_monitoring)
    RelativeLayout rlBatteryDangerMonitoring;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.rl_helper)
    RelativeLayout rlHelper;
    @BindView(R.id.iv_form)
    ImageView ivForm;
    @BindView(R.id.rl_work_order)
    RelativeLayout rlWorkOrder;
    @BindView(R.id.fragment_layout)
    LinearLayout fragmentLayout;
    @BindView(R.id.right_nickname)
    TextView rightNickname;
    @BindView(R.id.ll_update_psw)
    LinearLayout llUpdatePsw;
    @BindView(R.id.ll_exit)
    LinearLayout llExit;
    @BindView(R.id.rl_witpad)
    RelativeLayout rlWitpad;
    @BindView(R.id.right)
    LinearLayout right;
    @BindView(R.id.iv_query)
    ImageView ivQuery;
    @BindView(R.id.ll_item1)
    LinearLayout llItem1;
    @BindView(R.id.iv_arrow_setting)
    ImageView ivArrowSetting;
    @BindView(R.id.iv_arrow_form)
    ImageView ivArrowForm;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    private boolean helper = false;
    private boolean form = false;
    private String userId;
    private SmsTaskBean smsTaskBean;
    private MessageReceiver mMessageReceiver;
    public static boolean isForeground =false;
    private LoadingDialog dialog;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new LoadingDialog(this);
        dialog.show();
        ButterKnife.bind(this);
        RxPermissions.getInstance(this).request( Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, Manifest.permission.VIBRATE).subscribe(granted -> {
                    if (granted) {
                        //同意后跳转
                        init();
                    } else {
                        //不同意，给提示
                        Toast.makeText(MainActivity.this, "请同意软件的权限，才能继续提供服务", Toast.LENGTH_LONG).show();
                        System.exit(0);
                    }
                });

    }

    private void init() {
        name = SPUtils.getString(this, Constant.USER_NAME);
        String modname = SPUtils.getString(this, Constant.MODNAME);
        userId = SPUtils.getString(this, Constant.USER_ID);
        rightNickname.setText(name.equals("") ? "用户名" : name);
        tvChangeServer.setText(modname.equals("") ? "服务" : modname);
        //获取未读消息
        registerMessageReceiver();
        getMessage();
    }

  

    /**
     * 获取未读取消
     */
    private void getMessage() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constant.LOOKSTATUS, "0");
        params.put(Constant.USER_ID, userId);
        Log.i(TAG,"userID:"+userId);
        ApiClient.getInstance().mApiService.getSmsTaskCount(params).compose(RxSchedulers.io_main()).subscribe(smsTaskBean -> {
            this.smsTaskBean = smsTaskBean;
            SmsTaskBean.ApSmstasksBean apSmstasks = smsTaskBean.apSmstasks;
            SmsTaskBean.NoUptSmstasksBean noUptSmstasks = smsTaskBean.noUptSmstasks;
            SmsTaskBean.LowSmstasksBean lowSmstasks = smsTaskBean.lowSmstasks;
            SmsTaskBean.NoLoginSmstasksBean noLoginSmstasksBean = smsTaskBean.noLoginSmstasks;
            SmsTaskBean.OtherSmstasksBean otherSmstasksBean = smsTaskBean.otherSmstasks;
            Log.i(TAG,"smsTaskBean-"+smsTaskBean.toString());
            if (0<apSmstasks.sendCount||0<noUptSmstasks.sendCount||0<lowSmstasks.sendCount||0<noLoginSmstasksBean.sendCount||0<otherSmstasksBean.sendCount) {
                ivMessage.setImageResource(R.drawable.message);
                Log.i(TAG,"apSmstasks-"+apSmstasks.toString());
                Log.i(TAG,"noUptSmstasks-"+noUptSmstasks.toString());
                Log.i(TAG,"lowSmstasks-"+lowSmstasks.toString());
                Log.i(TAG,"noLoginSmstasksBean-"+noLoginSmstasksBean.toString());
                Log.i(TAG,"otherSmstasksBean-"+otherSmstasksBean.toString());
            }
            dialog.dismiss();
        }, throwable -> {
            dialog.dismiss();
        });
    }

    @OnClick({R.id.iv_message, R.id.iv_user, R.id.tv_change_server, R.id.iv_arrow, R.id.rl_ap_monitoring,
            R.id.rl_change_price, R.id.rl_register_monitoring, R.id.rl_Battery_monitoring,
            R.id.rl_Battery_danger_monitoring, R.id.rl_helper, R.id.rl_work_order, R.id.fragment_layout,
            R.id.ll_update_psw, R.id.ll_exit, R.id.rl_query, R.id.rl_witpad})
    public void onClick(View view) {
        switch (view.getId()) {
            //推送消息
            case R.id.iv_message:
                Intent intent = new Intent(this, MessageActivity.class);
//                intent.putExtra(Constant.MESSAGE, smsTaskBean);
                startActivity(intent);
                break;
            //抽屉
            case R.id.iv_user:
//                right.openDrawer
                drawerlayout.openDrawer(right);
                break;
            //切换服务器
            case R.id.tv_change_server:
            case R.id.iv_arrow:
                startActivityForResult(new Intent(this, MyServiceActivity.class), 100);
//                overridePendingTransition(R.anim.act_exit, R.anim.act_enter);
                break;
            //ap监控
            case R.id.rl_ap_monitoring:
                startActivity(new Intent(this,ApPhotoActivity.class));
                break;
            //变价监控
            case R.id.rl_change_price:
                startActivity(new Intent(this,ChangePriceAct.class));
                break;
            //注册监控
            case R.id.rl_register_monitoring:
                startActivity(new Intent(this,RegisterMrnitoring.class));
                break;
            //电量监控
            case R.id.rl_Battery_monitoring:
                startActivity(new Intent(this,BatteryMonitoringAct.class));
                break;
            //电量降幅监控
            case R.id.rl_Battery_danger_monitoring:
                startActivity(new Intent(this,BatteryDangerMonitoringActivity.class));
                break;
            //监控助手，展开条目
            case R.id.rl_helper:
                helper = !helper;
                if (helper) {
                    llItem1.setVisibility(View.VISIBLE);
                    ivArrowSetting.setImageResource(R.drawable.arrow_d);
                } else {
                    llItem1.setVisibility(View.GONE);
                    ivArrowSetting.setImageResource(R.drawable.arrow_r);
                }
                break;
            //条目查询
            case R.id.rl_query:
                startActivity(new Intent(MainActivity.this,QueryActivity.class));
                break;
            //手持机
            case R.id.rl_witpad:
                startActivity(new Intent(MainActivity.this,WaitPadActivity.class));
                break;
            // TODO 工单
            case R.id.rl_work_order:
                LoadingDialog loadingDialog = new LoadingDialog(this);
                loadingDialog.show();
                JMessageClient.getUserInfo(name, new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, UserInfo userInfo) {
                        if (null == userInfo) {
//                            Utils.showToast(MainActivity.this,"用户未上线");
                            Log.i(TAG,"userInfo:"+"用户未上线");
                            login(loadingDialog);
                        }else{
                            Log.i(TAG,"userInfo:"+userInfo.toString());
                            loadingDialog.dismiss();
                            startActivity(new Intent(MainActivity.this, FormActivity.class));
                        }
                    }
                });

                break;
//            //待提交
//            case R.id.rl_wait_confirm:
//                break;
//            //待处理
//            case R.id.rl_wait_handle:
//                break;
//            //已经处理
//            case R.id.rl_completed:
//                break;
            case R.id.ll_update_psw:
                startActivity(new Intent(MainActivity.this, UpdatePswActivity.class));
                break;
            case R.id.ll_exit:
                SPUtils.put(MainActivity.this, Constant.USER_ID, "");
                SPUtils.put(MainActivity.this, Constant.USER_NAME, "");
                SPUtils.put(MainActivity.this, Constant.PASS_WORD, "");
                SPUtils.put(this,Constant.SID,"");
                SPUtils.put(this,Constant.SHOPNAME,"");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;

        }
    }

    private void login(LoadingDialog dialog) {
        JMessageClient.login(name, Constant.JMPSW, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String LoginDesc) {
                if (responseCode == 0) {
                    Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                    Log.i(TAG, "JM登录成功");
                    dialog.dismiss();
                    startActivity(new Intent(MainActivity.this, FormActivity.class));
                } else {
                    Utils.showToast(MainActivity.this,"您的账号在其他设备登录，请重新登录!");
                    Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                    Log.i(TAG, "JM登录失败");
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        uploadPushId();
        String name = SPUtils.getString(this, Constant.USER_NAME);
        rightNickname.setText(name.equals("") ? "用户名" : name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 150) {
            String modname = data.getStringExtra(Constant.MODNAME);
            tvChangeServer.setText(modname);
        }
    }



    //一键退出程序
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Utils.showToast(this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }
    
    private class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                Log.i(TAG,"showMsg:"+showMsg.toString());
            }
        }
    }

    @Override
    protected void onPause() {
        isForeground = false;
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void uploadPushId() {
        HashMap<String, String> params = new HashMap<>();
        String userid = SPUtils.getString(this, Constant.USER_ID);
        String registerId = SPUtils.getString(this, Constant.REGISTRATION_ID);
        params.put(Constant.USER_ID, userid);
        params.put(Constant.REGISTRATION_ID, registerId);
        Log.i(TAG,"registerId:"+registerId+",userid:"+userid);
        if (!"".equals(registerId)){
            ApiClient.getInstance().mApiService.upPushId(params).compose(RxSchedulers.io_main()).subscribe(baseBean -> {
                int resultCode = baseBean.getResultCode();
                Log.i(TAG,"resultCode:"+resultCode);
            }, throwable -> {

            });
        }
    }
}

package com.suntowns.labeltest;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.suntowns.labeltest.utils.BitmapUtils;
import com.suntowns.labeltest.utils.Constant;
import com.suntowns.labeltest.utils.SPUtils;
import com.suntowns.labeltest.utils.Utils;
import com.suntowns.labeltest.utils.Xml2Json;
import com.suntowns.labeltest.widget.AppleDialog;
import com.suntowns.labeltest.widget.CircleImageView;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.rl_device_setting)
    RelativeLayout rlDeviceSetting;
    @BindView(R.id.rl_device_list)
    RelativeLayout rlDeviceList;
    @BindView(R.id.rl_order_center)
    RelativeLayout rlOrderCenter;
    @BindView(R.id.rl_my)
    RelativeLayout rlMy;
    @BindView(R.id.iv_exit)
    ImageView ivExit;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_good_fitting_num)
    TextView tvGoodFittingNum;
    private CircleImageView ivUploadPhoto;
    private OkHttpClient client;
    private Bitmap newBitmap;
    private static final int SCALE = 5;
    private static final int MAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private String userName;
    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        ivUploadPhoto = (CircleImageView) findViewById(R.id.iv_upload_photo);
        tvGoodFittingNum.setVisibility(View.GONE);
        init();
    }

    private void init() {
        String nickname = SPUtils.getString(this, Constant.NICKNAME);
        userName = SPUtils.getString(this, Constant.LOGIN_USER_NAME);
        String jpushID = SPUtils.getString(this, Constant.REGISTRATION_ID);
        Log.i("MainActivity","nickname:"+nickname);
        tvNickname.setText(nickname);
        Log.i("Main","jpushID:"+jpushID);
        getServerInfo();
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
                        Utils.showToast(MainActivity.this,"请检查网络");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        String json;
                        try {
                            json = new Xml2Json(is).Pull2Xml();
                            if("".equals(json)){
                                return;
                            }
                            ServerInfoBean serverInfoBean = new Gson().fromJson(json, ServerInfoBean.class);
                            String result = serverInfoBean.getRESULT();
                            if ("0".equals(result)){
                                SPUtils.put(MainActivity.this,Constant.SERVER_IP,serverInfoBean.getSERVER_IP());
                                SPUtils.put(MainActivity.this,Constant.SERVER_PORTNO,serverInfoBean.getSERVER_PORTNO());
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
        }).start();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ssid = Utils.getSsid(wm);
        SPUtils.put(MainActivity.this,Constant.WIFI_SSID,ssid);
        int pushNum = SPUtils.getInt(this, Constant.PUSH_NUM);
        Log.i("jpush","pushNum:"+pushNum);
        if (pushNum!=0){
            tvGoodFittingNum.setText(pushNum+"");
            tvGoodFittingNum.setVisibility(View.VISIBLE);
        }
        isForeground = true;
        String nickName = SPUtils.getString(this, Constant.NICKNAME);
        Log.i("MainActivity", nickName);
        tvNickname.setText(nickName);

        Bitmap path = BitmapUtils.getBitmapFromPath(userName);
        if (path != null) {
            Log.d("MainActivity", userName);
            ivUploadPhoto.setImageBitmap(path);
        } else {
            ivUploadPhoto.setImageResource(R.drawable.user);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


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

    @OnClick({R.id.rl_device_setting, R.id.rl_device_list, R.id.rl_order_center, R.id.rl_my, R.id.iv_exit, R.id.iv_upload_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_device_setting:
                //TODO 设备设置
                startActivity(new Intent(MainActivity.this, NetWifiActivity.class));
                break;
            case R.id.rl_device_list:
                //TODO 设备列表
                break;
            case R.id.rl_order_center:
                //订单管理
                break;
            case R.id.rl_my:
                break;
            case R.id.iv_upload_photo:
                break;
            case R.id.iv_exit:
                Log.i("MainActivity", "点击了");
                showDialog();
                break;
        }
    }

//    private void showPhoto() {
//        final String[] strings = {"拍照", "从相册中选择"};
//        new AppleDialog(this, strings[0], ContextCompat.getColorStateList(this, R.color.manTextColor), strings[1], ContextCompat.getColorStateList(this, R.color.colorAccent)).setmDialogListener(new AppleDialog.AppleDialogListener() {
//            @Override
//            public void onTopClick() {
//                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
//                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
//                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                startActivityForResult(openCameraIntent, MAKE_PICTURE);
//            }
//
//            @Override
//            public void onBottomClick() {
//                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                openAlbumIntent.setType("image/*");
//                startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
//            }
//        }).show();
//    }

    private void showDialog() {
        String[] strings = {"退出登录", "退出程序"};
        new AppleDialog(this, strings[0], ContextCompat.getColorStateList(this, R.color.manTextColor), strings[1], ContextCompat.getColorStateList(this, R.color.colorAccent)).setmDialogListener(new AppleDialog.AppleDialogListener() {
            @Override
            public void onTopClick() {
                SPUtils.put(MainActivity.this, Constant.MEMID, "");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onBottomClick() {
                System.exit(0);//正常退出App
            }
        }).show();
    }

}


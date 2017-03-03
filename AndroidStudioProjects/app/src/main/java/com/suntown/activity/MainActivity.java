package com.suntown.activity;

import android.Manifest;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.suntown.R;
import com.suntown.api.ApiService;
import com.suntown.bean.AlipayBean;
import com.suntown.bean.AvatarBean;
import com.suntown.bean.LoginBean;
import com.suntown.bean.PushNum;
import com.suntown.bean.ServerInfoBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.BitmapUtils;
import com.suntown.utils.Constant;
import com.suntown.utils.ImageTools;
import com.suntown.utils.JpushUtil;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;
import com.suntown.utils.Xml2String;
import com.suntown.widget.AppleDialog;
import com.suntown.widget.CircleImageView;
import com.suntown.widget.WaveView;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
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

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
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
    @BindView(R.id.tv_fm_num)
    TextView tvFmNum;
    @BindView(R.id.wave_view)
    WaveView waveView;
    private CircleImageView ivUploadPhoto;
    private OkHttpClient client;
    private Bitmap newBitmap;
    private static final int SCALE = 5;
    private static final int MAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private String userName;
    public static boolean isForeground = false;
    private MessageReceiver mMessageReceiver;
    private String memid;
    private String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        memid = SPUtils.getString(this, Constant.MEMID);
        ivUploadPhoto = (CircleImageView) findViewById(R.id.iv_upload_photo);
        user_name = SPUtils.getString(this, Constant.LOGIN_USER_NAME);
        tvGoodFittingNum.setVisibility(View.GONE);
        registerMessageReceiver();
        getAliPay();
    }

    private void initPermission() {
        RxPermissions.getInstance(this).request(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
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
        String nickname = SPUtils.getString(this, Constant.NICKNAME);
        userName = SPUtils.getString(this, Constant.LOGIN_USER_NAME);
        String jpushID = SPUtils.getString(this, Constant.REGISTRATION_ID);
        Log.i("MainActivity", "nickname:" + nickname);
        tvNickname.setText(nickname);
        Log.i("Main", "jpushID:" + jpushID);
        getServerInfo();
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ssid = Utils.getSsid(wm);
        SPUtils.put(MainActivity.this, Constant.WIFI_SSID, ssid);

        String avatar = SPUtils.getString(this, Constant.AVATAR);
        Log.d("MainActivity", "avatar:" + avatar.toString());
        //获取pushNum
        if (!"".equals(avatar)) {
            String url = Utils.replaceString(avatar);
            Log.i(TAG, "url:" + url);
            Picasso.with(this).load(url).error(R.drawable.no_photo).into(ivUploadPhoto);
        } else {
            Bitmap path = BitmapUtils.getBitmapFromPath(userName);
            if (path != null) {
                Log.d("MainActivity", userName);
                ivUploadPhoto.setImageBitmap(path);
            } else {
                ivUploadPhoto.setImageResource(R.drawable.user);
            }
        }
        uploadJpush();
        isForeground = true;
        String nickName = SPUtils.getString(this, Constant.NICKNAME);
        Log.i("MainActivity", nickName);
        tvNickname.setText(nickName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPermission();
        getVerifyMessage(memid);
    }

    //获取服务器信息
    private void getServerInfo() {
        final Request request = new Request.Builder()
                .url(Constant.BASE_HOST + "takeServerInfo")
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                        Utils.showToast(MainActivity.this,"请检查网络");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = response.body().byteStream();
                    String json;
                    try {
                        json = new Xml2Json(is).Pull2Xml();
                        if ("".equals(json)) {
                            return;
                        }
                        ServerInfoBean serverInfoBean = new Gson().fromJson(json, ServerInfoBean.class);
                        Log.i(TAG, "serverInfoBean:" + serverInfoBean.toString());
                        String result = serverInfoBean.getRESULT();
                        if ("0".equals(result)) {
                            SPUtils.put(MainActivity.this, Constant.SERVER_IP, serverInfoBean.getSERVER_IP());
                            SPUtils.put(MainActivity.this, Constant.SERVER_PORTNO, serverInfoBean.getSERVER_PORTNO());
                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }
            });
        }).start();
    }


    public static final String MESSAGE_RECEIVED_ACTION = "com.suntown.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    private void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);

    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            getVerifyMessage(memid);
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String title = intent.getStringExtra(KEY_TITLE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();

                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!JpushUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                Utils.showToast(context, "showMsg:" + showMsg);
                BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(MainActivity.this);
                builder.statusBarDrawable = R.mipmap.ic_launcher;
                builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                        | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
                builder.notificationDefaults = Notification.DEFAULT_SOUND
                        | Notification.DEFAULT_VIBRATE
                        | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
                JPushInterface.setPushNotificationBuilder(1, builder);
            }
        }
    }

    private void getAliPay() {
//        http://www.iesl.com.cn/axis2/services/sunteslwebservice/getAlipay
        final Request request = new Request.Builder().url(Constant.formatTEST_HOST("getAlipay")).get().build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = response.body().byteStream();
                    String json;
                    try {
                        json = new Xml2Json(is).Pull2Xml();
                        AlipayBean alipayBean = new Gson().fromJson(json, AlipayBean.class);
                        String alipaysfid = alipayBean.getALIPAYSFID();
                        String alipaysfzh = alipayBean.getALIPAYSFZH();
                        String alipaysfrsa = alipayBean.getALIPAYSFRSA();
                        SPUtils.put(MainActivity.this, Constant.ALIPAYSFID, alipaysfid);
                        SPUtils.put(MainActivity.this, Constant.ALIPAYSFZH, alipaysfzh);
                        SPUtils.put(MainActivity.this, Constant.ALIPAYSFRSA, alipaysfrsa);
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
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void getVerifyMessage(String memid) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, memid);
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).getMessageCount(params).compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG, "s:" + s);
//                <ns:getPushNUMsResponse xmlns:ns="http://services.suntown.com"><ns:return>
//                </ns:return></ns:getPushNUMsResponse>
            try {
                String json = new Xml2String(s).Pull2Xml();
                PushNum pushNum = new Gson().fromJson(json, PushNum.class);
                int pushnum = pushNum.PUSHNUM;
                int fm_pushnum = pushNum.FM_PUSHNUM;
                if (fm_pushnum > 0) {
                    tvFmNum.setVisibility(View.VISIBLE);
                    tvFmNum.setText(fm_pushnum > 99 ? "99" : fm_pushnum + "");
                    SPUtils.put(MainActivity.this, Constant.PUSH_FM_NUM, fm_pushnum);
                } else {
                    tvFmNum.setVisibility(View.GONE);
                }
                if (pushnum > 0) {
                    tvGoodFittingNum.setVisibility(View.VISIBLE);
                    tvGoodFittingNum.setText(pushnum > 99 ? "99" : pushnum + "");
                } else {
                    tvGoodFittingNum.setVisibility(View.GONE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);

        });
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
//                startActivity(new Intent(MainActivity.this, NetWifiActivity.class));
                break;
            case R.id.rl_device_list:
                //TODO 设备列表
                startActivity(new Intent(MainActivity.this, DeviceListActivity.class));
                break;
            case R.id.rl_order_center:
                //订单管理
                startActivity(new Intent(MainActivity.this, OrderCenterActivity.class));
                break;
            case R.id.rl_my:
                startActivity(new Intent(MainActivity.this, MyActivity.class));
                break;
            case R.id.iv_upload_photo:
                showPhoto();
                break;
            case R.id.iv_exit:
                Log.i("MainActivity", "点击了");
                showDialog();
                break;
        }
    }

    private void showPhoto() {
        final String[] strings = {"拍照", "从相册中选择"};
        new AppleDialog(this, strings[0], ContextCompat.getColorStateList(this, R.color.manTextColor), strings[1], ContextCompat.getColorStateList(this, R.color.colorAccent)).setmDialogListener(new AppleDialog.AppleDialogListener() {
            @Override
            public void onTopClick() {
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(openCameraIntent, MAKE_PICTURE);
            }

            @Override
            public void onBottomClick() {
                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setType("image/*");
                startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
            }
        }).show();
    }

    private void showDialog() {
        String[] strings = {"退出登录", "退出程序"};
        new AppleDialog(this, strings[0], ContextCompat.getColorStateList(this, R.color.manTextColor), strings[1], ContextCompat.getColorStateList(this, R.color.colorAccent)).setmDialogListener(new AppleDialog.AppleDialogListener() {
            @Override
            public void onTopClick() {
                SPUtils.put(MainActivity.this, Constant.MEMID, "");
                SPUtils.put(MainActivity.this, Constant.AVATAR, "");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onBottomClick() {
                System.exit(0);//正常退出App
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAKE_PICTURE) {
            //将保存在本地的图片取出并缩小后显示在界面上
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");
            if (bitmap == null) {
                return;
            }
            newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
            //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
            //TODO 上传 图片
            saveAndSetBitmap();
            bitmap.recycle();
//            String base64 = BitmapUtils.bitmapToBase64(newBitmap);
//            upload(base64);
        } else if (requestCode == CHOOSE_PICTURE) {
            ContentResolver resolver = getContentResolver();
            //照片的原始资源地址
            if (data == null) {
                return;
            }
            Uri originalUri = data.getData();
            try {
                //使用ContentProvider通过URI获取原始图片
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                if (photo != null) {
                    //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                    newBitmap = photo.getByteCount() > 512000 ? ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE) : photo;
                    //释放原始图片占用的内存，防止out of memory异常发生
                    saveAndSetBitmap();
                    newBitmap.recycle();
                    photo.recycle();

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAndSetBitmap() {

        ivUploadPhoto.setImageBitmap(BitmapUtils.getBitmapFromPath(user_name));
        String base64 = BitmapUtils.bitmapToBase64(newBitmap);
        upload(base64);
        BitmapUtils.saveBitmap(newBitmap, user_name);
    }

    private void upload(String base64) {
        String token = SPUtils.getString(MainActivity.this, Constant.LOGIN_TOKEN);
        String memid = SPUtils.getString(MainActivity.this, Constant.MEMID);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("memid", memid);
        params.put("context", base64);
        params.put("type", "1");
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
//                addConverterFactory(GsonConverterFactory.create())
        addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        service.updatePic(params).compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG, "s:" + s);
            String result = s.replace("<ns:uploadAvatarResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            Log.i(TAG, "result-" + result);
            result = result.replace("</ns:return></ns:uploadAvatarResponse>", "");
            Log.i(TAG, "result-" + result);
//               Log.i(TAG, "s:" + json);
            AvatarBean avatarBean = new Gson().fromJson(result, AvatarBean.class);
            if ("0".equals(avatarBean.RESULT)) {
                Utils.showToast(MainActivity.this, "头像上传成功");
                ivUploadPhoto.setImageBitmap(BitmapUtils.getBitmapFromPath(user_name));
                SPUtils.put(MainActivity.this, Constant.AVATAR, "");
            } else {

            }
        }, throwable -> {
            Log.i(TAG, throwable.toString());
        });

    }

    private void uploadJpush() {
        String registrationID = SPUtils.getString(this, Constant.REGISTRATION_ID);
        String memid = SPUtils.getString(MainActivity.this, Constant.MEMID);
        Log.i(TAG, "registrationID:" + registrationID);
        Log.i(TAG, "memid:" + memid);
        RequestBody formBody = new FormBody.Builder()
                .add("arg0", memid)
                .add("arg1", registrationID)
                .build();
        final Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST("storePushID"))
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = response.body().byteStream();
                    Log.i("MainActivity", is.toString());
                    String json;
                    try {
                        json = new Xml2Json(is).Pull2Xml();
                        Log.i("MainActivity", json + "");
                        LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                        String result = loginBean.getRESULT();
                        Log.d("LoginActivity", "loginBean:" + loginBean.getRESULT());
                        if (result.equals("0")) {
                            Log.i("Main", "JpushID上传成功");
                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }

                }
            });
        }).start();
    }
}


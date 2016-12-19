package com.suntown.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.bean.LoginBean;
import com.suntown.utils.BitmapUtils;
import com.suntown.utils.Constant;
import com.suntown.utils.ImageTools;
import com.suntown.utils.SPUtils;
import com.suntown.utils.StatusBarCompat;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;
import com.suntown.widget.AppleDialog;
import com.suntown.widget.CircleImageView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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

public class MyActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.rl_choose_photo)
    RelativeLayout rlChoosePhoto;
    @BindView(R.id.rl_choose_nick)
    RelativeLayout rlChooseNick;
    @BindView(R.id.rl_choose_sax)
    RelativeLayout rlChooseSax;
    @BindView(R.id.rl_choose_psw)
    RelativeLayout rlChoosePsw;
    @BindView(R.id.rl_enter_contacts)
    RelativeLayout rlEnterContacts;
    @BindView(R.id.rl_choose_address)
    RelativeLayout rlChooseAddress;
    @BindView(R.id.tv_sax)
    TextView tvSax;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;

    private static final int SCALE = 5;
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    @BindView(R.id.tv_psw)
    TextView tvPsw;
    private Bitmap newBitmap;
    private String base64;
    private OkHttpClient client;
    String[] strings = {"男", "女"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        StatusBarCompat.compat(this);
        ButterKnife.bind(this);
        client = new OkHttpClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        String nickName = SPUtils.getString(this, Constant.NICKNAME);
        String userName = SPUtils.getString(this, Constant.LOGIN_USER_NAME);
//        String psw = SPUtils.getString(this, Constant.LOGIN_PWD);
        String sex = SPUtils.getString(this, Constant.SEX);
        Log.i("MyActivity",sex);
        tvSax.setText(sex);
        Log.i("MyActivity", nickName);
        tvNickName.setText(nickName);
        Log.i("MyActivity", userName);
        tvNumber.setText(userName);
        Bitmap path = new BitmapUtils().getBitmapFromPath(userName);
        if (path != null) {
            ivHeader.setImageBitmap(path);
        } else {
            Log.i("MyActivity", "未找到本地图片");
            ivHeader.setImageResource(R.drawable.user);
        }
    }

    @OnClick({R.id.iv_back, R.id.rl_choose_photo, R.id.rl_choose_nick, R.id.rl_choose_sax, R.id.rl_choose_psw, R.id.rl_enter_contacts, R.id.rl_choose_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_choose_photo:
                //TODO Dialog
                Log.i("MyActivity", "dialog");
                showPhotoDialog();
                break;
            case R.id.rl_choose_nick:
                //TODO 更改昵称
                startActivity(new Intent(MyActivity.this, UpdateNickNameActivity.class));
                break;
            case R.id.rl_choose_sax:
                showDialog();
                break;
            case R.id.iv_back:
                upDateUserInfo();
                break;
            case R.id.rl_choose_psw:
                //TODO 更改密码
                startActivity(new Intent(MyActivity.this, UpdatePswActivity.class));
                break;
            case R.id.rl_enter_contacts:
                //TODO 进入联系人界面
                startActivity(new Intent(MyActivity.this, ContactsActivity.class));
                break;
            case R.id.rl_choose_address:
                //TODO 更改地址
                startActivity(new Intent(MyActivity.this, AddressCenterActivity.class));
                break;
        }
    }


    private void showPhotoDialog() {
        final String[] strings = {"拍照", "从相册中选择"};
        new AppleDialog(this, strings[0], ContextCompat.getColorStateList(this, R.color.manTextColor), strings[1], ContextCompat.getColorStateList(this, R.color.colorAccent)).setmDialogListener(new AppleDialog.AppleDialogListener() {
            @Override
            public void onTopClick() {
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
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
        new AppleDialog(this, strings[0], ContextCompat.getColorStateList(this, R.color.manTextColor), strings[1], ContextCompat.getColorStateList(this, R.color.colorAccent)).setmDialogListener(new AppleDialog.AppleDialogListener() {
            @Override
            public void onTopClick() {
                tvSax.setText(strings[0]);
                SPUtils.put(MyActivity.this, Constant.SEX, strings[0]);
            }

            @Override
            public void onBottomClick() {
                tvSax.setText(strings[1]);
                SPUtils.put(MyActivity.this, Constant.SAX, strings[1]);
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TAKE_PICTURE) {
            //将保存在本地的图片取出并缩小后显示在界面上
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");
            //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
            newBitmap = bitmap.getByteCount() > 512000 ? ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE) : bitmap;
            saveAndSetBitmap();
            bitmap.recycle();

//            ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
        } else if (requestCode == CHOOSE_PICTURE) {
            ContentResolver resolver = getContentResolver();
            //照片的原始资源地址
            Uri originalUri = data.getData();
            try {
                //使用ContentProvider通过URI获取原始图片
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                if (photo != null) {
                    //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                    newBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                    //释放原始图片占用的内存，防止out of memory异常发生
                    saveAndSetBitmap();
                    newBitmap.recycle();//TODO
                    photo.recycle();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return;
        }
    }

    private void saveAndSetBitmap() {
        ivHeader.setImageBitmap(newBitmap);
        base64 = BitmapUtils.bitmapToBase64(newBitmap);
        BitmapUtils.saveBitmap(newBitmap, SPUtils.getString(this, Constant.LOGIN_USER_NAME));
//        upload(base64);
    }

    private void upload(String base64) {
        RequestBody formBody = new FormBody.Builder()
                .add("token", SPUtils.getString(MyActivity.this, Constant.LOGIN_TOKEN))
                .add(Constant.MEMID, SPUtils.getString(MyActivity.this, Constant.MEMID))
                .add("context", base64)
                .add("type", "1")
                .build();
        final Request request = new Request.Builder()
                .url(Constant.format("uploadAvatar"))
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Utils.showToast(MyActivity.this, "联网失败，请检查网络");
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
                        if (result.equals("0")) {
                            Utils.showToast(MyActivity.this, "头像上传成功");
                        } else if (result.equals("")) {
                            Utils.showToast(MyActivity.this, "头像上传失败");
                        } else {
                            Utils.showToast(MyActivity.this, "头像上传失败");
                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }
            });
        }).start();
    }

    private void upDateUserInfo() {
        RequestBody formBody = new FormBody.Builder()
                .add(Constant.MEMID, SPUtils.getString(MyActivity.this, Constant.MEMID))
                .add(Constant.LOGIN_TOKEN, SPUtils.getString(MyActivity.this, Constant.LOGIN_TOKEN))
                .add(Constant.SEX, tvSax.getText().toString())
                .add("loginname", SPUtils.getString(MyActivity.this, Constant.LOGIN_USER_NAME))
                .add("cardno", "")
                .add("age", "")
                .add("address", "")
                .add("name", SPUtils.getString(MyActivity.this, Constant.LOGIN_USER_NAME))
                .build();
        final Request request = new Request.Builder()
                .url(Constant.format("addUserInfo"))
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Utils.showToast(MyActivity.this, "联网失败，请检查网络");
                finish();
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
                    if (loginBean==null){
                        return;
                    }
                    String result = loginBean.getRESULT();
                    Log.d("LoginActivity", "loginBean:" + loginBean.getRESULT());
//                    if ("0".equals(result)) {
//                        runOnUiThread(() -> finish());
//                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        finish();
    }
}

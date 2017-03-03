package com.suntown.activity;

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
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.suntown.R;
import com.suntown.api.ApiService;
import com.suntown.bean.AddUserInfo;
import com.suntown.bean.AvatarBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.BitmapUtils;
import com.suntown.utils.Constant;
import com.suntown.utils.ImageTools;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.widget.AppleDialog;
import com.suntown.widget.CircleImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class MyActivity extends BaseActivity {

    private static final String TAG = "MyActivity";
    private static final int SCALE = 5;
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_choose_nick)
    TextView tvChooseNick;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_sax)
    TextView tvSax;
    @BindView(R.id.tv_fm_num)
    TextView tvFmNum;

    private Bitmap newBitmap;
    private String base64;
    private OkHttpClient client;
    String[] strings = {"男", "女"};
    private String nickName;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my);
        setContentView(R.layout.activity_my_update);
        ButterKnife.bind(this);
        client = new OkHttpClient();
    }


    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        nickName = SPUtils.getString(this, Constant.NICKNAME);
        userName = SPUtils.getString(this, Constant.LOGIN_USER_NAME);
//        String psw = SPUtils.getString(this, Constant.LOGIN_PWD);
        String sex = SPUtils.getString(this, Constant.SEX);
        Log.i("MyActivity", sex);
        tvSax.setText(sex);
        Log.i("MyActivity", nickName);
        tvChooseNick.setText(nickName);
        Log.i("MyActivity", userName);
        tvTel.setText(userName);
        String avatar = SPUtils.getString(this, Constant.AVATAR);
        int anInt = SPUtils.getInt(this, Constant.PUSH_FM_NUM);
        if (0 < anInt) {
            tvFmNum.setVisibility(View.VISIBLE);
            tvFmNum.setText(anInt + "");
        } else {
            tvFmNum.setVisibility(View.GONE);
        }
        Log.d("MyActivity", "avatar:" + avatar);

        if (!"".equals(avatar)) {
            String url = Utils.replaceString(avatar);
            Log.i(TAG, "url:" + url);
            Picasso.with(this).load(url).error(R.drawable.no_photo).into(ivHead);
        } else {
            Bitmap path = BitmapUtils.getBitmapFromPath(userName);
            if (path != null) {
                Log.d("MainActivity", userName);
                ivHead.setImageBitmap(path);
            } else {
                ivHead.setImageResource(R.drawable.user);
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_choose_nick, R.id.rl_zxing, R.id.rl_choose_addres, R.id.rl_choose_psw, R.id.rl_device_setting, R.id.rl_enter_contacts, R.id.rl_my, R.id.ll_choose_sax})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_choose_nick:
                //TODO 更改昵称
                startActivity(new Intent(MyActivity.this, UpdateNickNameActivity.class));
                break;
            case R.id.rl_zxing:
                if (!Utils.isFastClick()) {
                    startActivity(new Intent(MyActivity.this, DbarCodeActivity.class));
                }
                break;
            case R.id.rl_choose_addres:
                //TODO 更改地址
                startActivity(new Intent(MyActivity.this, AddressCenterActivity.class));
                break;
            case R.id.rl_choose_psw:
                //TODO 更改密码
                startActivity(new Intent(MyActivity.this, UpdatePswActivity.class));
                break;
            //更改密码
            case R.id.rl_device_setting:
                //TODO 更改密码
                startActivity(new Intent(MyActivity.this, UpdatePswActivity.class));
                break;
            case R.id.rl_enter_contacts:
                //TODO 进入联系人界面
                startActivity(new Intent(MyActivity.this, ContactsActivity.class));
                break;
            case R.id.rl_my:
                //TODO 进入联系人界面
                startActivity(new Intent(MyActivity.this, ContactsActivity.class));
                break;
            case R.id.ll_choose_sax:
                showDialog();
                break;
        }
    }

    /**
     * 选择性别
     */
    private void showDialog() {
        new AppleDialog(this, strings[0], ContextCompat.getColorStateList(this, R.color.manTextColor), strings[1], ContextCompat.getColorStateList(this, R.color.colorAccent)).setmDialogListener(new AppleDialog.AppleDialogListener() {
            @Override
            public void onTopClick() {
                tvSax.setText(strings[0]);
                SPUtils.put(MyActivity.this, Constant.SEX, strings[0]);
                //TODO 上传服务器
                addUserInfo(0);
            }

            @Override
            public void onBottomClick() {
                tvSax.setText(strings[1]);
                SPUtils.put(MyActivity.this, Constant.SEX, strings[1]);
                addUserInfo(1);
            }
        }).show();
    }

    /**
     * 上传用户信息（性别）
     * @param i
     */
    private void addUserInfo(int i) {
//        {@"memid":memid,@"logintoken":loginToken,@"sex":gender,@"loginname":tel,@"cardno":@"",@"age":@"",@"address":@"",@"name":nickName};
        String token = SPUtils.getString(MyActivity.this, Constant.LOGIN_TOKEN);
        String memid = SPUtils.getString(MyActivity.this, Constant.MEMID);
        Map<String, String> params = new HashMap<>();
        params.put(Constant.MEMID, memid);
        params.put(Constant.LOGIN_TOKEN, token);
        params.put(Constant.SEX, i + "");
        params.put(Constant.USER_NAME, userName);
        params.put("cardno", "");
        params.put("age", "");
        params.put("address", "");
        params.put(Constant.NAME, nickName);
        String ip = Constant.HOST;
        Retrofit retrofit = new Retrofit.Builder().
//                addConverterFactory(GsonConverterFactory.create())
        addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).addUserInfo(params).compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "s:" + s.toString());
                String result = s.replace("<ns:addUserInfoResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                Log.i(TAG, "result-" + result);
                result = result.replace("</ns:return></ns:addUserInfoResponse>", "");
                Log.i(TAG, "result-" + result);
                AddUserInfo addUserInfo = new Gson().fromJson(result, AddUserInfo.class);
                if ("0".equals(addUserInfo.RESULT)) {
                    if (addUserInfo.USERINFO.SEX.equals(i)) {
//                        tvSax.setText(strings[i]);
                        SPUtils.put(MyActivity.this, Constant.SEX, strings[i]);
                    }
                }

            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable.toString());
        });
    }

}

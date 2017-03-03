package com.suntown.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.api.ApiService;
import com.suntown.bean.AddRespBean;
import com.suntown.bean.FriendBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.BitmapUtils;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FriendDetialAct extends BaseActivity {

    private static final String TAG = "FriendDetialAct";
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_beizhu)
    RelativeLayout rlBeizhu;
    @BindView(R.id.tv_sax)
    TextView tvSax;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_from)
    TextView tvFrom;
    private FriendBean.RECORDBean recordBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detial);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        FriendBean friendBean = intent.getParcelableExtra(Constant.RECORD_BEAN);
        //1 扫一扫  0 号码查找
        int intExtra = intent.getIntExtra(Constant.INT, 0);
        List<FriendBean.RECORDBean> record = friendBean.RECORD;
        recordBean = record.get(0);
        String url = Utils.replaceString(recordBean.AVATAR);
        Bitmap qrImage = BitmapUtils.getBitmapFromPath(url);
        ivHeader.setImageBitmap(BitmapUtils.createFramedPhoto(55, 55, qrImage, 10));
        tvName.setText(recordBean.NICKNAME);
        String sex = recordBean.SEX;
        if ("1".equals(sex)) {
            tvSax.setText("女");
        } else if ("0".equals(sex)) {
            tvSax.setText("男");
        }
        tvTel.setText(recordBean.TEL);
        if (intExtra==1){
            tvFrom.setText("扫一扫");
        }else if (intExtra==0){
            tvFrom.setText("号码查找");
        }
    }

    @OnClick({R.id.iv_back, R.id.btn_agree})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_agree:
                //TODO 发起请求
                if (!Utils.isFastClick()) {
                    addFriend();
                }
                break;
        }
    }

    private void addFriend() {
        String userName = SPUtils.getString(this, Constant.LOGIN_USER_NAME);
        if (userName.equals(recordBean.TEL)) {
            Utils.showToast(FriendDetialAct.this, "无法添加自己为好友");
            return;
        }
        Map<String, String> params = new HashMap<>();
        Log.i(TAG, "s:" + userName + " ," + recordBean.TEL);
        params.put(Constant.ARG0, userName);
        params.put(Constant.ARG1, recordBean.TEL);
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
//                addConverterFactory(GsonConverterFactory.create())
        addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).addFriend(params).compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG, "s:" + s.toString());
            String result = s.replace("<ns:addFriendResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            result = result.replace("</ns:return></ns:addFriendResponse>", "");
            Log.i(TAG, "s:" + result);
            AddRespBean addRespBean = new Gson().fromJson(result, AddRespBean.class);
            if (addRespBean.RESULT.equals("1")) {
                Utils.showToast(FriendDetialAct.this, "添加成功");
                finish();
            } else if (addRespBean.RESULT.equals("0")) {
                Utils.showToast(FriendDetialAct.this, addRespBean.MSG);
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable.toString());
        });
    }
}

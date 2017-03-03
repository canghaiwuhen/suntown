package com.suntown.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.suntown.R;
import com.suntown.api.ApiService;
import com.suntown.bean.AddListBean;
import com.suntown.bean.BaseBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.BitmapUtils;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

import static com.suntown.utils.Constant.ARG0;

public class AddDetialActivity extends BaseActivity {

    private static final String TAG = "AddDetialActivity";
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
    @BindView(R.id.ll_setting)
    LinearLayout llSetting;
    private AddListBean.RECORDBean recordBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detial);
        ButterKnife.bind(this);
        recordBean = getIntent().getParcelableExtra(Constant.RECORD_BEAN);
        init();
    }

    private void init() {
        Log.i(TAG, "recordBean:" + recordBean.toString());
        llSetting.setVisibility(recordBean.PASSFLAG.equals("-1") ? View.VISIBLE : View.GONE);
        AddListBean.RECORDBean.FMBean fm = recordBean.FM;
        AddListBean.RECORDBean.SELFBean self = recordBean.SELF;
        String userName = SPUtils.getString(this, Constant.NICKNAME);
        if (userName.equals(self.NICKNAME)) {
            String avatar = fm.AVATAR;
//            String url = Utils.replaceString(avatar);
//            Bitmap qrImage = BitmapUtils.getBitmapFromPath(url);
//            ivHeader.setImageBitmap(BitmapUtils.createFramedPhoto(55, 55, qrImage, 10));
            if ("".equals(avatar)) {
                ivHeader.setImageResource(R.drawable.user);
            }else{
                String url = Utils.replaceString(avatar);
                Picasso.with(this).load(url).error(R.drawable.user).into(ivHeader);
//            ivPhoto.setImageBitmap(getHttpBitmap(avatar));
            }
            tvName.setText(fm.NICKNAME);
            String sex = fm.SEX;
            if ("1".equals(sex)){
                tvSax.setText("女");
            }else if ("0".equals(sex)){
                tvSax.setText("男");
            }
            tvTel.setText(fm.TEL);
        } else if (userName.equals(fm.NICKNAME)) {
            String avatar = self.AVATAR;
//            String url = Utils.replaceString(avatar);
//            Bitmap qrImage = BitmapUtils.getBitmapFromPath(url);
//            Bitmap framedPhoto = BitmapUtils.createFramedPhoto(55, 55, qrImage, 10);
//            ivHeader.setImageBitmap(framedPhoto);
            if ("".equals(avatar)) {
                ivHeader.setImageResource(R.drawable.user);
            }else{
                String url = Utils.replaceString(avatar);
                Picasso.with(this).load(url).error(R.drawable.user).into(ivHeader);
//            ivPhoto.setImageBitmap(getHttpBitmap(avatar));
            }
            tvName.setText(self.NICKNAME);
            String sex = self.SEX;
            if ("1".equals(sex)){
                tvSax.setText("女");
            }else if ("0".equals(sex)){
                tvSax.setText("男");
            }
            tvTel.setText(self.TEL);
        }

    }


    @OnClick({R.id.iv_back, R.id.btn_agree, R.id.btn_refuse})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_agree:
                //TODO
                addFriendResp("1");
                break;
            case R.id.btn_refuse:
                addFriendResp("0");
                break;
        }
    }

    private void addFriendResp(String i) {
//        arg0(被添加者memid) arg1(添加者memid)  arg2(0 拒绝，1接受) arg3(messageID)
        String memid = recordBean.MEMID;
        String fmid = recordBean.FMID;
        String id = recordBean.ID;
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0,fmid);
        params.put(Constant.ARG1,memid);
        params.put(Constant.ARG2, i);
        params.put(Constant.ARG3, id);
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
//                addConverterFactory(GsonConverterFactory.create())
        addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).getAddFriendResp(params).compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG,"s:"+s.toString());
                String result = s.replace("<ns:addFriendRespResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                result = result.replace("</ns:return></ns:addFriendRespResponse>", "");
                BaseBean baseBean = new Gson().fromJson(result, BaseBean.class);
                if (baseBean.RESULT.equals("1")) {
                    llSetting.setVisibility(View.GONE);
                    Utils.showToast(AddDetialActivity.this,"添加好友成功");
                }else{
                    Utils.showToast(AddDetialActivity.this,"添加好友失败");
                }
            }
        }, throwable -> {
            Log.i(TAG,"throwable:"+throwable.toString());
        });

    }
}

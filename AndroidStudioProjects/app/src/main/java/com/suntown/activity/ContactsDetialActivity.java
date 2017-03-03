package com.suntown.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.suntown.R;
import com.suntown.adapter.ContactsDetialAdapter;
import com.suntown.adapter.ContactsTAGAdapter;
import com.suntown.bean.UserInfoBean;
import com.suntown.bean.UserTagBean;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;
import com.suntown.widget.CircleImageView;
import com.suntown.widget.RoundAngleImageView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ContactsDetialActivity extends BaseActivity {


    private static final String TAG = "ContactsDetialActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.lv_constant)
    RecyclerView lvConstant;
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @BindView(R.id.tv_no_tag)
    TextView tvNoTag;
    @BindView(R.id.iv_head)
    RoundAngleImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    private OkHttpClient client;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    List<UserTagBean.RECORDBean> userTagBeanRECORD = (List<UserTagBean.RECORDBean>) msg.obj;
                    Log.i("lvConstant", "userTagBeanRECORD:" + userTagBeanRECORD);
//                    ContactsDetialAdapter contactsAdapter = new ContactsDetialAdapter(ContactsDetialActivity.this, userTagBeanRECORD);
                    ContactsTAGAdapter contactsAdapter = new ContactsTAGAdapter(ContactsDetialActivity.this, userTagBeanRECORD);
                    lvConstant.setLayoutManager(new LinearLayoutManager(ContactsDetialActivity.this));
                    lvConstant.setAdapter(contactsAdapter);
                    Log.i("lvConstant", "lvConstant:" + userTagBeanRECORD);
                    contactsAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    tvNoTag.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };
    private String ssid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detial);
        ButterKnife.bind(this);
        ssid = SPUtils.getString(this, Constant.WIFI_SSID);
        UserInfoBean.RECORDBean recordBean = getIntent().getParcelableExtra(Constant.RECORD_BEAN);
        Log.i(TAG, "recordBean:" + recordBean.toString());
        String memid = recordBean.MEMID;
        String avatar = recordBean.AVATAR;
        if ("".equals(avatar)) {
            ivHead.setImageResource(R.drawable.user);
        } else {
            String url = Utils.replaceString(avatar);
            Log.i(TAG, "url:" + url);
            Picasso.with(this).load(url).error(R.drawable.no_photo).into(ivHead);
        }
        client = new OkHttpClient();
        queryServer(memid);
        tvName.setText(recordBean.NICKNAME);
        tvPhoneNum.setText(recordBean.TEL);
    }

    private void queryServer(String memid) {
        Log.i("Okhttp", "memid:" + memid);
        FormBody body = new FormBody.Builder()
                .add("arg0", memid)
                .build();
        final Request request = new Request.Builder().post(body).url(Constant.formatBASE_HOST("Getoked_info")).build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    {
                        InputStream is = response.body().byteStream();
                        String json;
                        try {
                            json = new Xml2Json(is).Pull2Xml();
                            final UserTagBean userTagBean = new Gson().fromJson(json, UserTagBean.class);
                            int rows = userTagBean.getROWS();
                            if (rows != 0) {
                                List<UserTagBean.RECORDBean> userTagBeanRECORD = userTagBean.getRECORD();
                                handler.obtainMessage(1, userTagBeanRECORD).sendToTarget();
                            } else {
                                handler.sendEmptyMessage(2);
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }).start();
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}

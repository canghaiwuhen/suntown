package com.suntown.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.adapter.ContactsAdapter;
import com.suntown.bean.PhoneInfo;
import com.suntown.bean.UserInfoBean;
import com.suntown.utils.Constant;
import com.suntown.utils.Contacts;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

public class ContactsActivity extends Activity {

    @BindView(R.id.btn_satart_contact)
    Button btnSatartContact;
    @BindView(R.id.ll_start_contacts)
    LinearLayout llStartContacts;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_no_constact)
    TextView tvNoConstact;

    private OkHttpClient client;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    contacts = new ArrayList<>();
                    adapter = new ContactsAdapter(contacts, ContactsActivity.this);
                    List<UserInfoBean.RECORDBean> recordList = (List<UserInfoBean.RECORDBean>) msg.obj;
                    Log.i("ContactsActivity","Phoneinfolist:"+phoneInfoList+",recordList"+recordList);
                    if ("".equals(phoneInfoList)||"".equals(recordList)) {
                        tvNoConstact.setVisibility(View.VISIBLE);
                        pbProgress.setVisibility(View.GONE);
                        return;
                    }
                    for (UserInfoBean.RECORDBean recordBean : recordList) {
                        for (PhoneInfo list : phoneInfoList) {
                            if (recordBean.getTEL().equals(list.getPhoneNumber())) {
                                pbProgress.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                                listView.setAdapter(adapter);
                                if (!contacts.contains(recordBean)) {
                                    contacts.add(recordBean);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                        Log.i("Constact", "contacts:" + contacts.size() + ",contacts:" + contacts);
                    }
                    if (contacts.size() == 0) {
                        tvNoConstact.setVisibility(View.VISIBLE);
                        pbProgress.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private ListView listView;
    private List<UserInfoBean.RECORDBean> contacts;
    private ContactsAdapter adapter;
    private List<PhoneInfo> phoneInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        if (contacts!=null){
            contacts.clear();
            adapter.notifyDataSetChanged();
        }
        client = new OkHttpClient();
        listView = (ListView) findViewById(R.id.lv_contacts);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            UserInfoBean.RECORDBean recordBean = contacts.get(position);
            Intent intent = new Intent(ContactsActivity.this, ContactsDetialActivity.class);
            intent.putExtra("recordBean",recordBean);
            startActivity(intent);
        });
    }


    @OnClick({R.id.iv_back, R.id.btn_satart_contact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_satart_contact:
                llStartContacts.setVisibility(View.GONE);
                pbProgress.setVisibility(View.VISIBLE);
                //TODO 扫描联系人  使条目显示 如果没有联系人，显示默认图片
                new Thread(() -> {
                    phoneInfoList = Contacts.getNumber(ContactsActivity.this);
//                  获取同一 wifi下的 用户  和联系人比较  GetMemUsers
                    getMemUsers();
                }).start();
                break;
        }
    }

    private void getMemUsers() {
        String ssid = SPUtils.getString(this, Constant.WIFI_SSID);
        String memid = SPUtils.getString(this, Constant.MEMID);
        FormBody body = new FormBody.Builder()
                //待修改
                .add("arg0", ssid).add("arg1", memid)
                .build();
        final Request request = new Request.Builder().post(body).url(Constant.BASE_HOST + "GetMemUsers").build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> {
                            tvNoConstact.setVisibility(View.VISIBLE);
                            pbProgress.setVisibility(View.GONE);
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        String json;
                        try {
                            json = new Xml2Json(is).Pull2Xml();
                            List<String> tellNum = new ArrayList<>();
                            UserInfoBean userInfoBean = new Gson().fromJson(json, UserInfoBean.class);
                            if (userInfoBean.getROWS() > 0) {
                                List<UserInfoBean.RECORDBean> record = userInfoBean.getRECORD();
                                handler.obtainMessage(1, record).sendToTarget();
                            }else {
                                runOnUiThread(() -> {
                                    tvNoConstact.setVisibility(View.VISIBLE);
                                    pbProgress.setVisibility(View.GONE);
                                });
                            }
                        } catch (XmlPullParserException e) {
                            runOnUiThread(() -> {
                                tvNoConstact.setVisibility(View.VISIBLE);
                                pbProgress.setVisibility(View.GONE);
                            });
                            e.printStackTrace();
                        }
                    }
                });


    }

}

package com.suntown.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.Receiver.MyReceiver;
import com.suntown.adapter.ContactsAdapter;
import com.suntown.api.ApiService;
import com.suntown.bean.LoginBean;
import com.suntown.bean.UserInfoBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.Constant;
import com.suntown.utils.JpushUtil;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.widget.AddPopWindow;
import com.suntown.widget.AppleDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class ContactsActivity extends BaseActivity {

    private static final String TAG = "ContactsActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_normal)
    TextView llNormal;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    private List<UserInfoBean.RECORDBean> recordList = new ArrayList<>();
    private ListView listView;
    private ContactsAdapter adapter;
    private String memid;
    private Handler mHandler;
    public static final int REFRESH = 0x000001;
    private int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        memid = SPUtils.getString(this, Constant.MEMID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recordList.clear();
        getMemUsers(memid);
//        mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == REFRESH) {
//                    recordList.clear();
//                    getMemUsers(memid);
//                }
//                super.handleMessage(msg);
//            }
//        };
//        new MyThread().start();
        listView = (ListView) findViewById(R.id.lv_contacts);
        adapter = new ContactsAdapter(recordList, this);
        listView.setAdapter(adapter);
        adapter.setOnWaitAdapterCallBack(new ContactsAdapter.OnDeleteAdapterCallBack() {
            @Override
            public void deleteItemClick(int position) {
                //删除好友
                UserInfoBean.RECORDBean recordBean = recordList.get(position);
                deleteFriend(recordBean, position);
            }

            @Override
            public void onItemClick(int position) {
                UserInfoBean.RECORDBean recordBean = recordList.get(position);
                Intent intent = new Intent(ContactsActivity.this, ContactsDetialActivity.class);
                intent.putExtra(Constant.RECORD_BEAN, recordBean);
                startActivity(intent);
            }
        });
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//
//        });
        int anInt = SPUtils.getInt(this, Constant.PUSH_FM_NUM);
        if (0 < anInt) {
            tvCount.setText(anInt + "");
            tvCount.setVisibility(View.VISIBLE);
        } else {
            tvCount.setVisibility(View.GONE);
        }
        //获取家庭成员

        llMessage.setOnClickListener(v -> {
            //TODO 进入添加好友界面 resetPushNUM();
            startActivity(new Intent(this, VerifyFriendsActivity.class));
        });
        ivAdd.setOnClickListener(v -> {
            //TODO 弹出dialog
            AddPopWindow addPopWindow = new AddPopWindow(ContactsActivity.this);
            addPopWindow.showPopupWindow(v);
            Intent intent = new Intent(this, AddActivity.class);
            addPopWindow.setmDialogListener(new AddPopWindow.AddPopWindowListener() {
                @Override
                public void onTopClick() {
                    //扫一扫
                    intent.putExtra(Constant.FLAG, 1);
                    startActivity(intent);
                }

                @Override
                public void onBottomClick() {
                    intent.putExtra(Constant.FLAG, 0);
                    startActivity(intent);
                }
            });
        });


    }

//    public class MyThread extends Thread {
//        public void run() {
//            while (!Thread.currentThread().isInterrupted()) {
//                i++;
//                Message msg = new Message();
//                msg.what = REFRESH;
//                mHandler.sendMessage(msg);
//
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    //删除好友 delOKFM
    private void deleteFriend(UserInfoBean.RECORDBean recordBean, int currentPosition) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, memid);
//        128 被删除者
        params.put(Constant.ARG1, recordBean.MEMID);
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
//                addConverterFactory(GsonConverterFactory.create())
        addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).delOKFM(params).compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "s:" + s.toString());
                String result = s.replace("<ns:delOKFMResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                result = result.replace("</ns:return></ns:delOKFMResponse>", "");
                //删除成功之后 判断显示无好友
                LoginBean loginBean = new Gson().fromJson(result, LoginBean.class);
                if (loginBean.getRESULT().equals("1")) {
                    Utils.showToast(ContactsActivity.this, "删除好友成功");
                    recordList.remove(currentPosition);
                    if (0 == recordList.size()) {
                        llNormal.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(ContactsActivity.this, "删除好友失败");
                }
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable.toString());
        });
    }

    //获取好友列表
    private void getMemUsers(String memid) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, memid);
//        params.put(Constant.ARG0, "1070");
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
//                addConverterFactory(GsonConverterFactory.create())
        addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).getFamilyMember(params).compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG, "s:" + s);
            String result = s.replace("<ns:getOKFMListResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            result = result.replace("</ns:return></ns:getOKFMListResponse>", "");
            Log.i(TAG, "s:" + result);
            UserInfoBean userInfoBean = new Gson().fromJson(result, UserInfoBean.class);
            if (userInfoBean.ROWS > 0) {
                llNormal.setVisibility(View.GONE);
                recordList.addAll(userInfoBean.RECORD);
                Log.i(TAG, "record:" + recordList.toString());
                adapter.notifyDataSetChanged();
            }
        }, throwable -> {
            Log.i(TAG, throwable.toString());
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

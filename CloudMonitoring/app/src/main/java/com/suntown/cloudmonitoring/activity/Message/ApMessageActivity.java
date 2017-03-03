package com.suntown.cloudmonitoring.activity.Message;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.WebActivity;
import com.suntown.cloudmonitoring.adapter.QuickAdapter;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.MessageBean;
import com.suntown.cloudmonitoring.bean.UpdateBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class ApMessageActivity extends BaseActivity {

    private static final String TAG = "ApMessageActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_view)
    RecyclerView rlView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    //    @BindView(R.id.swipeLayout)
//    SwipeRefreshLayout swipeLayout;
    private int smsType;
    private String userid;
    private OkHttpClient client;
    private List<MessageBean> beanList = new ArrayList<>();
    private int i = 1;
    Handler handler = new Handler();
    private String serverIP;
    private QuickAdapter quickAdapter;
    private int time = 1000;
    //    String ip = "http://192.168.0.143:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_message);
        ButterKnife.bind(this);
        smsType = getIntent().getIntExtra(Constant.SMSTYPE, 1);
        client = new OkHttpClient();
        Log.i(TAG, "smsType-" + smsType);
        serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverIP)) {
            userid = SPUtils.getString(this, Constant.USER_ID);
            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
            Log.i(TAG, "serverIP1:" + serverIP);
        } else {
            userid = SPUtils.getString(this, Constant.SUB_USER_ID);
            Log.i(TAG, "serverIP2:" + serverIP);
        }
        initNet(i, 20);
        if (smsType == 1) {
            tvTitle.setText("AP监控");
        }
        tvTitle.setText(smsType == 1 ? "AP监控" : (smsType == 6 ? "电量监控" : (smsType == 7 ? "变价监控" : (smsType == 10 ? "注册监控" : "其他"))));
//        swipeLayout.setOnRefreshListener(() -> {
//            //加载数据后停止
//            i=1;
//            beanList.clear();
//            handler.postDelayed(() -> {
//                initNet(i, 20);
//            },2000);
//        });
        rlView.setLayoutManager(new LinearLayoutManager(this));
//        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        fab.attachToRecyclerView(rlView);
        quickAdapter = new QuickAdapter(R.layout.message_item, beanList);
        quickAdapter.setOnLoadMoreListener(() -> {
            i++;
            //加载数据后停止
            handler.postDelayed(() -> initNet(i, 20), time);
        });
        quickAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rlView.setAdapter(quickAdapter);
        //TODO  panduan status  并传递给服务器
        rlView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MessageBean messageBean = beanList.get(i);
                int lookstatus = messageBean.lookstatus;
                String detailUrl = messageBean.detailUrl;
                if (lookstatus == 1) {
                    if (!"".equals(detailUrl)) {
                        Intent intent = new Intent(ApMessageActivity.this, WebActivity.class);
                        intent.putExtra(Constant.WEB_URL, detailUrl);
                        startActivity(intent);
                    } else {
                        Utils.showToast(ApMessageActivity.this, "没有消息详情");
                    }
                } else if (lookstatus == 0) {
                    sendService(messageBean);
                }
            }
        });
        fab.setOnClickListener(view -> {
            if (beanList.size()>40){
                rlView.scrollToPosition(0);
            }else{
                rlView.smoothScrollToPosition(0);
            }
        });
    }

    private void sendService(MessageBean messageBean) {
        int msgid = messageBean.msgid;
        Map<String, String> params = new HashMap<>();
        params.put(Constant.LOOKSTATUS, "1");
        params.put(Constant.ID, msgid + "");
        String ip = Constant.formatBASE_HOST(serverIP);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<UpdateBean> updateMessage = service.getUpdateMessage(params);
        updateMessage.compose(RxSchedulers.io_main()).subscribe(updateBean -> {
            int record = updateBean.record;
            messageBean.lookstatus = record == 1 ? 1 : 0;
            quickAdapter.notifyDataSetChanged();
        }, throwable -> {

        });
    }


    /**
     * 请求数据
     *
     * @param page
     * @param num
     */
    private void initNet(int page, int num) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.SMSTYPE, smsType + "").
                add(Constant.USER_ID, userid).
                add(Constant.PAGE_NUM, page + "").
                add(Constant.NUM_PER_PAGE, num + "").
                build();
        String ip = Constant.formatBASE_HOST(serverIP);
        Log.i(TAG, "ip-" + ip);
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(ip) + "/eslrmsh/phone/sms/getSmsTasksByLookStatus")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
//                    Log.i(TAG, "s-" + s);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<MessageBean>>() {
                    }.getType();
                    List<MessageBean> messageBeanList = gson.fromJson(s, type);
                    if (0 != messageBeanList.size()) {
                        beanList.addAll(messageBeanList);
                        Log.i(TAG, "beanList-" + beanList.size());
                        if (i > 1) {
                            runOnUiThread(() -> quickAdapter.loadMoreComplete());
                        }
                    } else {
                        Utils.showToast(ApMessageActivity.this, "没有数据了");
                        runOnUiThread(() -> quickAdapter.loadMoreEnd());
                    }
                    runOnUiThread(() -> quickAdapter.notifyDataSetChanged());
                }
            });
        }).start();

    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }


}

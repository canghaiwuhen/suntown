package com.suntown.cloudmonitoring.activity.Message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.suntown.cloudmonitoring.xlistview.XListView;

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
import rx.functions.Action1;

public class ApMessageActivity extends Activity implements XListView.IXListViewListener {

    private static final String TAG = "ApMessageActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private QuickAdapter mQuickAdapter;
    private int smsType;
    private String userid;
    private OkHttpClient client;
    private List<MessageBean> beanList = new ArrayList<>();
    private XListView mListView;
    private int i = 1;
    Handler handler = new Handler() ;
    private String serverIP;
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
            Log.i(TAG,"serverIP1:"+serverIP);
        } else {
            userid = SPUtils.getString(this, Constant.SUB_USER_ID);
            Log.i(TAG,"serverIP2:"+serverIP);
        }
        initNet(i, 20);
        if (smsType==1){
            tvTitle.setText("AP监控");
        }
        tvTitle.setText(smsType == 1 ? "AP监控" : (smsType == 6 ? "电量监控" : (smsType==7?"变价监控":(smsType==10?"注册监控":"其他"))));
        mQuickAdapter = new QuickAdapter(this,beanList);
        mListView = (XListView) findViewById(R.id.xListView);
        mListView.setPullLoadEnable(true);
        mListView.setAdapter(mQuickAdapter);
        mListView.setXListViewListener(this);

        //TODO  panduan status  并传递给服务器
        mListView.setOnItemClickListener((adapterView, view, i1, l) -> {
            MessageBean messageBean = beanList.get(i1-1);
            int lookstatus = messageBean.lookstatus;
            String detailUrl = messageBean.detailUrl;
            if (lookstatus==1){
                if (!"".equals(detailUrl)) {
                    Intent intent = new Intent(this,WebActivity.class);
                    intent.putExtra(Constant.WEB_URL,detailUrl);
                    startActivity(intent);
                }else{
                    Utils.showToast(this,"没有消息详情");
                }

            }else if (lookstatus==0){
                sendService(messageBean);
            }
        });
    }

    private void sendService(MessageBean messageBean) {
        int msgid = messageBean.msgid;
        Map<String, String> params = new HashMap<>();
        params.put(Constant.LOOKSTATUS, "1");
        params.put(Constant.ID,msgid+"");
        String ip = Constant.formatBASE_HOST(serverIP);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<UpdateBean> updateMessage = service.getUpdateMessage(params);
        updateMessage.compose(RxSchedulers.io_main()).subscribe(updateBean -> {
            int record = updateBean.record;
            messageBean.lookstatus=record==1?1:0;
            mQuickAdapter.notifyDataSetChanged();
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
        Map<String, String> params = new HashMap<>();
//        params.put(Constant.SMSTYPE, smsType + "");
//        params.put(Constant.USER_ID, userid);
//        params.put(Constant.PAGE_NUM, page + "");
//        params.put(Constant.NUM_PER_PAGE, num + "");
////        String ip = Constant.formatBASE_HOST(serverIP);
//
//        Retrofit retrofit = new Retrofit.Builder().
//                addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
//        ApiService service = retrofit.create(ApiService.class);
//        Observable<MessageBean> message = service.getSmsTaskMessage(params);
//        message.compose(RxSchedulers.io_main()).subscribe(messageBean -> {
//            Log.i(TAG,"messageBean-"+messageBean.toString());
//            if (null ==messageBean){
//                //获取已读信息
//            }else{
//                beanList.add(messageBean);
//            }
//        }, throwable -> {
//
//        });
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
                    Type type = new TypeToken<List<MessageBean>>() {}.getType();
                    List<MessageBean> messageBeanList = gson.fromJson(s, type);
                    if(0!=messageBeanList.size()){
                        beanList.addAll(messageBeanList);
                        Log.i(TAG,"beanList-"+beanList.size());
                    }else{
                        Utils.showToast(ApMessageActivity.this,"没有数据了");
                    }
                    runOnUiThread(() -> mQuickAdapter.notifyDataSetChanged());
                }
            });
        }).start();

    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    /** 下拉刷新 */
    @Override
    public void onRefresh() {
        i++;
        //加载数据后停止
        handler.postDelayed(() -> {
//            initNet(i, 20);
            onLoad();
        },1000);

    }
    /** 上拉加载 */
    @Override
    public void onLoadMore() {
        //加载数据后停止
        i=1;
        beanList.clear();
        handler.postDelayed(() -> {
            initNet(i, 20);
            onLoad();
        },2000);
    }

    /** 停止刷新， */
    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
    }
}

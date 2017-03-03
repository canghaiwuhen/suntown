package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.adapter.ServiceAdapter;
import com.suntown.cloudmonitoring.api.ApiClient;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.ChildServerBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;

public class MyServiceActivity extends BaseActivity {

    private static final String TAG = "MyServiceActivity";
    @BindView(R.id.iv_message)
    TextView ivMessage;
    @BindView(R.id.lv_service)
    ListView lvService;
    private ServiceAdapter adapter;
    private List<ChildServerBean.RECORDBean> record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ButterKnife.bind(this);
        init();
    }


    @OnClick(R.id.iv_message)
    public void onClick() {
        finish();
    }

    private void init() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, SPUtils.getString(this,Constant.USER_ID));
        String serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        String ip = Constant.formatBASE_HOST(serverIP);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        service.getChildServer(params).compose(RxSchedulers.io_main()).subscribe(childServerBean -> {
            if (childServerBean.getROWS()>0) {
                record = childServerBean.getRECORD();
                Log.i(TAG,"record:"+record.toString());
                adapter = new ServiceAdapter(MyServiceActivity.this, record);
                lvService.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else{
                Utils.showToast(this,"连接服务器失败，请检查网络!");
            }
        }, throwable -> {
            Utils.showToast(this,"连接服务器失败，请重试!");
        });
        lvService.setOnItemClickListener((adapterView, view, i, l) -> {
            ChildServerBean.RECORDBean recordBean = record.get(i);
            String modurl = recordBean.getModurl();
            String modname = recordBean.getModname();
            String moduserid = recordBean.getModuserid();
            if (!modurl.equals("")) {
                Intent intent = new Intent();
                SPUtils.put(this,Constant.SUBSERVER_IP,modurl);
                SPUtils.put(this,Constant.MODNAME,modname);
                SPUtils.put(this,Constant.SUB_USER_ID,moduserid);
                SPUtils.put(this,Constant.SID,"");
                SPUtils.put(this,Constant.SHOPNAME,"");
                intent.putExtra(Constant.MODNAME,modname);
                setResult(150, intent);
                finish();
//                overridePendingTransition(R.anim.act_exit, R.anim.act_enter);
//                Utils.showToast(this,":"+SPUtils.getString(this,Constant.SERVER_IP));
            }else{
                Utils.showToast(this,"服务器不存在，请选择其他地址!");
            }
        });
//        ApiClient.getInstance().mApiService.getChildServer(params).compose(RxSchedulers.io_main()).subscribe(childServerBean -> {
//            if (childServerBean.getROWS()>0) {
//                record = childServerBean.getRECORD();
//                adapter = new ServiceAdapter(MyServiceActivity.this, record);
//                lvService.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//            }else{
//                Utils.showToast(this,"连接服务器失败，请检查网络!");
//            }
//        }, throwable -> {
//            Utils.showToast(this,"连接服务器失败，请重试!");
//        });
//        lvService.setOnItemClickListener((adapterView, view, i, l) -> {
//            ChildServerBean.RECORDBean recordBean = record.get(i);
//            String modurl = recordBean.getModurl();
//            String modname = recordBean.getModname();
//            String moduserid = recordBean.getModuserid();
//            if (!modurl.equals("")) {
//                Intent intent = new Intent();
//                SPUtils.put(this,Constant.SERVER_IP,modurl);
//                SPUtils.put(this,Constant.MODNAME,modname);
//                SPUtils.put(this,Constant.USER_ID,moduserid);
//                intent.putExtra(Constant.MODNAME,modname);
//                setResult(150, intent);
//                finish();
////                Utils.showToast(this,":"+SPUtils.getString(this,Constant.SERVER_IP));
//            }else{
//                Utils.showToast(this,"服务器不存在，请选择其他地址!");
//            }
//        });
    }
}

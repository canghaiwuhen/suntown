package com.suntown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.adapter.VerifyAdapter;
import com.suntown.api.ApiService;
import com.suntown.bean.AddListBean;
import com.suntown.bean.BaseBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Xml2String;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.functions.Action1;

public class VerifyFriendsActivity extends BaseActivity {

    private static final String TAG = "VerifyFriendsActivity";
    @BindView(R.id.lv_list)
    ListView lvList;
    @BindView(R.id.tv_no_signal)
    TextView tvNoSignal;
    private String memid;
    List<AddListBean.RECORDBean> recordBeanList = new ArrayList<>();
    VerifyAdapter verifyAdapter = new VerifyAdapter(this, recordBeanList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_friends);
        ButterKnife.bind(this);
        memid = SPUtils.getString(this, Constant.MEMID);
        SPUtils.put(this, Constant.PUSH_FM_NUM, 0);
        resetPushNUM();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //清空消息
        recordBeanList.clear();
        //获取好友验证列表
        getVerifyList(2);
        Observable.timer(100, TimeUnit.MILLISECONDS).subscribe(aLong -> {
            getVerifyList(1);
        });
        lvList.setAdapter(verifyAdapter);
        lvList.setOnItemClickListener((adapterView, view, i, l) -> {
            AddListBean.RECORDBean recordBean = recordBeanList.get(i);
            Intent intent = new Intent(VerifyFriendsActivity.this, AddDetialActivity.class);
            intent.putExtra(Constant.RECORD_BEAN,recordBean);
            startActivity(intent);
        });
    }

    private void getVerifyList(int arg1) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, memid);
        //1 已同意 2 未同意
        params.put(Constant.ARG1, arg1+"");
        params.put(Constant.ARG2, "");
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).getUserFriendReq(params).compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG, "s:" + s);
            String result = s.replace("<ns:getUserFriendReqResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            result = result.replace("</ns:return></ns:getUserFriendReqResponse>", "");
            AddListBean addListBean = new Gson().fromJson(result, AddListBean.class);
            if (0 < addListBean.ROWS) {
                recordBeanList.addAll(addListBean.RECORD);
                Log.i(TAG, "recordBeanList:" + recordBeanList.toString());
                verifyAdapter.notifyDataSetChanged();
            } else {

            }
            if (recordBeanList.size()==0){
                tvNoSignal.setVisibility(View.VISIBLE);
            }else{
                tvNoSignal.setVisibility(View.GONE);
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
        });
    }

    /**
     * 设置验证消息条数
     */
    private void resetPushNUM() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, memid);
        params.put(Constant.ARG1, "2");
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).resetPushNUM(params).compose(RxSchedulers.io_main()).subscribe(s -> {
            try {
                String json = new Xml2String(s).Pull2Xml();
                BaseBean baseBean = new Gson().fromJson(json, BaseBean.class);
                if ("0".equals(baseBean.RESULT)) {
                    Log.i(TAG, "PUSHNUM 重置成功");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }, throwable -> {

        });
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}

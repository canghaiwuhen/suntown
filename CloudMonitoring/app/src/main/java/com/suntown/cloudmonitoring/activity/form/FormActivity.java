package com.suntown.cloudmonitoring.activity.form;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.adapter.FormListAdapter;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.FormListBean;
import com.suntown.cloudmonitoring.chatting.ChatActivity;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;

public class FormActivity extends BaseActivity {

    @BindView(R.id.rl_item)
    RecyclerView rlItem;
    private String userid;
    private String TAG = "FormActivity";
    private FormListAdapter adapter;
    private List<FormListBean.WorkformsBean> workforms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO 获取工单信息 设置适配器 http://192.168.0.12:8080/eslrmsh/phone/workform/queryWorkformByUser?acceptUserId=suntown
        String serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverIP)) {
            userid = SPUtils.getString(this, Constant.USER_ID);
            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        } else {
            userid = SPUtils.getString(this, Constant.SUB_USER_ID);
        }
        new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl("http://192.168.0.12:8080/").
                build().create(ApiService.class).queryWorkForm(userid).compose(RxSchedulers.io_main()).
                subscribe(formListBean -> {
                    Log.i(TAG, "formListBean+++++" + formListBean.toString());
                    workforms = formListBean.workforms;
                    rlItem.setHasFixedSize(true);
                    rlItem.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new FormListAdapter(R.layout.form_item,workforms);
                    rlItem.setAdapter(adapter);
                    rlItem.addOnItemTouchListener(new OnItemClickListener() {
                        @Override
                        public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                            Intent intent = new Intent(FormActivity.this, FormChatActivity.class);
                            Intent intent = new Intent(FormActivity.this, ChatActivity.class);
                            FormListBean.WorkformsBean workformsBean = workforms.get(i);
                            String userid = workformsBean.acceptUser.userid;
                            intent.putExtra(Constant.NAME,userid);
                            startActivity(intent);
                        }
                    });
                }, throwable -> {
                    Log.i(TAG, "throwable+++++" + throwable);
                    Utils.showToast(FormActivity.this, "联网失败，请确认网络");
                });
    }

    @OnClick({R.id.iv_back, R.id.iv_add_form})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_add_form:
                //TODO 添加工单
                startActivity(new Intent(this, ProductFormActivity.class));
                break;
        }
    }
}

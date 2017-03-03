package com.suntown.smartscreen.server;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.data.ServerBean;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChooseServerActvity extends BaseActivity<ServerPresenter, ServerModel> implements ServerContract.View {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl)
    RecyclerView rl;
    private ServerAdapter serverAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_choose_server_actvity;
    }

    @Override
    protected void initView() {
        tvTitle.setText("服务列表");
        //获取服务器列表
        String userId = SPUtils.getString(this, Constant.USER_ID);
        mPresenter.getServer(userId);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    public void getSuccess(ServerBean serverBean) {
        //设置适配器
        List<ServerBean.RECORDBean> record = serverBean.RECORD;
        rl.setHasFixedSize(true);
        rl.setLayoutManager(new LinearLayoutManager(this));
        serverAdapter = new ServerAdapter(R.layout.item,record);
        serverAdapter.openLoadAnimation();
        rl.setAdapter(serverAdapter);
        rl.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                ServerBean.RECORDBean recordBean = (ServerBean.RECORDBean) baseQuickAdapter.getItem(i);
                String modid = recordBean.MODID;
                String modurl = recordBean.MODURL;
                String modname = recordBean.MODNAME;
                SPUtils.put(ChooseServerActvity.this,Constant.MODID,modid);
                SPUtils.put(ChooseServerActvity.this,Constant.MODURL,modurl);
                SPUtils.put(ChooseServerActvity.this,Constant.MODNAME,modname);
                finish();
            }
        });
    }

    @Override
    public void showMsg(String msg) {
        Utils.showToast(this,msg);
    }
}

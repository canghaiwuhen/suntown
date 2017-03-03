package com.suntown.smartscreen.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.melnykov.fab.FloatingActionButton;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.data.AllMainTainBean;
import com.suntown.smartscreen.maintain.allocation.AllocationActivity;
import com.suntown.smartscreen.maintain.detial.MainTainDetialActivity;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainTainActivity extends BaseActivity<MainTainPresenter,MainTainModel> implements MainTainContract.View,BaseQuickAdapter.RequestLoadMoreListener{


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_main)
    RecyclerView rlMain;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    ArrayList<AllMainTainBean.RECORDBean> beanList = new ArrayList<>();
    private MainTainAdapter mainTainAdapter;
    private String modUrl;
    private String userId;
    private int i;

    @Override
    protected int getContentView() {
        return R.layout.activity_main_tain;
    }

    @Override
    protected void initView() {
        tvTitle.setText("模板维护");
        modUrl = SPUtils.getString(this, Constant.MODURL);
        if ("".equals(modUrl)) {
            modUrl = ApiConstant.BASE_URL;
        }
        i=1;
        userId = SPUtils.getString(this, Constant.USER_ID);
        rlMain.setLayoutManager(new LinearLayoutManager(this));
        mainTainAdapter = new MainTainAdapter(beanList);
        mainTainAdapter.setOnLoadMoreListener(this);
        mainTainAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        fab.attachToRecyclerView(rlMain);
        rlMain.setAdapter(mainTainAdapter);
        mainTainAdapter.setOnSwipeAdapterCallBack(new MainTainAdapter.OnSwipeAdapterCallBack() {
            @Override
            public void onItemClick(int position) {
                //TODO 点击设置模板
                String dmcode = beanList.get(position).DMCODE;
                Intent intent = new Intent(MainTainActivity.this, AllocationActivity.class);
                intent.putExtra(Constant.DMCODE,dmcode);
                startActivity(intent);
            }

            @Override
            public void onBottomClick(int position) {
                // todo点击跳转详情
                AllMainTainBean.RECORDBean recordBean = beanList.get(position);
                String dmcode = recordBean.DMCODE;
                String specname = recordBean.SPECNAME;
                Intent intent = new Intent(MainTainActivity.this, MainTainDetialActivity.class);
                intent.putExtra(Constant.DMCODE,dmcode);
                intent.putExtra(Constant.SPECNAME,specname);
                startActivity(intent);
            }
        });
        requestData(userId,i,20,modUrl);
        //rlMain.scrollToPosition(0)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (beanList.size()>20){
                    rlMain.scrollToPosition(0);
                }else{
                    rlMain.smoothScrollToPosition(0);
                }
            }
        });
    }

    private void requestData(String id, int i, int i1, String modUrl) {
        mPresenter.getAllDispMContents(id,"", i +"",i1+"", modUrl);
        this.i++;
        if (i==3){
            fab.show(true);
        }
    }


    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    public void getAllDispMContentsSuccess(AllMainTainBean allMainTainBean) {
        beanList.addAll(allMainTainBean.RECORD);
        mainTainAdapter.notifyDataSetChanged();
        mainTainAdapter.loadMoreComplete();
    }

    @Override
    public void noData() {
        Utils.showToast(this,"没有更多数据了");
        mainTainAdapter.loadMoreEnd();
    }

    @Override
    public void showMsg(String msg) {
        Utils.showToast(this,msg);
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        requestData(userId,i,20,modUrl);
    }
}

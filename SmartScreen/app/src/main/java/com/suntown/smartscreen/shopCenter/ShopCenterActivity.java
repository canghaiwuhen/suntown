package com.suntown.smartscreen.shopCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.data.AllShopBean;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.price.chooseShop.ChooseShopContract;
import com.suntown.smartscreen.price.chooseShop.ChooseShopModel;
import com.suntown.smartscreen.price.chooseShop.ChooseShopPresenter;
import com.suntown.smartscreen.shopCenter.detial.ShopDetialActivity;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;
import com.suntown.smartscreen.weight.LoadingDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class ShopCenterActivity extends BaseActivity<ChooseShopPresenter, ChooseShopModel> implements ChooseShopContract.View {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_serach)
    EditText etSerach;
    @BindView(R.id.rl_main)
    RecyclerView rlMain;
    private String modUrl;
    private ArrayList<AllShopBean.CityBean.ShopBean> shopBeenList;
    private ShopListAdapter shopListAdapter;
    private ArrayList<AllShopBean.CityBean.ShopBean> itemList = new ArrayList<>();
    private LoadingDialog dialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_shop_center;
    }

    @Override
    protected void initView() {
        tvTitle.setText("门店管理");
        dialog = new LoadingDialog(this);
        dialog.show();
        modUrl = SPUtils.getString(this, Constant.MODURL);
        String userId = SPUtils.getString(this, Constant.USER_ID);
        if ("".equals(modUrl)) {
            modUrl = ApiConstant.BASE_URL;
        }
        mPresenter.getUserShops(userId, modUrl);
        shopBeenList = new ArrayList<>();
        etSerach.addTextChangedListener(watcher);
        etSerach.setOnEditorActionListener((textView, i, keyEvent) -> true);
    }


    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    public void getUserShopsSuccess(List<AllShopBean> allShopBeanList) {
        dialog.dismiss();
        for (AllShopBean allShopBean : allShopBeanList) {
            for (AllShopBean.CityBean cityBean : allShopBean.city) {
                for (AllShopBean.CityBean.ShopBean shopBean : cityBean.shop) {
                    shopBeenList.add(shopBean);
                }
            }
        }
        Collections.sort(shopBeenList, (shopBean, t1) -> {
            if (Integer.parseInt(shopBean.id)>Integer.parseInt(t1.id)) {
                return 1;
            }
            return -1;
        });
        itemList.addAll(shopBeenList);
        //设置数据
        rlMain.setHasFixedSize(true);
        rlMain.setLayoutManager(new LinearLayoutManager(this));
        shopListAdapter = new ShopListAdapter(R.layout.item3, shopBeenList);
        shopListAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rlMain.setAdapter(shopListAdapter);
        rlMain.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                AllShopBean.CityBean.ShopBean shopBean = shopBeenList.get(i);
                //TODO
                Intent intent = new Intent(ShopCenterActivity.this, ShopDetialActivity.class);
                intent.putExtra(Constant.SHOP_BEAN,shopBean);
                startActivity(intent);
            }
        });
    }

    @Override
    public void showMsg(String msg) {
        Utils.showToast(this,msg);
    }

    //editText监听
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

        }
        //查询标签
        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            String tag = s.toString();
            //查询标签  并显示
            shopBeenList.clear();
            for (AllShopBean.CityBean.ShopBean shopBean : itemList) {
                if (shopBean.name.contains(tag)||shopBean.id.contains(tag)) {
                    shopBeenList.add(shopBean);
                }
            }
            if (0==shopBeenList.size()||"".equals(tag)) {
                shopBeenList.addAll(itemList);
            }
            Collections.sort(shopBeenList, (shopBean, t1) -> {
                if (Integer.parseInt(shopBean.id)>Integer.parseInt(shopBean.id)) {
                    return 1;
                }
                return -1;
            });
            shopListAdapter.notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}

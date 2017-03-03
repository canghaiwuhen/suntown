package com.suntown.smartscreen.price.chooseShop;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.data.AllShopBean;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;
import com.suntown.smartscreen.weight.FlowLayout;
import com.suntown.smartscreen.weight.LoadingDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChooseShopActivity extends BaseActivity<ChooseShopPresenter, ChooseShopModel> implements ChooseShopContract.View {


    private static final String TAG = "ChooseShopActivity";
    @BindView(R.id.lv_fast)
    ListView lvFast;
    @BindView(R.id.fl_title)
    FlowLayout flTitle;
    ArrayList<String> nameList = new ArrayList<>();
    private MyAdapter nameAdapter;
    private List<String> data;
    private LoadingDialog dialog;
    private List<AllShopBean> allShopBeanList;

    @Override
    protected int getContentView() {
        return R.layout.activity_choose_shop;
    }

    @Override
    protected void initView() {
        dialog = new LoadingDialog(this);
        dialog.show();
        String userid = SPUtils.getString(this, Constant.USER_ID);
        String modUrl = SPUtils.getString(this, Constant.MODURL);
        if ("".equals(modUrl)) {
            modUrl = ApiConstant.BASE_URL;
        }
        //TODO 获取所有门店
        data = new ArrayList<>();
        mPresenter.getUserShops(userid, modUrl);
    }

    /**
     * 联网成功设置数据
     *
     * @param allShopBeanList
     */
    @Override
    public void getUserShopsSuccess(List<AllShopBean> allShopBeanList) {
        this.allShopBeanList = allShopBeanList;
        dialog.dismiss();
        Log.i(TAG, "allShopBeanList:" + allShopBeanList.toString());
        //设置数据
        nameList.clear();
        for (AllShopBean allShopBean : allShopBeanList) {
            String name = allShopBean.name;
            nameList.add(name);
        }
        nameAdapter = new MyAdapter(this, nameList);
        lvFast.setAdapter(nameAdapter);
        lvFast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean isFirst = false;
                boolean isSecond = false;
                lvFast.setVisibility(View.VISIBLE);
                String name = nameList.get(i);
                nameList.clear();
                data.add(name);
                addView(data);
                for (AllShopBean allShopBean : allShopBeanList) {
                    if (allShopBean.name.equals(name)) {
                        isFirst = true;
                        isSecond = false;
                        break;
                    }
                }
                if (!isFirst) {
                    for (AllShopBean allShopBean : allShopBeanList) {
                        for (AllShopBean.CityBean cityBean : allShopBean.city) {
                            if (cityBean.name.equals(name)) {
                                isSecond = true;
                                isFirst = false;
                                break;
                            }
                        }
                    }
                }
                if (!isSecond) {
                    for (AllShopBean allShopBean : allShopBeanList) {
                        for (AllShopBean.CityBean cityBean : allShopBean.city) {
                            for (AllShopBean.CityBean.ShopBean shopBean : cityBean.shop) {
                                if (shopBean.name.equals(name)) {
                                    isSecond = false;
                                    isFirst = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (isFirst) {
                    for (AllShopBean allShopBean : allShopBeanList) {
                        if (allShopBean.name.equals(name)) {
                            List<AllShopBean.CityBean> city = allShopBean.city;
                            for (AllShopBean.CityBean cityBean : city) {
                                nameList.add(cityBean.name);
                            }
                        }
                    }
                } else if (isSecond) {
                    for (AllShopBean allShopBean : allShopBeanList) {
                        for (AllShopBean.CityBean cityBean : allShopBean.city) {
                            if (cityBean.name.equals(name)) {
                                List<AllShopBean.CityBean.ShopBean> shop = cityBean.shop;
                                for (AllShopBean.CityBean.ShopBean shopBean : shop) {
                                    nameList.add(shopBean.name);
                                }
                            }
                        }
                    }
                } else {
                    nameList.clear();
                }
                nameAdapter.notifyDataSetChanged();
            }
        });
        nameAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMsg(String msg) {
        Utils.showToast(this, msg);
    }
    boolean isShop = false;
    @OnClick({R.id.iv_back, R.id.button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.button:
                //TODO 确定
                String name = data.get(data.size() - 1);
                Log.i(TAG, "name:" + name);
                for (AllShopBean allShopBean : allShopBeanList) {
                    for (AllShopBean.CityBean cityBean : allShopBean.city) {
                        for (AllShopBean.CityBean.ShopBean shopBean : cityBean.shop) {
                            if (shopBean.name.equals(name)) {
                                SPUtils.put(this,Constant.SHOP_ID,shopBean.id);
                                SPUtils.put(this,Constant.SHOP_NAME,shopBean.name);
                                isShop = true;
                                break;
                            }
                        }
                    }
                }
                if (isShop){
                    finish();
                }else{
                    Utils.showToast(this,"请选择门店");
                }
                break;
        }
    }

    /**
     * 添加布局
     *
     * @param data
     */
    public void addView(List<String> data) {
        flTitle.removeAllViews();
        for (String tag : data) {
            int ranHeight = dip2px(this, 30);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ranHeight);
            lp.setMargins(dip2px(this, 10), 0, dip2px(this, 10), 0);
            TextView tv = new TextView(this);
            tv.setPadding(dip2px(this, 10), 0, dip2px(this, 10), 0);
            tv.setTextColor(Color.parseColor("#FEB73A"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setText(tag + "  ✕");
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setLines(1);
            tv.setBackgroundResource(R.drawable.item_bg);
            flTitle.addView(tv, lp);
            tv.setOnClickListener(OnTagTouchListener);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 流式布局点击删除
     */
    private View.OnClickListener OnTagTouchListener = view -> {
        String name = ((TextView) view).getText().toString().replace("  ✕", "");
//        Utils.showToast(this,name);
        int i = data.indexOf(name);
        if (i == data.size() - 1) {
            lvFast.setVisibility(View.VISIBLE);
            data.remove(i);
            nameList.clear();
            addView(data);
            //显示外层数据
            if (data.size() == 0) {
                for (AllShopBean allShopBean : allShopBeanList) {
                    nameList.add(allShopBean.name);
                }
            } else if (data.size() == 1) {
                String s = data.get(data.size() - 1);
                for (AllShopBean allShopBean : allShopBeanList) {
                    if (allShopBean.name.equals(s)) {
                        List<AllShopBean.CityBean> city = allShopBean.city;
                        for (AllShopBean.CityBean cityBean : city) {
                            nameList.add(cityBean.name);
                        }
                    }
                }
            } else {
                String s = data.get(data.size() - 1);
                for (AllShopBean allShopBean : allShopBeanList) {
                    for (AllShopBean.CityBean cityBean : allShopBean.city) {
                        if (cityBean.name.equals(s)) {
                            List<AllShopBean.CityBean.ShopBean> shop = cityBean.shop;
                            for (AllShopBean.CityBean.ShopBean shopBean : shop) {
                                nameList.add(shopBean.name);
                            }
                        }
                    }
                }
            }
            nameAdapter.notifyDataSetChanged();
        }
    };

}

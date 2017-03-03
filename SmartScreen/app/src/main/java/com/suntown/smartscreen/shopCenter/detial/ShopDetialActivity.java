package com.suntown.smartscreen.shopCenter.detial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.data.AllShopBean;
import com.suntown.smartscreen.shopCenter.AllocatingTaskActivity;
import com.suntown.smartscreen.shopCenter.fragment.APFragment;
import com.suntown.smartscreen.shopCenter.fragment.BoardFragment;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopDetialActivity extends FragmentActivity {


    private static final String TAG = "ShopDetialActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tabpageindicator)
    TabPageIndicator tabpageindicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public String sid;
    public String modUrl;
    public String userId;
    public static final String TITLE[] = {"AP管理", "模板管理"};
    private List<Fragment> mFragments = new ArrayList<>();

    {
        mFragments.add(new APFragment());
        mFragments.add(new BoardFragment());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detial);
        ButterKnife.bind(this);
        initView();
    }


    protected void initView() {
        AllShopBean.CityBean.ShopBean shopBean = getIntent().getParcelableExtra(Constant.SHOP_BEAN);
        tvTitle.setText(shopBean.name);
        sid = shopBean.id;
        modUrl = SPUtils.getString(this, Constant.MODURL);
        userId = SPUtils.getString(this, Constant.USER_ID);
        if ("".equals(modUrl)) {
            modUrl = ApiConstant.BASE_URL;
        }
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tabpageindicator.setViewPager(viewpager);
    }


    @OnClick({R.id.iv_back, R.id.tv_choose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_choose:
                //TODO
                Intent intent = new Intent(ShopDetialActivity.this, AllocatingTaskActivity.class);
                intent.putExtra(Constant.USER_ID,sid);
                startActivity(intent);
                break;
        }
    }
    private class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem() called");
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.d(TAG, "destroyItem() called");
            super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position];
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递参数
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}

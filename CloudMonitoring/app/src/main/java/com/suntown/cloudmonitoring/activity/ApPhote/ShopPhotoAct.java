package com.suntown.cloudmonitoring.activity.ApPhote;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.ApMonitoring;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.APInfoBean;
import com.suntown.cloudmonitoring.fragment.BaseChartFragment;
import com.suntown.cloudmonitoring.fragment.BaseDetialFragment;
import com.suntown.cloudmonitoring.fragment.BaseFragment;
import com.suntown.cloudmonitoring.fragment.ChartFragment;
import com.suntown.cloudmonitoring.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopPhotoAct extends BaseActivity {

    private static final String TAG = "ShopPhotoAct";
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.pager)
    ViewPager viewPager;
    public List<APInfoBean.RECORDBean> recordList;
    @BindView(R.id.ll_viewgroup)
    LinearLayout llViewgroup;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv2)
    ImageView iv2;
    private List<Fragment> mFragments = new ArrayList<>();
    private ArrayList<ImageView> mDots;
    private LayoutInflater mInflater;
    public int intExtra;

    {
        mFragments.add(new BaseDetialFragment());
        mFragments.add(new BaseChartFragment());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_photo);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        recordList = intent.getParcelableArrayListExtra(Constant.RECORD);
        intExtra = intent.getIntExtra(Constant.NUM, 0);
        tvName.setText(intExtra ==0?"正常AP":"异常AP");
        Log.i(TAG, "recordList" + recordList.toString());
        mInflater = getLayoutInflater();

        mDots = new ArrayList<>();
        mDots.add(iv1);
        mDots.add(iv2);
        mDots.get(0).setImageResource(R.drawable.normal_bg);
        MyPagerAdapter mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),mFragments);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //将所有的dot设置为深色
                for (ImageView imageView : mDots) {
                    imageView.setImageResource(R.drawable.normal_bg);
                }
                mDots.get(position).setImageResource(R.drawable.fab_bg);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_filtrate})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_filtrate:
                intent = new Intent(this, ApMonitoring.class);
                intent.putParcelableArrayListExtra(Constant.All_RECORD, (ArrayList<? extends Parcelable>) recordList);
                startActivity(intent);
                break;
        }
    }
}

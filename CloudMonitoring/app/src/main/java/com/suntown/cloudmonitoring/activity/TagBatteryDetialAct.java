package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.RegisterMor;
import com.suntown.cloudmonitoring.bean.TagDetialBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.viewpagerindicator.TabPageIndicator;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class TagBatteryDetialAct extends Activity {

    private static final String TAG = "TagBatteryDetialAct";
    @BindView(R.id.tv_tag_name)
    TextView tvTagName;
    @BindView(R.id.tabpageindicator)
    TabPageIndicator tabpageindicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private String sid;
    private String tinyip;
    private TagDetialBean tagDetialBean;
    private RegisterMor.RECORDBean recordBean;
    public static final String TITLE[] = {"基本信息", "图文信息"};
    private MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_battery_detial);
        ButterKnife.bind(this);
        recordBean = getIntent().getParcelableExtra("RECORD_BEAN");
        Log.i(TAG, "recordBean--" + recordBean.toString());
        sid = recordBean.SID;
        tinyip = recordBean.TINYIP;
        tvTagName.setText(tinyip);
        adapter = new MyAdapter();
        viewpager.setAdapter(adapter);
        NetService();
    }

    private void NetService() {
        Map<String, String> params = new HashMap<>();
        String userid = SPUtils.getString(this, Constant.USER_ID);
        params.put(Constant.USER_ID, userid);
        params.put(Constant.SID, sid);
        params.put(Constant.TINYIP, tinyip);
        String serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        // TODO
        String ip = Constant.formatBASE_HOST("www.iesl.com.cn/");
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<TagDetialBean> tagDetial = apiService.getTAGDetial(params);
        tagDetial.compose(RxSchedulers.io_main()).subscribe(tagDetialBean -> {
            this.tagDetialBean = tagDetialBean;
            initData();
            tabpageindicator.setVisibility(View.VISIBLE);
        }, throwable -> {

        });
    }

    private void initData() {

        //TODO viewpager切换

//        tvTagName.setText(tinyip);
//        tvShopName.setText(tagDetialBean.sname);
//        tvShopIdName.setText(tagDetialBean.sid);
//        tvApIdName.setText(tagDetialBean.spec);
//        tvApaName.setText(recordBean.APADDR);
//        tvLastRegisTime.setText(recordBean.ACTIVITYDATE);
//        tvName.setText(recordBean.GNAME);
////        tvSize.setText(recordBean.);
//        tvAddress.setText(tagDetialBean.origin);
//        tvGoods.setText(recordBean.AID);
//        tvPrice.setText(tagDetialBean.uptprice+"");
//        tvChangePrice.setText(recordBean.LASTRETDATE);
//        tvBattery.setText(tagDetialBean.battery+"");
//        tvSignal.setText(tagDetialBean.lqi+"");


    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    private class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return TITLE.length==0?0:TITLE.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((View) object));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }
    }
}

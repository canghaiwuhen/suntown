package com.suntown.cloudmonitoring.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.base.BaseApplication;
import com.suntown.cloudmonitoring.bean.InfoBean;
import com.suntown.cloudmonitoring.bean.Person;
import com.suntown.cloudmonitoring.bean.TagDetialBean;
import com.suntown.cloudmonitoring.fragment.BaseInformationFragment;
import com.suntown.cloudmonitoring.fragment.PhoteInformationFragment;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class TagDetialActivity extends BaseActivity {

    private static final String TAG = "TagDetialActivity";
    @BindView(R.id.tabpageindicator)
    TabPageIndicator tabpageindicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    public static final String TITLE[] = {"基本信息", "图文详情"};
    @BindView(R.id.tv_tag)
    TextView tvTag;
    public String tinyip;
    public String sid;
    @BindView(R.id.tv_off)
    TextView tvOff;

    private List<Fragment> mFragments = new ArrayList<>();//Arrays.asList(SimpleFragment.createSimpleFragment(TITLE[0]), SimpleFragment.createSimpleFragment(TITLE[1]), SimpleFragment.createSimpleFragment(TITLE[2]));
    public OkHttpClient client;
    private String serverIP;
    private String userId;
    public TagDetialBean tagDetialBean;
    private DbManager db;

    {
        mFragments.add(new BaseInformationFragment());
        mFragments.add(new PhoteInformationFragment());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detial);
        db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
        ButterKnife.bind(this);
        client = new OkHttpClient();
        Intent intent = getIntent();
        InfoBean infoBean = intent.getParcelableExtra(Constant.INFO_BEAN);
        Log.i(TAG, infoBean.toString());
        tinyip = infoBean.tinyip;
        sid = infoBean.sid;
        Log.i(TAG,"sid-"+sid);
        tvTag.setText(tinyip);
        initData();
        initView();
    }

    private void initData() {
        Map<String, String> params = new HashMap<>();
        serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverIP)) {
            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
            userId = SPUtils.getString(this, Constant.USER_ID);
        } else {
            userId = SPUtils.getString(this, Constant.SUB_USER_ID);
        }
        Log.i(TAG,"serverIP-"+serverIP);
        params.put(Constant.USER_ID, userId);
        params.put(Constant.TINYIP, tinyip);
        params.put(Constant.SID, sid);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(serverIP).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<TagDetialBean> tagDetial = service.getTAGDetial(params);
        tagDetial.compose(RxSchedulers.io_main()).subscribe(tagDetialBean -> {
            this.tagDetialBean=tagDetialBean;
            String poweroff = tagDetialBean.poweroff;
            if (!"开机".equals(poweroff)){
                tvOff.setClickable(false);
                tvOff.setTextColor(Color.GRAY);
            }
            String activityDate = tagDetialBean.activityDate;
            Log.i(TAG, "tagDetialBean--" + tagDetialBean.toString());
        }, throwable -> {

        });
    }


    @OnClick({R.id.iv_back, R.id.tv_off})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_off:
                //关机指令
                offTag();
                break;
        }
    }

    private void offTag() {
        Log.i(TAG,"tinyip:"+tinyip);
        String xml = "<ESLOFF><TINYIP>" + tinyip + "</TINYIP></ESLOFF>";
        submitService(xml);
    }

    //发送关机指令
    private void submitService(String xml) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/axis2/services/STPdaService2/ESLOFF")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    result = result.replace("<ns:ESLOFFResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>","").
                            replace("</ns:return></ns:ESLOFFResponse>","");
                    if (result.equals("0")) {
                        Utils.showToast(TagDetialActivity.this, "提交关机任务成功");
//                        initData();
                        runOnUiThread(() -> {
                            tvOff.setTextColor(Color.parseColor("#555859"));
                            tvOff.setClickable(false);
                            List<Person> personList = null;
                            try {
                                personList = db.selector(Person.class).where("ip", "=", tinyip).
                                        where("sid", "=", sid).where("userId", "=", userId).findAll();
                                if (0!=personList.size()) {
                                     db.delete(personList);
                                    Log.i(TAG,"删除成功");
                                }
                            } catch (DbException e) {
                                e.printStackTrace();
                                Log.i(TAG,"删除失败");
                            }
                        });
                    } else {
                        Utils.showToast(TagDetialActivity.this, "提交关机任务失败");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    Utils.showToast(TagDetialActivity.this, "网络错误，请重试");
                }
            });
        }).start();
    }

    private void initView() {
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tabpageindicator.setViewPager(viewpager);
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

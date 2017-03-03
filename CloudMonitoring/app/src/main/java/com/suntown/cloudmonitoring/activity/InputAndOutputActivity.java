package com.suntown.cloudmonitoring.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.base.BaseApplication;
import com.suntown.cloudmonitoring.fragment.CheckFragment;
import com.suntown.cloudmonitoring.fragment.InputFragment;
import com.suntown.cloudmonitoring.fragment.OutputFragment;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.viewpagerindicator.TabPageIndicator;
import org.xutils.DbManager;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class InputAndOutputActivity extends BaseActivity {

    public static final String TITLE[] = {"入库管理", "出库管理", "盘点管理"};
    private static final String TAG = "InputAndOutputActivity";
    private static final int SCANNIN_GREQUEST_CODE = 1;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tabpageindicator)
    TabPageIndicator tabpageindicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Fragment> mFragments = new ArrayList<>();
    public OkHttpClient client;
    public String sid;
    public String resultStr = "";
    public String serverIP;
    public String userid;
    public DbManager db;
    public String sname;
    private InputMethodManager manager;

    {
        mFragments.add(new InputFragment());
        mFragments.add(new OutputFragment());
        mFragments.add(new CheckFragment());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_and_output);
        client = new OkHttpClient();
        ButterKnife.bind(this);
        initView();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
        serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        SPUtils.put(this,Constant.IN_NUM,"");
        SPUtils.put(this,Constant.OUT_NUM,"");
        if ("".equals(serverIP)){
            userid = SPUtils.getString(this, Constant.USER_ID);
            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        }else{
            userid = SPUtils.getString(this, Constant.SUB_USER_ID);
        }
        sid = getIntent().getStringExtra(Constant.SID);
        String sname = getIntent().getStringExtra(Constant.SNAME);
        tvShopName.setText(sname);
    }

    private void initView() {
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tabpageindicator.setViewPager(viewpager);
    }

    @OnClick({R.id.iv_back, R.id.iv_scanner,R.id.ll_main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_scanner:
                //TODO 扫描
                break;
            case R.id.ll_main:


                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您还没有提交数据，是否确认退出");
        builder.setNegativeButton("确定", (dialogInterface, i) -> {
            finish();
            dialogInterface.dismiss();
        });
        builder.setPositiveButton("取消", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    resultStr = bundle.getString(Constant.RESULT_CODE);
                    Log.i(TAG, "resultStr:" + resultStr);
                }
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

    /**
     * 回收键盘
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
			/*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isActive()){
                inputMethodManager.hideSoftInputFromWindow(InputAndOutputActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}

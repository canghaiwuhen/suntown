package com.suntown.scannerproject.input;

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

import com.suntown.scannerproject.R;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.base.BaseApplication;
import com.suntown.scannerproject.input.fragment.CheckFragment;
import com.suntown.scannerproject.input.fragment.InputFragment;
import com.suntown.scannerproject.input.fragment.OutputFragment;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.SPUtils;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.DbManager;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;

public class InputAndOutputActivity extends BaseActivity {

    public static final String TITLE[] = {"入库管理", "出库管理", "盘点管理"};
    private static final String TAG = "InputAndOutputActivity";
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
        userid = SPUtils.getString(this, Constant.USER_CODE);
        if ("".equals(serverIP)) {
            serverIP = ApiConstant.BASE_URL;
        }
        sid = getIntent().getStringExtra(Constant.SID);
        String sname = getIntent().getStringExtra(Constant.SHOP_NAME);
        Log.i(TAG,"sname:"+sname);
        tvShopName.setText("进销存管理");
    }

    private void initView() {
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tabpageindicator.setViewPager(viewpager);
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
//                showDialog();
                finish();
                break;
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
////            showDialog();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    private void showDialog() {
//        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
//                .setTitleText("提示")
//                .setContentText("您还没有提交数据，是否确认退出")
//                .setCancelText("取消")
//                .setConfirmText("确定")
//                .showCancelButton(true)
//                .setCancelClickListener(sDialog -> sDialog.dismiss()).setConfirmClickListener(sDialog ->{finish(); sDialog.dismiss();})
//                .show();
//    }

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

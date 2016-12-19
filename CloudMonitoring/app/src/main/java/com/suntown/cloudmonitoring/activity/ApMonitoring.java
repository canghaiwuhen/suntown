package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.APInfoBean;
import com.suntown.cloudmonitoring.bean.FiltrateBean;
import com.suntown.cloudmonitoring.fragment.ShowAllFragment;
import com.suntown.cloudmonitoring.fragment.ShowExceptionFragment;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.weight.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class ApMonitoring extends Activity {

    private static final String TAG = "ApMonitoring";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_filtrate)
    TextView tvFiltrate;
    @BindView(R.id.tv_show_exception)
    TextView tvShowException;
    @BindView(R.id.tv_show_all)
    TextView tvShowAll;
    @BindView(R.id.fl_show_fragment)
    FrameLayout flShowFragment;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;

    public List<APInfoBean.RECORDBean> exceptionList;
    public List<APInfoBean.RECORDBean> allList;
    public List<APInfoBean.RECORDBean> record;
    @BindView(R.id.ll_ap_main)
    LinearLayout llApMain;
    private String userid;
    private LoadingDialog dialog;
    Handler handler = new Handler();
    private LinearLayout llNormal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_monitoring);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        llNormal = (LinearLayout) findViewById(R.id.ll_normal);
        ButterKnife.bind(this);
        dialog = new LoadingDialog(this);
        dialog.show();
        init();
    }

    private void init() {
        record = getIntent().getParcelableArrayListExtra(Constant.All_RECORD);
        exceptionList = new ArrayList<>();
        allList = new ArrayList<>();
        llNormal.setVisibility(View.GONE);
        dialog.dismiss();
        llApMain.setVisibility(View.VISIBLE);
        Log.i(TAG, record.toString());
        for (APInfoBean.RECORDBean recordBean : this.record) {
            if (recordBean.STATUS == 0) {
                exceptionList.add(recordBean);
            }
            allList.add(recordBean);
        }
        Log.i(TAG, "exceptionList-" + exceptionList.toString());
        Log.i(TAG, "allList-" + allList.toString());
        showFragment();
//        llApMain.setVisibility(View.GONE);
//        Map<String, String> params = new HashMap<>();
//        String serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
//        if ("".equals(serverIP)) {
//            userid = SPUtils.getString(this, Constant.USER_ID);
//            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
//        } else {
//            userid = SPUtils.getString(this, Constant.SUB_USER_ID);
//        }
//        Log.i(TAG, userid + "  " + serverIP);
//        params.put(Constant.USER_ID, userid);
//        String ip = Constant.formatBASE_HOST(serverIP);
//        Retrofit retrofit = new Retrofit.Builder().
//                addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
//        ApiService service = retrofit.create(ApiService.class);
//        Observable<APInfoBean> apForm = service.getAPForm(params);
//        apForm.compose(RxSchedulers.io_main()).subscribe(apInfoBean -> {
//            Log.i(TAG, "apInfoBean:" + apInfoBean.toString());
//            if (apInfoBean.ROWS > 0) {
//                llNormal.setVisibility(View.GONE);
//                dialog.dismiss();
//                llApMain.setVisibility(View.VISIBLE);
//                exceptionList = new ArrayList<>();
//                allList = new ArrayList<>();
//                record = apInfoBean.RECORD;
//                Log.i(TAG, record.toString());
//                for (APInfoBean.RECORDBean recordBean : this.record) {
//                    if (recordBean.STATUS == 0) {
//                        exceptionList.add(recordBean);
//                    }
//                    allList.add(recordBean);
//                }
//                Log.i(TAG, "exceptionList-"+exceptionList.toString());
//                Log.i(TAG, "allList-"+allList.toString());
//                showFragment();
//                //显示隐藏Fragment
//            } else {
//                //TODO 异常处理
//                llNormal.setVisibility(View.VISIBLE);
//                Log.i(TAG, apInfoBean.toString());
//            }
//        }, throwable -> {
//            tvFiltrate.setClickable(false);
//            llNormal.setVisibility(View.VISIBLE);
//            tvFiltrate.setTextColor(Color.GRAY);
//        });

    }

    private void showFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_show_fragment, new ShowExceptionFragment()).commit();
    }

    @OnClick({R.id.tv_filtrate, R.id.tv_show_exception, R.id.tv_show_all, R.id.iv_back})
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            //筛选
            case R.id.tv_filtrate:
                ArrayList<FiltrateBean> filtrateBean = new ArrayList<>();
                for (APInfoBean.RECORDBean recordBean : record) {
                    FiltrateBean bean = new FiltrateBean(recordBean.ANAME, recordBean.SNAME);
                    filtrateBean.add(bean);
                }
                Set<FiltrateBean> ts = new HashSet<>();
                ts.addAll(filtrateBean);
                filtrateBean.clear();
                filtrateBean.addAll(ts);
                Intent intent = new Intent(this, FiltrateRegisterAct.class);
                intent.putParcelableArrayListExtra("record", filtrateBean);
                startActivityForResult(intent, 200);
                break;
            case R.id.tv_show_exception:
                isShowExcp(true);
                transaction.replace(R.id.fl_show_fragment, new ShowExceptionFragment());
                break;
            case R.id.tv_show_all:
                isShowExcp(false);
                transaction.replace(R.id.fl_show_fragment, new ShowAllFragment());
                break;
            case R.id.iv_back:
                finish();
                break;
        }
        transaction.commit();
    }

    private void isShowExcp(Boolean isShowEx) {
        if (isShowEx) {
            tvShowException.setTextColor(Color.WHITE);
            tvShowException.setBackgroundResource(R.color.colorBlackChange);
            tvShowAll.setTextColor(Color.GRAY);
            tvShowAll.setBackgroundResource(R.color.colorBlackBg);
        } else {
            tvShowException.setTextColor(Color.GRAY);
            tvShowException.setBackgroundResource(R.color.colorBlackBg);
            tvShowAll.setTextColor(Color.WHITE);
            tvShowAll.setBackgroundResource(R.color.colorBlackChange);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 & resultCode == 300) {
            String name = data.getStringExtra(Constant.STRING_DATA);
            if ("".equals(name)) {
                return;
            }
            exceptionList.clear();
            allList.clear();
            for (APInfoBean.RECORDBean recordBean : record) {
                if (recordBean.ANAME.equals(name)) {
                    if (recordBean.STATUS == 0) {
                        exceptionList.add(recordBean);
                    }
                    allList.add(recordBean);
                } else if (recordBean.SNAME.equals(name)) {
                    if (recordBean.STATUS == 0) {
                        exceptionList.add(recordBean);
                    }
                    allList.add(recordBean);
                }
            }
            Log.i(TAG, "exceptionList--" + exceptionList.toString() + ",arrayList--" + allList.toString());
        }
    }
}

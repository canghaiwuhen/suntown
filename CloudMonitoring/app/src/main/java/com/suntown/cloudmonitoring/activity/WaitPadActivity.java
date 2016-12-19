package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.AllShopBean;
import com.suntown.cloudmonitoring.bean.FiltrateBean;
import com.suntown.cloudmonitoring.bean.TagDetialBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class WaitPadActivity extends Activity {

    @BindView(R.id.tv_choose_shop)
    TextView tvChooseShop;
    private List<AllShopBean.RECORDBean> record;
    private String shopName ="";
    private String sid;
    private String TAG = "WaitPadActivity";
    private ArrayList<FiltrateBean> filtrateBean = new ArrayList<>();
    private AllShopBean.RECORDBean bean;
    private String userId;
    private String serverIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_pad);
        ButterKnife.bind(this);
        sid = SPUtils.getString(this,Constant.SID);
        if (!"".equals(sid)){
            shopName = SPUtils.getString(this,Constant.SHOPNAME);
            Log.i(TAG,"shopName+"+shopName+"+sid+"+sid);
            tvChooseShop.setText(shopName);

        }
        initView();
    }

    //查询所有门店信息
    private void initView() {
        Map<String, String> params = new HashMap<>();
        userId = SPUtils.getString(this, Constant.SUB_USER_ID);
        if ("".equals(userId)) {
            userId = SPUtils.getString(this, Constant.USER_ID);
            serverIp = SPUtils.getString(this, Constant.SERVER_IP);
        } else {
            serverIp = SPUtils.getString(this, Constant.SUBSERVER_IP);
        }
        Log.i(TAG, serverIp + "  ");
        params.put(Constant.USER_ID, userId);
//        params.put(Constant.USER_ID, SPUtils.getString(this, Constant.USER_ID));
//        String serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        String ip = Constant.formatBASE_HOST(serverIp);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<AllShopBean> apForm = service.getAllShopInfo(params);
        apForm.compose(RxSchedulers.io_main()).subscribe(allShopBean -> {
            record = allShopBean.RECORD;
            for (AllShopBean.RECORDBean recordBean : this.record) {
                FiltrateBean bean = new FiltrateBean(recordBean.ANAME, recordBean.SNAME);
                filtrateBean.add(bean);
            }
            Log.i(TAG, "filtrateBean--" + filtrateBean);

        }, throwable -> {
            Utils.showToast(WaitPadActivity.this, "数据加载失败，请检查网络");
        });
    }


    @OnClick({R.id.iv_back, R.id.tv_has_goods, R.id.tv_no_goods, R.id.tv_choose_shop, R.id.tv_input_setting})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //有货架数据采集
            case R.id.tv_has_goods:
                if (shopName.equals("")) {
                    showDialog();
                    return;
                }
                if (!Utils.isFastClick()) {
                    intent = new Intent(this, HasGoodsShelfActivity.class);
                    intent.putExtra(Constant.SID, sid);
                    intent.putExtra(Constant.SNAME, shopName);
                    startActivity(intent);
                }
                break;
            //无货架数据采集
            case R.id.tv_no_goods:
                if (shopName.equals("")) {
                    showDialog();
                    return;
                }
                if (!Utils.isFastClick()) {
                    intent = new Intent(this, NoGoodsShelfActivity.class);
                    intent.putExtra(Constant.SID, sid);
                    intent.putExtra(Constant.SNAME, shopName);
                    startActivity(intent);
                }

                break;
            //选择门店
            case R.id.tv_choose_shop:
                //查询店铺
                if (!Utils.isFastClick()) {
                    netService();
                }
                break;
            //进销存管理
            case R.id.tv_input_setting:
                if (shopName.equals("")) {
                    showDialog();
                    return;
                }
                if (!Utils.isFastClick()) {
                    intent = new Intent(this, InputAndOutputActivity.class);
                    intent.putExtra(Constant.SID, sid);
                    intent.putExtra(Constant.SNAME, shopName);
                    startActivity(intent);
                }
                break;
        }
    }

    private void netService() {
        if (filtrateBean.size() == 0) {
            Utils.showToast(WaitPadActivity.this, "数据加载中，请稍后");
            return;
        }
        Intent intent = new Intent(this, FiltrateRegisterAct.class);
        intent.putParcelableArrayListExtra("record", filtrateBean);
        startActivityForResult(intent, 200);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == 300) {
                    String name = data.getStringExtra(Constant.STRING_DATA);
                    if ("".equals(name)) {
                        return;
                    }
                }
                break;
            case 200:
                if (resultCode == 300) {
                    String name = data.getStringExtra(Constant.STRING_DATA);
                    if ("".equals(name)) {
                        return;
                    }
                    for (AllShopBean.RECORDBean recordBean : record) {
                        if (recordBean.SNAME.equals(name)) {
                            tvChooseShop.setText(name);
                            bean = recordBean;
                            sid = recordBean.SID;
                            shopName = name;
                            SPUtils.put(this,Constant.SID,sid);
                            SPUtils.put(this,Constant.SHOPNAME,shopName);
                            Log.i(TAG, "SID:" + sid);
                        }
                    }
                }
                break;
        }

    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("请选择门店").setNegativeButton("取消", (dialogInterface, i) -> {
            dialogInterface.cancel();
        }).setPositiveButton("确定", (dialogInterface, i) -> {
            dialogInterface.cancel();
        });
        dialog.create().show();
    }
}

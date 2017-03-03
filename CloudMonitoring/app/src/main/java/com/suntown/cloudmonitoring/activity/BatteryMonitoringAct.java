package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.adapter.BatteryMorExpandAdapter;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.APInfoBean;
import com.suntown.cloudmonitoring.bean.FiltrateBean;
import com.suntown.cloudmonitoring.bean.Item0;
import com.suntown.cloudmonitoring.bean.RegisterMor;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.weight.LoadingDialog;

import java.util.ArrayList;
import java.util.Collections;
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

public class BatteryMonitoringAct extends BaseActivity {

    @BindView(R.id.tv_filtrate)
    TextView tvFiltrate;
    @BindView(R.id.tv_all_shop_info)
    TextView tvAllShopInfo;
    @BindView(R.id.expand_lv)
    ExpandableListView expandLv;
    @BindView(R.id.ll_show)
    LinearLayout llShow;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    public List<RegisterMor.RECORDBean> record;
    @BindView(R.id.tv_name)
    TextView tvName;
    private Map<String, List<RegisterMor.RECORDBean>> listMap;
    private List<Item0> items;
    private BatteryMorExpandAdapter adapter;
    private LinearLayout llNormal;
    private PopupWindow popupWindow;
    List<RegisterMor.RECORDBean> beanList;
    private LoadingDialog dialog;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mrnitoring);
        ButterKnife.bind(this);
        llNormal = (LinearLayout) findViewById(R.id.ll_normal);
        tvName.setText("电量监控");
        init();
        dialog = new LoadingDialog(this);
        dialog.show();
    }

    private void init() {
        Map<String, String> params = new HashMap<>();
        String userid ;
        String serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverIP)){
            userid = SPUtils.getString(this, Constant.USER_ID);
            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        }else{
            userid = SPUtils.getString(this, Constant.SUB_USER_ID);
        }
        Log.i("userid", "uerid:" + userid);
        params.put(Constant.USER_ID, userid);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(serverIP).build();
        ApiService service = retrofit.create(ApiService.class);
        service.getUserLowBattery(params).compose(RxSchedulers.io_main()).subscribe(registerMor -> {
            record = registerMor.RECORD;
            int rows = registerMor.ROWS;
            for (int i = 0; i < this.record.size(); i++) {
                if (0 == this.record.get(i).BATTERY) {
                    this.record.remove(i);
                }
            }
            Log.i("rows", "rows:" + rows + "    record:" + this.record.toString());
            if (this.record.size() == 0) {
                dialog.dismiss();
                //TODO  显示默认
                llNormal.setVisibility(View.VISIBLE);
                llShow.setVisibility(View.GONE);
            } else {
                initdata();
                dialog.dismiss();
                tvAllShopInfo.setText("所有门店低电量的标签总数:" + rows + "个");
                llNormal.setVisibility(View.GONE);
                llShow.setVisibility(View.VISIBLE);
            }
        }, throwable -> {
            //TODO
            dialog.dismiss();
            llNormal.setVisibility(View.VISIBLE);
            llShow.setVisibility(View.GONE);
        });
    }

    private void initdata() {
        Log.i("record", "record:" + record.toString());
        listMap = new HashMap<>();
        items = new ArrayList<>();
        for (RegisterMor.RECORDBean recordBean : record) {
            if (recordBean == null) {
                break;
            }
            String sname = recordBean.SNAME;
            String sid = recordBean.SID;
            if (listMap.containsKey(sid)) {
                beanList = listMap.get(sid);
                beanList.add(recordBean);
            } else {
                items.add(new Item0(sname, sid));
                beanList = new ArrayList<>();
                beanList.add(recordBean);
            }
            listMap.put(sid, beanList);
        }
        Log.i("listmap", "items-->" + items.toString() + "--listmap-->:" + listMap.toString());
        adapter = new BatteryMorExpandAdapter(this, items, listMap);
        Collections.sort(items, (item0, item1) -> {
            if (Integer.parseInt(item0.sid)>Integer.parseInt(item1.sid)) {
                return 1;
            }
            return -1;
        });
        expandLv.setAdapter(adapter);
        adapter.setOnWaitFlagClickListener(position -> {
            if (expandLv.isGroupExpanded(position)) {  //如果是打开状态则关闭
                expandLv.collapseGroup(position);
            } else { //如果是关闭状态则打开
                expandLv.expandGroup(position);
            }
        });
        expandLv.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String sid = items.get(groupPosition).sid;
            List<RegisterMor.RECORDBean> recordBeanList = listMap.get(sid);
            RegisterMor.RECORDBean recordBean = recordBeanList.get(childPosition);
            Intent intent = new Intent(BatteryMonitoringAct.this, TagBatteryActivity.class);
            intent.putExtra("RECORD_BEAN", recordBean);
            startActivity(intent);
            return true;
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_filtrate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_filtrate:
                if (null!=record) {
                    Intent intent = new Intent(this, FiltrateRegisterAct.class);
                    ArrayList<FiltrateBean> filtrateBean = new ArrayList<>();
                    for (RegisterMor.RECORDBean recordBean : record) {
                        FiltrateBean bean = new FiltrateBean(recordBean.ANAME, recordBean.SNAME);
                        filtrateBean.add(bean);
                    }
                    Set<FiltrateBean> ts = new HashSet<>();
                    ts.addAll(filtrateBean);
                    filtrateBean.clear();
                    filtrateBean.addAll(ts);
                    intent.putParcelableArrayListExtra("record", filtrateBean);
                    startActivityForResult(intent, 200);
                }
                break;
        }
    }

    //更改适配器数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 & resultCode == 300) {
            String name = data.getStringExtra(Constant.STRING_DATA);
            if ("".equals(name)) {
                return;
            }
            items.clear();
            listMap.clear();
            for (RegisterMor.RECORDBean recordBean : record) {
                if (recordBean.ANAME.equals(name)) {
                    String sname = recordBean.SNAME;
                    String sid = recordBean.SID;
                    if (listMap.containsKey(sid)) {
                        beanList = listMap.get(sid);
                        beanList.add(recordBean);
                    } else {
                        items.add(new Item0(sname, sid));
                        beanList = new ArrayList<>();
                        beanList.add(recordBean);
                    }
                    listMap.put(sid, beanList);
                } else if (recordBean.SNAME.equals(name)) {
                    String sname = recordBean.SNAME;
                    String sid = recordBean.SID;
                    if (listMap.containsKey(sid)) {
                        beanList = listMap.get(sid);
                        beanList.add(recordBean);
                    } else {
                        items.add(new Item0(sname, sid));
                        beanList = new ArrayList<>();
                        beanList.add(recordBean);
                    }
                    listMap.put(sid, beanList);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}

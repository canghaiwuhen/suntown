package com.suntown.cloudmonitoring.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.adapter.RegisterExpandAdapter;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.APInfoBean;
import com.suntown.cloudmonitoring.bean.FiltrateBean;
import com.suntown.cloudmonitoring.bean.InfoBean;
import com.suntown.cloudmonitoring.bean.Item0;
import com.suntown.cloudmonitoring.bean.RegisterMor;
import com.suntown.cloudmonitoring.bean.TagInfoBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
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
import rx.Observable;
import rx.functions.Action1;

public class RegisterMrnitoring extends BaseActivity {

    private static final String TAG = "RegisterMrnitoring";
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
    private Map<String, List<RegisterMor.RECORDBean>> listMap;
    private List<Item0> items;
    private RegisterExpandAdapter adapter;
    private LinearLayout llNormal;
    private PopupWindow popupWindow;
    public List<RegisterMor.RECORDBean> beanList;
    private String userid;
    private LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mrnitoring);
        ButterKnife.bind(this);
        llNormal = (LinearLayout) findViewById(R.id.ll_normal);
        init();
        dialog = new LoadingDialog(this);
        dialog.show();
    }

    private void init() {
        Map<String, String> params = new HashMap<>();
        String serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverIP)){
            userid = SPUtils.getString(this, Constant.USER_ID);
            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        }else{
            userid = SPUtils.getString(this, Constant.SUB_USER_ID);
        }
        
        params.put(Constant.USER_ID, userid);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(serverIP).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<RegisterMor> label = service.getNoRegisterLabel(params);
        label.compose(RxSchedulers.io_main()).subscribe(registerMor -> {
            record = registerMor.RECORD;
            int rows = registerMor.ROWS;
            for (int i = 0; i < record.size(); i++) {
                if (0 == record.get(i).BATTERY) {
                    record.remove(i);
                }
            }
            Log.i("rows", "rows:" + rows + "    record:" + record.toString());
            if (0<record.size()){
                initdata();
                dialog.dismiss();
                tvAllShopInfo.setText("所有门店注册失败的标签总数:" + rows + "个");
                llNormal.setVisibility(View.GONE);
                llShow.setVisibility(View.VISIBLE);
            }else{
                dialog.dismiss();
                tvFiltrate.setClickable(false);
                tvFiltrate.setTextColor(Color.GRAY);
                llNormal.setVisibility(View.VISIBLE);
            }
        }, throwable -> {
            dialog.dismiss();
            llNormal.setVisibility(View.VISIBLE);
        });
    }

    //设置数据到adapter
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
        Log.i("listmap", "items-->"+items.toString()+"--listmap-->:" + listMap.toString());

        adapter = new RegisterExpandAdapter(this, items, listMap);
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
            InfoBean infoBean = new InfoBean(recordBean.SID, recordBean.TINYIP);
            Intent intent = new Intent(RegisterMrnitoring.this,TagDetialActivity.class);
            Log.i(TAG,"RegisterMor.RECORDBean--"+infoBean.toString());
            intent.putExtra(Constant.INFO_BEAN,infoBean);
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
                if (null!=record){
                    Log.i(TAG,"record:"+record.toString());
                    ArrayList<FiltrateBean> filtrateBean = new ArrayList<>();
                    for (RegisterMor.RECORDBean recordBean : record) {
                        FiltrateBean bean = new FiltrateBean(recordBean.ANAME, recordBean.SNAME);
                        if (!filtrateBean.contains(bean)) {
                            filtrateBean.add(bean);
                        }
                    }
                    Set<FiltrateBean> ts = new HashSet<>();
                    ts.addAll(filtrateBean);
                    filtrateBean.clear();
                    filtrateBean.addAll(ts);
                    if (!Utils.isFastClick()) {
                        Log.i(TAG,"filtrateBean:"+filtrateBean.toString());
                        Intent intent = new Intent(this,FiltrateRegisterAct.class);
                        intent.putParcelableArrayListExtra("record", filtrateBean);
                        startActivityForResult(intent,200);
                    }
                }
                break;
        }
    }
    //更改适配器数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==200&resultCode==300){
            String name = data.getStringExtra(Constant.STRING_DATA);
            if ("".equals(name)){
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
                }else if (recordBean.SNAME.equals(name)){
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
            Collections.sort(items, (item0, item1) -> {
                if (Integer.parseInt(item0.sid)>Integer.parseInt(item1.sid)) {
                    return 1;
                }
                return -1;
            });
            adapter.notifyDataSetChanged();
        }
    }
}

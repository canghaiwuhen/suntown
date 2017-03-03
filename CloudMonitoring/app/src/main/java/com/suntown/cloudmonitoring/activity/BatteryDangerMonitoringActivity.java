package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.adapter.BatteryExpandAdapter;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.InfoBean;
import com.suntown.cloudmonitoring.bean.Item0;
import com.suntown.cloudmonitoring.bean.Item1;
import com.suntown.cloudmonitoring.bean.RegisterMor;
import com.suntown.cloudmonitoring.bean.TestBAttertDangerBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.weight.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class BatteryDangerMonitoringActivity extends BaseActivity {
    private static final int DURATION = 2000;
    private static final String TAG = "BatteryDangerMonitoringActivity";

    @BindView(R.id.chart)
    ColumnChartView barChart;
    @BindView(R.id.expand_lv)
    ExpandableListView expandLv;
    @BindView(R.id.content)
    LinearLayout Content;
    public List<TestBAttertDangerBean.RECORDBean> recordList;

    private BatteryExpandAdapter adapter;
    private LinearLayout ll_normal;
    private ColumnChartData data;
    private LoadingDialog dialog;
    private Handler handler = new Handler();
    private List<Item1> itemList;
    private HashMap<String, List<TestBAttertDangerBean.RECORDBean.Bean>> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_danger_monitoring);
        ButterKnife.bind(this);
        ll_normal = (LinearLayout) findViewById(R.id.ll_normal);
        init();
    }

    private boolean hasData = false;

    private void init() {
        dialog = new LoadingDialog(this);
        dialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, SPUtils.getString(this, Constant.USER_ID));
        String serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        String ip = Constant.formatBASE_HOST(serverIP);
//        String ip = Constant.formatBASE_HOST("http://192.168.0.143:8080/");
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<TestBAttertDangerBean> batteryForm = service.getBatteryDangerInfo(params);
        batteryForm.compose(RxSchedulers.io_main()).subscribe(batteryDangerBean -> {
            recordList = batteryDangerBean.RECORD;
            Log.i(TAG, "recordList:" + recordList.toString() );
            for (TestBAttertDangerBean.RECORDBean recordBean : recordList) {
                if (recordBean.RECORD.size()!=0) {
                    hasData=true;
                    break;
                }
            }
            if (hasData){
                hashMap = new HashMap<>();
                itemList = new ArrayList<>();
                for (TestBAttertDangerBean.RECORDBean recordBean : recordList) {
                    List<TestBAttertDangerBean.RECORDBean.Bean> record = recordBean.RECORD;
                    Log.i(TAG,"record--"+record.toString());
                    String fallrange = recordBean.FALLRANGE;
                    int rows = recordBean.ROWS;
                    if (rows != 0) {
                        Item1 item1 = new Item1(fallrange, rows);
                        itemList.add(item1);
                        hashMap.put(fallrange, record);
                    }
                    Log.i(TAG,"hashMap"+hashMap.toString());
                }
                Log.i(TAG, "beanList:" + itemList.toString() + "   hashMap " + hashMap.toString());
//                if (hasData) {
                refreshData();
                refreshListData();
                dialog.dismiss();
                ll_normal.setVisibility(View.GONE);
                Content.setVisibility(View.VISIBLE);
            }else{
                dialog.dismiss();
                ll_normal.setVisibility(View.VISIBLE);
                Content.setVisibility(View.GONE);
            }

        }, throwable -> {
            dialog.dismiss();
            ll_normal.setVisibility(View.VISIBLE);
            Content.setVisibility(View.GONE);
        });
    }

    //初始化List数据
    private void refreshListData() {
        adapter = new BatteryExpandAdapter(this, itemList, hashMap);
        expandLv.setAdapter(adapter);
        expandLv.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String fallrange = itemList.get(groupPosition).FALLRANGE;
            List<TestBAttertDangerBean.RECORDBean.Bean> beanList = hashMap.get(fallrange);
            TestBAttertDangerBean.RECORDBean.Bean bean = beanList.get(childPosition);
            InfoBean infoBean = new InfoBean(bean.SID, bean.TINYIP);
            Intent intent = new Intent(BatteryDangerMonitoringActivity.this,TagDetialActivity.class);
            Log.i(TAG,"RegisterMor.RECORDBean--"+infoBean.toString());
            intent.putExtra(Constant.INFO_BEAN,infoBean);
            startActivity(intent);
            return true;
        });
    }

    //初始化chart
    private void refreshData() {
        int numSubcolumns = 1;//设置每个柱状图显示的颜色数量(每个柱状图显示多少块)
        int numColumns = recordList.size();//柱状图的数量
        List<Column> columns = new ArrayList<Column>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            TestBAttertDangerBean.RECORDBean recordBean = recordList.get(i);
            for (int j = 0; j < numSubcolumns; ++j) {
                int rows = recordBean.ROWS;
                values.add(new SubcolumnValue(rows, Color.YELLOW));
                AxisValue value = new AxisValue(i);
                String fallrange = recordBean.FALLRANGE;
                String[] split = fallrange.split("-");
                value.setLabel(split[0] + "%↓" + split[1] + "%");
                axisValues.add(value);
//                values.add(new SubcolumnValue(rows, Color.YELLOW));
//                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, Color.YELLOW));
            }
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }

        data = new ColumnChartData(columns);
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("下降幅度");
        axisY.setName("下降个数");
        axisX.setLineColor(Color.GRAY);
        axisY.setLineColor(Color.GRAY);
        axisX.setTextColor(Color.parseColor("#D94368"));
        axisY.setTextColor(Color.parseColor("#D94368"));
        axisX.setValues(axisValues);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        barChart.setColumnChartData(data);

    }

    @OnClick(R.id.iv_message)
    public void onClick() {
        finish();
    }

}

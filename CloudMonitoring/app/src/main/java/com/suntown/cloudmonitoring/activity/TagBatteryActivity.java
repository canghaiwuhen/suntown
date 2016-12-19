package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.BattVoltageBean;
import com.suntown.cloudmonitoring.bean.InfoBean;
import com.suntown.cloudmonitoring.bean.RegisterMor;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class TagBatteryActivity extends Activity {

    private static final String TAG = "TagBatteryActivity";
    private List<String> xlabel;
    private List<Float> yvalue;
    private ImageView ivAdd;
    private ImageView ivCut;
    private TextView tvTime;
    private LineChartView linechart;
    private TextView tvGoodsName;
    private TextView tvGoodsNum;
    private TextView tvApaddrNum;
    private TextView tvDatial;
    private TextView tvTagName;
    private TextView tvBatteryNum;
    private TextView tvSignalNum;
    private String tinyip;
    private String[] times = new String[]{"一   周", "一个月", "两个月"};
    private int currentPosition = 0;

    private RegisterMor.RECORDBean recordBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_battery);
        ButterKnife.bind(this);
        recordBean = getIntent().getParcelableExtra("RECORD_BEAN");
        Log.i(TAG, "recordBean-->" + recordBean.toString());
        initView();
        //连接网络
        int lqi = recordBean.LQI;
        int battery = recordBean.BATTERY;
        String apaddr = recordBean.APADDR;
        String aid = recordBean.AID;
        String gname = recordBean.GNAME;
        tinyip = recordBean.TINYIP;
        tvSignalNum.setText(lqi + "");
        tvBatteryNum.setText(battery + "");
        tvApaddrNum.setText(apaddr);
        tvGoodsNum.setText(aid);
        tvGoodsName.setText(gname);
        tvTagName.setText(tinyip);
        tvTime.setText(times[currentPosition]);
        ivCut.setImageResource(R.drawable.arrow_l_off);
        ivAdd.setImageResource(R.drawable.arrow_r_on);
        ivCut.setClickable(false);
        ivAdd.setClickable(true);
        connNet(currentPosition+2+"");
    }

    //初始化view
    private void initView() {
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        ivCut = (ImageView) findViewById(R.id.iv_cut);
        tvSignalNum = ((TextView) findViewById(R.id.tv_signal_num));
        tvBatteryNum = ((TextView) findViewById(R.id.tv_battery_num));
        tvTime = ((TextView) findViewById(R.id.tv_time));
        tvTagName = ((TextView) findViewById(R.id.tv_tag_name));
        tvDatial = ((TextView) findViewById(R.id.tv_datial));
        tvGoodsName = ((TextView) findViewById(R.id.tv_goods_name));
        tvGoodsNum = ((TextView) findViewById(R.id.tv_goods_num));
        tvApaddrNum = ((TextView) findViewById(R.id.tv_apaddr_num));
        linechart = ((LineChartView) findViewById(R.id.linechart));
        findViewById(R.id.iv_back).setOnClickListener(view -> finish());
    }

    private void connNet(String SID) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.DATERANGE, SID);
        params.put(Constant.TINYIP, tinyip);
        Log.i(TAG,tinyip+" "+Constant.TINYIP);
        Log.i(TAG,SID+" "+Constant.DATERANGE);
        String serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        // TODO
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(serverIP).build();
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<BattVoltageBean> tagVoltage = apiService.getTAGVoltage(params);
        tagVoltage.compose(RxSchedulers.io_main()).subscribe(battVoltageBean -> {
            xlabel = battVoltageBean.label;
            yvalue = battVoltageBean.value;
            Log.i(TAG, "label:" + xlabel + ",value:" + yvalue);
            initColumnData();
        }, throwable -> {

        });
    }
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> xValues = new ArrayList<>();
    private List<AxisValue> yValues = new ArrayList<>();
    //初始化图表数据
    private void initColumnData() {
//        label x轴数据 value y轴数据
        xValues.clear();
        mPointValues.clear();
        //设置X轴显示
        //图表的每个点的显示
        mPointValues.add(new PointValue(0, yvalue.get(0)-10));
        for (int i = 0; i < xlabel.size(); i++) {
            Float y = yvalue.get(i);
            xValues.add(new AxisValue(i).setLabel(xlabel.get(i)));//为每个对应的i设置相应的label(显示在X轴
                mPointValues.add(new PointValue(i,y));
//            yValues.add(new AxisValue(i).setLabel(y+""));
            yValues.add(new AxisValue(i).setLabel(y+""));//添加Y轴显示的刻度值
        }
        mPointValues.remove(1);
        Log.i(TAG,"mAxisValues:"+xValues+",mPointValues:"+mPointValues);
        initLineChart();
    }

    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#EFA93C")).setCubic(false);  //折线的颜色
        List<Line> lines = new ArrayList<>();
//        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
//        line.setCubic(false);//曲线是否平滑
//        line.setFilled(true);//是否填充曲线的面积
//        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
//        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
//        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.parseColor("#D94368"));  //设置字体颜色
        axisX.setLineColor(Color.GRAY);
        axisX.setName("时间");  //表格名称
        axisX.setTextSize(7);//设置字体大小
        axisX.setMaxLabelChars(7);  //最多几个X轴坐标
        axisX.setValues(xValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        Axis axisY = new Axis();  //Y轴
        axisY.setTextSize(7);//设置字体大小
        axisY.setMaxLabelChars(7); //默认是3，只能看最后三个数字
        axisY.setTextColor(Color.parseColor("#D94368"));
        axisY.setLineColor(Color.GRAY);
        axisY.setName("电量");//y轴标注
        axisY.setHasLines(true);
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //设置行为属性，支持缩放、滑动以及平移
        linechart.setInteractive(true);
        linechart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        linechart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        linechart.setLineChartData(data);
        linechart.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.tv_datial, R.id.iv_cut, R.id.iv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_datial:
                //TODO 跳转到详情界面
                Log.i(TAG,"recordBean:"+recordBean.toString());
                String sid = recordBean.SID;
//                Intent intent = new Intent(TagBatteryActivity.this, TagBatteryDetialAct.class);
                InfoBean infoBean = new InfoBean(sid, tinyip);
                Intent intent = new Intent(TagBatteryActivity.this, TagDetialActivity.class);
                intent.putExtra(Constant.INFO_BEAN, infoBean);
                startActivity(intent);
                break;
            case R.id.iv_cut:
                if(currentPosition==0){
                    ivCut.setClickable(false);
                    ivAdd.setClickable(true);
                    ivCut.setImageResource(R.drawable.arrow_l_off);
                    ivAdd.setImageResource(R.drawable.arrow_r_on);
                }else {
                    ivCut.setClickable(true);
                    ivAdd.setClickable(true);
                    ivAdd.setImageResource(R.drawable.arrow_r_on);
                    ivCut.setImageResource(R.drawable.arrow_l_on);
                    currentPosition--;
                    tvTime.setText(times[currentPosition]);
                    if(currentPosition==0){
                        ivCut.setClickable(false);
                        ivAdd.setClickable(true);
                        ivCut.setImageResource(R.drawable.arrow_l_off);
                        ivAdd.setImageResource(R.drawable.arrow_r_on);
                    }
                    connNet(currentPosition+2+"");
                }
                break;
            case R.id.iv_add:
                if (currentPosition==2){
                    ivAdd.setClickable(false);
                    ivCut.setClickable(true);
                    ivAdd.setImageResource(R.drawable.arrow_r_off);
                    ivCut.setImageResource(R.drawable.arrow_l_on);
                } else{
                    ivAdd.setClickable(true);
                    ivCut.setClickable(true);
                    ivCut.setImageResource(R.drawable.arrow_l_on);
                    ivAdd.setImageResource(R.drawable.arrow_r_on);
                    currentPosition++;
                    tvTime.setText(times[currentPosition]);
                    if (currentPosition==2){
                        ivAdd.setClickable(false);
                        ivCut.setClickable(true);
                        ivAdd.setImageResource(R.drawable.arrow_r_off);
                        ivCut.setImageResource(R.drawable.arrow_l_on);
                    }
                    connNet(currentPosition+2+"");
                }
                break;
        }
    }
}

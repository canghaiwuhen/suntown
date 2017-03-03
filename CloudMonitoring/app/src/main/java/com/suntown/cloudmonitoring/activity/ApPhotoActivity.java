package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.ApPhote.photoDetialAct;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.APInfoBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.weight.LoadingDialog;

import java.math.BigDecimal;
import java.text.NumberFormat;
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

public class ApPhotoActivity extends BaseActivity {


    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.tv_percent)
    TextView tvPercent;
    @BindView(R.id.tv_unusual_num)
    TextView tvUnusualNum;
    @BindView(R.id.tv_normal_num)
    TextView tvNormalNum;
    @BindView(R.id.pb2)
    ProgressBar pb2;
    @BindView(R.id.tv2_percent)
    TextView tv2Percent;
    @BindView(R.id.piechart)
    PieChart pieChart;
    @BindView(R.id.tv_filtrate)
    TextView tvFiltrate;
    private String userid;
    private String TAG = "ApPhotoActivity";
    private LoadingDialog dialog;
    List<APInfoBean.RECORDBean> exceptionList;
    List<APInfoBean.RECORDBean> normalList;
    private List<APInfoBean.RECORDBean> record;
    private int exceptionSize;
    private int normalSize;
    private int mainSize;
    private String p1;
    private String p2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_photo);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ButterKnife.bind(this);
        dialog = new LoadingDialog(this);
        dialog.show();
        init();
    }

    private void init() {
        Map<String, String> params = new HashMap<>();
        String serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverIP)) {
            userid = SPUtils.getString(this, Constant.USER_ID);
            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        } else {
            userid = SPUtils.getString(this, Constant.SUB_USER_ID);
        }
        Log.i(TAG, userid + "  " + serverIP);
        params.put(Constant.USER_ID, userid);
        String ip = Constant.formatBASE_HOST(serverIP);
//        String ip = Constant.formatBASE_HOST("http://192.168.0.152:8090/");
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<APInfoBean> apForm = service.getAPForm(params);
        apForm.compose(RxSchedulers.io_main()).subscribe(apInfoBean -> {
            Log.i(TAG, "apInfoBean:" + apInfoBean.toString());
            exceptionList = new ArrayList<>();
            normalList = new ArrayList<>();
            if (apInfoBean.ROWS > 0) {
                dialog.dismiss();
                record = apInfoBean.RECORD;
                Log.i(TAG, "record:" + record.toString());
                for (APInfoBean.RECORDBean recordBean : record) {
                    if (recordBean.STATUS == 0) {
                        exceptionList.add(recordBean);
                    } else {
                        normalList.add(recordBean);
                    }
                }
                tvFiltrate.setClickable(true);
                tvFiltrate.setTextColor(getResources().getColor(R.color.colorWhite));
                initDate();
            } else {
                //TODO 异常处理
                Log.i(TAG, apInfoBean.toString());
            }
        }, throwable -> {

        });

    }

    private void initDate() {
        mainSize = record.size();
        exceptionSize = exceptionList.size();
        normalSize = normalList.size();
        double percent1 = (double) exceptionSize / (double) mainSize;
        double percent2 = (double) normalSize / (double) mainSize;
        Log.i(TAG, "percent1-" + percent1 + "  percent2-" + percent2);
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(1);
        p1 = nt.format(percent1);
        p2 = nt.format(percent2);
        pb.setProgress(exceptionSize);
        pb.setMax(mainSize);
        pb2.setProgress(normalSize);
        pb2.setMax(mainSize);
        tvPercent.setText(p1);
        tv2Percent.setText(p2);
        tvUnusualNum.setText(exceptionSize + "");
        tvNormalNum.setText(normalSize + "");

        float quarterly1 = (float) (percent1 * 100);
        float quarterly2 = (float) (percent2 * 100);


        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setExtraOffsets(20.f, 20.f, 20.f, 20.f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(getResources().getColor(R.color.colorBlackBg));
        pieChart.setTransparentCircleColor(getResources().getColor(R.color.colorBlackBg));
        pieChart.setTransparentCircleAlpha(60);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setRotationAngle(90);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        setData();

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // pieChart.spin(2000, 0, 360);

        Legend mLegend = pieChart.getLegend();
        mLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        mLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        mLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        mLegend.setDrawInside(true);//TODO
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(0f);
        mLegend.setTextColor(Color.WHITE);
        mLegend.setYOffset(0f);
    }

    private void setData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry((float) (exceptionSize * 100) / mainSize, "异常AP数量占比:"+p1));
//        entries.add(new PieEntry((float) (normalSize * 100) / mainSize,  "正常AP数量占比:"+p2));
        entries.add(new PieEntry((float) (exceptionSize * 100) / mainSize, "异常AP数量:"+exceptionSize));
        entries.add(new PieEntry((float) (normalSize * 100) / mainSize,  "正常AP数量:"+normalSize));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextColor(getResources().getColor(R.color.transparent));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorViolet));
        colors.add(getResources().getColor(R.color.colorYellowIcon));

        dataSet.setColors(colors);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueTextColor(getResources().getColor(R.color.colorWhite));

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
//        data.setValueTextColor(getResources().getColor(R.color.colorTransparent));
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }


    @OnClick({R.id.iv_back, R.id.tv_filtrate,R.id.rl_item1,R.id.rl_item2})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_filtrate:
                intent = new Intent(this, ApMonitoring.class);
                intent.putParcelableArrayListExtra(Constant.All_RECORD, (ArrayList<? extends Parcelable>) record);
                startActivity(intent);
                break;
            case R.id.rl_item1:
                intent = new Intent(this, photoDetialAct.class);
                intent.putParcelableArrayListExtra(Constant.RECORD, (ArrayList<? extends Parcelable>) exceptionList);
                intent.putExtra(Constant.NUM,1);
                startActivity(intent);
                break;
            case R.id.rl_item2:
                intent = new Intent(this, photoDetialAct.class);
                intent.putParcelableArrayListExtra(Constant.RECORD, (ArrayList<? extends Parcelable>) normalList);
                intent.putExtra(Constant.NUM,0);
                startActivity(intent);
                break;
        }
    }
}

package com.suntown.cloudmonitoring.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.ApPhote.photoDetialAct;
import com.suntown.cloudmonitoring.bean.APInfoBean;
import com.suntown.cloudmonitoring.bean.Item0;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by Administrator on 2016/11/21.
 */

public class ChartFragment extends Fragment {

    private View inflate;
    private PieChart pieChart;
    private List<APInfoBean.RECORDBean> recordList;
    private List<String> stringList;
    private Map<String, List<APInfoBean.RECORDBean>> listMap;
    private int mainSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.chart_layout, container, false);
        pieChart = (PieChart)inflate.findViewById(R.id.piechart);
        initView();
        return inflate;
    }

    private void initView() {
        recordList = ((photoDetialAct) getActivity()).recordList;
        listMap = new HashMap<>();
        List<APInfoBean.RECORDBean> arrayList;
        stringList = new ArrayList<>();
        for (APInfoBean.RECORDBean recordBean : recordList) {
            String aname = recordBean.ANAME;
            if (listMap.containsKey(aname)){
                arrayList = listMap.get(aname);
                arrayList.add(recordBean);
            }else{
                stringList.add(aname);
                arrayList = new ArrayList<>();
                arrayList.add(recordBean);
            }
            listMap.put(aname,arrayList);
        }
        mainSize = 0;
        for (String s : stringList) {
            List<APInfoBean.RECORDBean> recordBeanList = listMap.get(s);
            mainSize +=recordBeanList.size();
        }
        initChart();
    }

    private void initChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(10, 10, 10, 10);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setExtraOffsets(25.f, 25.f, 25.f, 25.f);
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
        mLegend.setWordWrapEnabled(true);
        mLegend.setDrawInside(true);
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);
        mLegend.setTextColor(Color.WHITE);
        mLegend.setYOffset(0f);
    }

    private void setData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
//        entries.add(new PieEntry((float) (exceptionSize * 100) + 100 / 5, "异常AP数量"+exceptionSize));
//        entries.add(new PieEntry((float) (normalSize * 100) + 100 / 5,  "正常AP数量"+normalSize));
        Log.i("chartFragment","stringList:"+stringList.size());
        Log.i("chartFragment","stringList:"+stringList.toString());
        for (String s : stringList) {
            List<APInfoBean.RECORDBean> recordBeanList = listMap.get(s);
            int size = recordBeanList.size();
            String aname = recordBeanList.get(0).ANAME;
            double percent = (double) size / (double) mainSize;
            NumberFormat nt = NumberFormat.getPercentInstance();
            //设置百分数精确度2即保留两位小数
            nt.setMinimumFractionDigits(1);
            String p = nt.format(percent);
            entries.add(new PieEntry((float) (size * 100) / mainSize, aname+"数量:"+size));
            Random random = new Random();
            int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
            colors.add(ranColor);
        }
//        entries.add(new PieEntry((float) (normalSize * 100) / mainSize,  "正常AP数量占比:"+p2));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextColor(getResources().getColor(R.color.colorWhite));

        dataSet.setColors(colors);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(getResources().getColor(R.color.colorWhite));
        pieChart.setData(data);

        pieChart.highlightValues(null);
        pieChart.invalidate();
    }
}

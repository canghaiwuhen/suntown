package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.BatteryDangerMonitoringActivity;
import com.suntown.cloudmonitoring.bean.BatteryDangerBean;
import com.suntown.cloudmonitoring.bean.Item1;
import com.suntown.cloudmonitoring.bean.TestBAttertDangerBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class BatteryExpandAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Item1> itemList;
    private HashMap<String, List<TestBAttertDangerBean.RECORDBean.Bean>> hashMap;

    public BatteryExpandAdapter(Context context,List<Item1> itemList , HashMap<String, List<TestBAttertDangerBean.RECORDBean.Bean>> hashMap) {
        this.context = context;
        this.itemList=itemList;
        this.hashMap = hashMap;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView =  View.inflate(context, R.layout.battery_danger_title, null);
        }
        RelativeLayout rlItem = (RelativeLayout) convertView.findViewById(R.id.rl_item);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvCount = (TextView) convertView.findViewById(R.id.tv_count);
        ImageView ivArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
        if (!isExpanded) {
            ivArrow.setImageResource(R.drawable.arrow_r);
        } else {
            ivArrow.setImageResource(R.drawable.arrow_d);
        }
        Item1 item1 = itemList.get(groupPosition);
        tvCount.setText(item1.ROWS+"个");
        String fallrange = item1.FALLRANGE;
        String[] split = fallrange.split("-");
        tvName.setText(split[0] + "↓" + split[1] + "%");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView =  View.inflate(context, R.layout.simple_items, null);
        }
        TextView tvId = ViewHolder.get(convertView, R.id.tv_id);

        TestBAttertDangerBean.RECORDBean.Bean bean = (TestBAttertDangerBean.RECORDBean.Bean) getChild(groupPosition, childPosition);
        tvId.setText(bean.TINYIP);
        return convertView;
    }

    @Override
    public int getGroupCount() {
        return itemList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String fallrange = itemList.get(groupPosition).FALLRANGE;
        List<TestBAttertDangerBean.RECORDBean.Bean> beanList = hashMap.get(fallrange);
        return beanList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return itemList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String fallrange = itemList.get(groupPosition).FALLRANGE;
        List<TestBAttertDangerBean.RECORDBean.Bean> beanList = hashMap.get(fallrange);
        return beanList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

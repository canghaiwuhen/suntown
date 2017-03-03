package com.suntown.smartscreen.spaceShelf;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.ShelfInfoBean;
import com.suntown.smartscreen.data.ShelfListBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class ExpandAdapter extends BaseExpandableListAdapter {

    Context context;
    List<Integer> list;
    HashMap<Integer, List<ShelfInfoBean.RECORDBean>> hashMap;


    public ExpandAdapter(Context context, List<Integer> list, HashMap<Integer, List<ShelfInfoBean.RECORDBean>> hashMap) {
        this.context=context;
        this.list=list;
        this.hashMap=hashMap;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        List<ShelfInfoBean.RECORDBean> recordBeen = hashMap.get(list.get(i));
//        recordBeen.size()
        //  返回值必须为1，否则会重复数据
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        List<ShelfInfoBean.RECORDBean> recordBeen = hashMap.get(list.get(i));
        return recordBeen.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        if (convertView==null){
            convertView =  View.inflate(context, R.layout.board_item1, null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        ImageView ivArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
        LinearLayout llItem = (LinearLayout) convertView.findViewById(R.id.ll_item);
        if (!isExpanded) {
            ivArrow.setImageResource(R.drawable.arrow_r);
        } else {
            ivArrow.setImageResource(R.drawable.arrow_d);
        }
        llItem.setBackgroundColor(Color.parseColor("#36383F"));
        tvName.setTextColor(Color.parseColor("#FAB53A"));
        tvName.setText("第"+list.get(i)+"层");
        return convertView;
    }

    @Override
    public View getChildView(int goupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView==null){
            convertView =  View.inflate(context, R.layout.grid_item, null);
        }
        GridView gridView = (GridView) convertView;
        MyGridViewAdapter gridViewAdapter = new MyGridViewAdapter(context, hashMap.get(list.get(goupPosition)));
        gridView.setHorizontalSpacing(10);
        gridView.setVerticalSpacing(10);
        gridView.setAdapter(gridViewAdapter);
        return convertView;
    }


}

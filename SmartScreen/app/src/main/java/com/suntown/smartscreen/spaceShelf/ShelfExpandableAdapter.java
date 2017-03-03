package com.suntown.smartscreen.spaceShelf;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.ShelfListBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class ShelfExpandableAdapter extends BaseExpandableListAdapter {
    Context context;
    List<ShelfListBean.RECORDBeanX> record;
    public ShelfExpandableAdapter(Context context, List<ShelfListBean.RECORDBeanX> record) {
            this.context=context;
            this.record=record;
    }

    @Override
    public int getGroupCount() {
        return record.size();
    }

    @Override
    public int getChildrenCount(int i) {
        ShelfListBean.RECORDBeanX recordBeanX = record.get(i);
        return recordBeanX.RECORD.size();
    }

    @Override
    public Object getGroup(int i) {
        return record.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        ShelfListBean.RECORDBeanX recordBeanX = record.get(i);
        return recordBeanX.RECORD.get(i1);
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
            convertView =  View.inflate(context, R.layout.board_item, null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvCount = (TextView) convertView.findViewById(R.id.tv_count);
        ImageView ivArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
        LinearLayout llItem = (LinearLayout) convertView.findViewById(R.id.ll_item);
        if (!isExpanded) {
            ivArrow.setImageResource(R.drawable.arrow_r);
        } else {
            ivArrow.setImageResource(R.drawable.arrow_d);
        }
        llItem.setBackgroundColor(Color.parseColor("#36383F"));
        ShelfListBean.RECORDBeanX recordBeanX = record.get(i);
        tvName.setText(recordBeanX.RNAME);
        tvCount.setText(recordBeanX.ROWS+"");
        return convertView;
    }

    @Override
    public View getChildView(int goupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView==null){
            convertView =  View.inflate(context, R.layout.text_items, null);
        }
        TextView tvText = (TextView) convertView.findViewById(R.id.tv_text);
        ShelfListBean.RECORDBeanX recordBeanX = record.get(goupPosition);
        List<ShelfListBean.RECORDBeanX.RECORDBean> recordBeanList = recordBeanX.RECORD;
        ShelfListBean.RECORDBeanX.RECORDBean recordBean = recordBeanList.get(childPosition);
        tvText.setText(recordBean.SFID);
        tvText.setTextColor(Color.parseColor("#FFFFFF"));
        return convertView;
    }


}

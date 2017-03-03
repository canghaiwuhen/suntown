package com.suntown.smartscreen.shopCenter.detial;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.ShopBoardBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/2/16.
 */

public class ExpandableItemAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> list;
    HashMap<String, List<ShopBoardBean.RECORDBean>> listHashMap;
    public ExpandableItemAdapter(Context context, List<String> list, HashMap<String, List<ShopBoardBean.RECORDBean>> listHashMap) {
        this.context=context;
        this.list=list;
        this.listHashMap=listHashMap;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        List<ShopBoardBean.RECORDBean> recordBeen = listHashMap.get(list.get(i));
        return recordBeen.size();
    }

    @Override
    public Object getGroup(int i) {
        return list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        List<ShopBoardBean.RECORDBean> recordBeen = listHashMap.get(list.get(i));
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
    public View getGroupView(int goupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        if (convertView==null){
            convertView =  View.inflate(context, R.layout.board_item, null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvCount = (TextView) convertView.findViewById(R.id.tv_count);
        ImageView ivArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
        if (!isExpanded) {
            ivArrow.setImageResource(R.drawable.arrow_r);
        } else {
            ivArrow.setImageResource(R.drawable.arrow_d);
        }
        String s = list.get(goupPosition);
        int size = listHashMap.get(s).size();
        tvName.setText(s);
        tvCount.setText(size+"");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup viewGroup) {
        if (convertView==null){
            convertView =  View.inflate(context, R.layout.dm_item1, null);
        }
        TextView tvTypeName = (TextView) convertView.findViewById(R.id.tv_type_name);
        TextView tvSize = (TextView) convertView.findViewById(R.id.tv_size);
        TextView tvStateTag = (TextView) convertView.findViewById(R.id.tv_state_tag);
        TextView tvState = (TextView) convertView.findViewById(R.id.tv_state);
        TextView tvStting = (TextView) convertView.findViewById(R.id.tv_setting);
        LinearLayout llItem = (LinearLayout) convertView.findViewById(R.id.ll_item);
        List<ShopBoardBean.RECORDBean> recordBeen = listHashMap.get(list.get(groupPosition));
        ShopBoardBean.RECORDBean recordBean = recordBeen.get(childPosition);
        tvTypeName.setText(recordBean.DMNAME);
        tvSize.setText(recordBean.SPECNAME);
                if (recordBean.ISACTIVE.equals("1")) {
                    tvStateTag.setTextColor(Color.parseColor("#79B645"));
                    tvState.setTextColor(Color.parseColor("#79B645"));
                    tvState.setText("正常");
                    tvStting.setText("停用");
                } else if (recordBean.ISACTIVE.equals("0")) {
                    tvStateTag.setTextColor(Color.parseColor("#D94368"));
                    tvState.setTextColor(Color.parseColor("#D94368"));
                    tvState.setText("启用");
                    tvStting.setText("异常");
                }
                tvStting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onAdapterCallBack.onSettingClick(groupPosition,childPosition);
                    }
                });
                    llItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo 接口回掉，跳转节面
                        onAdapterCallBack.onItemClick(groupPosition,childPosition);
                    }
                });
        return convertView;
    }

    public interface OnAdapterCallBack {
        void onSettingClick(int parentPosition,int childPosition);//告诉外界删除条目的回调
        void onItemClick(int parentPosition,int childPosition);//告诉外界删除条目的回调
    }

    private OnAdapterCallBack onAdapterCallBack;

    public void setOnAdapterCallBack(OnAdapterCallBack onAdapterCallBack) {
        this.onAdapterCallBack = onAdapterCallBack;
    }

}

package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.APInfoBean;
import com.suntown.cloudmonitoring.bean.Item0;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ExceptionExpandAdapter extends BaseExpandableListAdapter {
    private Map<String, List<APInfoBean.RECORDBean>> listMap;
    private Context mContext;
    private ArrayList<Item0> items;
    public ExceptionExpandAdapter(Context context, ArrayList<Item0> items, Map<String, List<APInfoBean.RECORDBean>> listMap) {
        this.mContext = context;
        this.listMap = listMap;
        this.items = items;
    }

    @Override
    public int getGroupCount() {
        return items.size()==0?0:items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String sid = items.get(groupPosition).sid;
        List<APInfoBean.RECORDBean> shopInfos = listMap.get(sid);
        return shopInfos.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String sid = items.get(groupPosition).sid;
        List<APInfoBean.RECORDBean> shopInfos = listMap.get(sid);
        return shopInfos.get(childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView =  View.inflate(mContext, R.layout.ap_title_item, null);
        }
        RelativeLayout rlItem = ViewHolder.get(convertView, R.id.rl_item);
        ImageView ivArrow = ViewHolder.get(convertView, R.id.iv_arrow);
        TextView tvStoreNum = ViewHolder.get(convertView, R.id.tv_store_num);
        TextView tvStoreName = ViewHolder.get(convertView, R.id.tv_store_name);
        if (!isExpanded) {
            ivArrow.setImageResource(R.drawable.arrow_r);
        } else {
            ivArrow.setImageResource(R.drawable.arrow_d);
        }
        Item0 item0 = items.get(groupPosition);
        tvStoreName.setText(item0.sname);
        tvStoreNum.setText(item0.sid);
        rlItem.setOnClickListener(v -> {
            onWaitFlagClickListener.OnFlagClickListener(groupPosition);
        });
        return convertView;
    }

    private OnWaitFlagClickListener onWaitFlagClickListener;
    public void setOnWaitFlagClickListener(OnWaitFlagClickListener onWaitFlagClickListener){
        this.onWaitFlagClickListener = onWaitFlagClickListener;
    }
    public interface OnWaitFlagClickListener {
        /**
         * 三角点击事件的回调方法，点击三角时展开或关闭子ListView
         * @param position
         */
        void OnFlagClickListener(int position);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = View.inflate(mContext, R.layout.ap_items, null);
        }
        TextView tvApid = ViewHolder.get(convertView, R.id.tv_apid);
        TextView tvApaddr = ViewHolder.get(convertView, R.id.tv_apaddr);
        TextView tvNum = ViewHolder.get(convertView, R.id.tv_num);
        TextView tvLastTime = ViewHolder.get(convertView, R.id.tv_lastTime);
        ImageView ivAlarm = ViewHolder.get(convertView, R.id.iv_alarm);

        APInfoBean.RECORDBean shopInfo = (APInfoBean.RECORDBean) getChild(groupPosition, childPosition);
        if (shopInfo.STATUS==0) {
            ivAlarm.setVisibility(View.VISIBLE);
        }else{
            ivAlarm.setVisibility(View.GONE);
        }
        tvNum.setText(shopInfo.EDCOUNT+"");
        tvApid.setText(shopInfo.APIP);
        tvApaddr.setText(shopInfo.APADDR);
        tvLastTime.setText(shopInfo.LASTDATE);
        // 此处可以设置子view中控件的点击事件

        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


}

package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.Item0;
import com.suntown.cloudmonitoring.bean.RegisterMor;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/27.
 */
public class RegisterExpandAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Item0> items;
    private Map<String,List<RegisterMor.RECORDBean>> listMap;
    public RegisterExpandAdapter(Context context, List<Item0> items, Map<String, List<RegisterMor.RECORDBean>> listMap) {
        this.context=context;
        this.items=items;
        this.listMap=listMap;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView =  View.inflate(context, R.layout.rg_title_item, null);
        }
        TextView nowShopTagNum = ViewHolder.get(convertView, R.id.tv_now_shop_tag_num);
        ImageView ivArrow = ViewHolder.get(convertView, R.id.iv_arrow);
        TextView tvStoreNum = ViewHolder.get(convertView, R.id.tv_store_num);
        TextView tvStoreName = ViewHolder.get(convertView, R.id.tv_store_name);
        RelativeLayout rlItem = ViewHolder.get(convertView, R.id.rl_item);
        if (!isExpanded) {
            ivArrow.setImageResource(R.drawable.arrow_r);
            nowShopTagNum.setVisibility(View.GONE);
        } else {
            ivArrow.setImageResource(R.drawable.arrow_d);
            nowShopTagNum.setVisibility(View.VISIBLE);
        }
        Item0 item0 = items.get(groupPosition);
        tvStoreName.setText(item0.sname);
        String sid = item0.sid;
        List<RegisterMor.RECORDBean> recordBeanList = listMap.get(sid);
        int size = recordBeanList.size();
        nowShopTagNum.setText("当前门店编号:"+sid);
        tvStoreNum.setText(size+"");
        rlItem.setOnClickListener(v -> {
            onWaitFlagClickListener.OnFlagClickListener(groupPosition);
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView =  View.inflate(context, R.layout.rg_items, null);
        }
         TextView tvIp= ViewHolder.get(convertView, R.id.tv_ip);
         TextView tvNum= ViewHolder.get(convertView, R.id.tv_num);
         TextView tvName= ViewHolder.get(convertView, R.id.tv_name);
        RegisterMor.RECORDBean  recordBean= (RegisterMor.RECORDBean) getChild(groupPosition, childPosition);

        tvIp.setText(recordBean.TINYIP);
        tvNum.setText(recordBean.AID);
        tvName.setText(recordBean.GNAME);

        return convertView;
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String sid = items.get(groupPosition).sid;
        List<RegisterMor.RECORDBean> recordBeen = listMap.get(sid);
        return recordBeen.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String sid = items.get(groupPosition).sid;
        List<RegisterMor.RECORDBean> recordBeen = listMap.get(sid);
        return recordBeen.get(childPosition);
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

    private ExceptionExpandAdapter.OnWaitFlagClickListener onWaitFlagClickListener;
    public void setOnWaitFlagClickListener(ExceptionExpandAdapter.OnWaitFlagClickListener onWaitFlagClickListener){
        this.onWaitFlagClickListener = onWaitFlagClickListener;
    }
    public interface OnWaitFlagClickListener {
        /**
         * 三角点击事件的回调方法，点击三角时展开或关闭子ListView
         * @param position
         */
        void OnFlagClickListener(int position);
    }
}

package com.suntown.smartscreen.maintain.detial;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.DispMShopBean;
import com.suntown.smartscreen.maintain.MainTainAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */

public class MainTainDetialAdapter extends BaseAdapter {
    private List<DispMShopBean.RECORDBean> record;
    private Context context;

    public MainTainDetialAdapter(Context context, List<DispMShopBean.RECORDBean> record) {
        this.record = record;
        this.context = context;
    }

    @Override
    public int getCount() {
        return record.size() == 0 ? 0 : record.size();
    }

    @Override
    public Object getItem(int i) {
        return record.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        DispMShopBean.RECORDBean recordBean = record.get(i);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.dm_item,null);
            viewHolder.tvTypeName = (TextView) convertView.findViewById(R.id.tv_type_name);
            viewHolder.sid = (TextView) convertView.findViewById(R.id.tv_id);
            viewHolder.tvStateTag = (TextView) convertView.findViewById(R.id.tv_state_tag);
            viewHolder.tvState = (TextView) convertView.findViewById(R.id.tv_state);
            viewHolder.tvStting = (TextView) convertView.findViewById(R.id.tv_setting);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTypeName.setText(recordBean.SNAME);
        viewHolder.sid.setText(recordBean.SID);
        if (recordBean.ISACTIVE.equals("1")) {
            viewHolder.tvStateTag.setTextColor(Color.parseColor("#79B645"));
            viewHolder.tvState.setTextColor(Color.parseColor("#79B645"));
            viewHolder.tvState.setText("已启用");
            viewHolder.tvStting.setText("停用");
        } else if (recordBean.ISACTIVE.equals("0")) {
            viewHolder.tvStateTag.setTextColor(Color.parseColor("#D94368"));
            viewHolder.tvState.setTextColor(Color.parseColor("#D94368"));
            viewHolder.tvStting.setText("启用");
            viewHolder.tvState.setText("未启用");
        }
        viewHolder.tvStting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdapterCallBack.onItemClick(i);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tvTypeName;
        TextView sid;
        TextView tvStateTag;
        TextView tvState;
        TextView tvStting;
    }

    public interface OnAdapterCallBack {
        void onItemClick(int position);//告诉外界删除条目的回调
    }

    private OnAdapterCallBack onAdapterCallBack;

    public void setOnAdapterCallBack(OnAdapterCallBack onAdapterCallBack) {
        this.onAdapterCallBack = onAdapterCallBack;
    }
//       @Override
//    protected void convert(BaseViewHolder holder, DispMShopBean.RECORDBean recordBean) {
//        if (recordBean.ISACTIVE.equals("0")) {
//            TextView tvTag = holder.getView(R.id.tv_state_tag);
//            TextView tvState = holder.getView(R.id.tv_state);
//            tvTag.setTextColor(Color.parseColor("#79B645"));
//            tvState.setTextColor(Color.parseColor("#79B645"));
//            tvState.setText("正常");
//        }else if (recordBean.ISACTIVE.equals("1")){
//            TextView tvTag = holder.getView(R.id.tv_state_tag);
//            TextView tvState = holder.getView(R.id.tv_state);
//            tvTag.setTextColor(Color.parseColor("#D94368"));
//            tvState.setTextColor(Color.parseColor("#D94368"));
//            tvState.setText("异常");
//        }
//        holder.setText(R.id.tv_type_name,recordBean.SNAME);
//        holder.setText(R.id.tv_id,recordBean.SID);
//        int position = holder.getPosition();
//        holder.getView(R.id.tv_setting).setOnClickListener(view -> onAdapterCallBack.onItemClick(position));
//    }

}

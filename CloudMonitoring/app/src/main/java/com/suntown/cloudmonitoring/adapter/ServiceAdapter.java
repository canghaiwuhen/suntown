package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.ChildServerBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ServiceAdapter extends BaseAdapter {
    private Context context;
    private List<ChildServerBean.RECORDBean> record;
    public ServiceAdapter(Context context, List<ChildServerBean.RECORDBean> record) {
        this.context=context;
        this.record=record;
    }

    @Override
    public int getCount() {
        return record.size()==0?0:record.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context, R.layout.item, null);
        }
        TextView tvName = ViewHolder.get(view, R.id.tv_text);
        ChildServerBean.RECORDBean recordBean = record.get(i);
        tvName.setText(recordBean.getModname());
        return view;
    }



}

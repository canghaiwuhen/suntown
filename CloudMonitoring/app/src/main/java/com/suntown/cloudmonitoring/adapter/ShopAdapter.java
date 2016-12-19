package com.suntown.cloudmonitoring.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.ChangePriceAct;
import com.suntown.cloudmonitoring.bean.ChildServerBean;
import com.suntown.cloudmonitoring.bean.SortModel;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ShopAdapter extends BaseAdapter{
    Context context;
    List<String> host;

    public ShopAdapter(Context context, List<String> host) {
        this.context=context;
        this.host=host;
    }

    @Override
    public int getCount() {
        return host.size()==0?0:host.size();
    }

    @Override
    public Object getItem(int i) {
        return host.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context, R.layout.item3, null);
        }
        TextView tvName = ViewHolder.get(view, R.id.tv_text);
        tvName.setText(host.get(i));
        return view;
    }
}

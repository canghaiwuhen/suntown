package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.HistortyActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */
public class HistoryAdapter extends BaseAdapter{
    Context context;
    List<String> daythlist;

    public HistoryAdapter(Context context, List<String> daythlist) {
        this.context=context;
        this.daythlist=daythlist;
    }

    @Override
    public int getCount() {
        return daythlist.size()==0?0:daythlist.size();
    }

    @Override
    public Object getItem(int i) {
        return daythlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String s = daythlist.get(i);
        ViewHolder viewHolder;
        if (view==null){
            viewHolder = new ViewHolder();
            view=  View.inflate(context, R.layout.item2,null);
            viewHolder.textView = (TextView) view.findViewById(R.id.tv_text);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(s);
        return view;
    }
    class ViewHolder {
         TextView textView;
    }
}

package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.zhikaizhang.indexview.Util;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.Item;

import java.util.List;



public class MyAdapter extends BaseAdapter {

    private Context context;
    private List<Item> list;

    public MyAdapter(Context context, List<Item> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Item item = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.text_items, null);
            viewHolder.indexTextView = (TextView) convertView.findViewById(R.id.catalog);
            viewHolder.userNameTextView = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.userNameTextView.setText(item.userName);
        char index = Util.getIndex(item.userName);
        if(position == 0 || Util.getIndex(list.get(position - 1).userName) != index){
            viewHolder.indexTextView.setVisibility(View.VISIBLE);
            viewHolder.indexTextView.setText(String.valueOf(index));
        }else{
            viewHolder.indexTextView.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView indexTextView;
        TextView userNameTextView;
    }
}

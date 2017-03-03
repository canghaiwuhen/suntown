package com.suntown.smartscreen.price.chooseShop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suntown.smartscreen.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/8.
 */

public class MyAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<String> nameList;
    public MyAdapter(Context context, ArrayList<String> nameList) {
        this.context=context;
        this.nameList=nameList;
    }

    @Override
    public int getCount() {
        return nameList.size()==0?0:nameList.size();
    }

    @Override
    public Object getItem(int i) {
        return nameList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        convertView = LayoutInflater.from(context).inflate(R.layout.text_items, null);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_text);
        tvName.setText(nameList.get(i));
        return convertView;
    }
}

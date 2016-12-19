package com.suntown.scannerproject.input.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.suntown.scannerproject.R;

import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */
public class OddNumAdapter extends BaseAdapter{
    Context context;
    List<String> stringList;
    public OddNumAdapter(Context context, List<String> stringList) {
        this.context=context;
        this.stringList=stringList;
    }

    @Override
    public int getCount() {
        return stringList.size()==0?0:stringList.size();
    }

    @Override
    public Object getItem(int i) {
        return stringList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        String num = stringList.get(i);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.intput_item_title,null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ((TextView) convertView.findViewById(R.id.tv_num)).setText(num);
        return convertView;


    }
    class ViewHolder{
        TextView tvNum;
    }
}

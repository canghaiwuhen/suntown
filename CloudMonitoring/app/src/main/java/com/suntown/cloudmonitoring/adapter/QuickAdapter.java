package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.MessageBean;

import java.util.List;


/**
 * Created by Administrator on 2016/11/8.
 */
public class QuickAdapter extends BaseAdapter {
    Context context;
    List<MessageBean> beanList;
    public QuickAdapter(Context context, List<MessageBean> beanList) {
        this.context=context;
        this.beanList=beanList;
    }


    @Override
    public int getCount() {
        return beanList.size()==0?0:beanList.size();
    }

    @Override
    public Object getItem(int i) {
        return beanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        MessageBean messageBean = beanList.get(i);
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context,R.layout.message_item,null);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.ivMassage = (ImageView) convertView.findViewById(R.id.iv_message);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTime.setText( messageBean.sendTimeStr);
        viewHolder.tvContent.setText(messageBean.content);
        viewHolder.ivMassage.setVisibility(messageBean.lookstatus==0?View.VISIBLE:View.INVISIBLE);
        return convertView;
    }

    class ViewHolder{
        TextView tvTime;
        TextView tvContent;
        ImageView ivMassage;
    }

}

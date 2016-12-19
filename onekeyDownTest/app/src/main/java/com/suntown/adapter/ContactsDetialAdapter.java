package com.suntown.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.suntown.R;
import com.suntown.activity.ContactsDetialActivity;
import com.suntown.bean.DeviceInfoBean;
import com.suntown.bean.UserInfoBean;
import com.suntown.bean.UserTagBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ContactsDetialAdapter extends BaseAdapter {
    Context context;
    List<UserTagBean.RECORDBean> userTagBeanRECORD;
    public ContactsDetialAdapter(Context context, List<UserTagBean.RECORDBean> userTagBeanRECORD) {
        this.context=context;
        this.userTagBeanRECORD=userTagBeanRECORD;
    }

    @Override
    public int getCount() {
        return userTagBeanRECORD==null?0:userTagBeanRECORD.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = View.inflate(context, R.layout.constact_items,null);
        }
        TextView tvTagNum = ViewHolder.get(convertView, R.id.tv_tag_num);
        TextView tvGoodsNum = ViewHolder.get(convertView, R.id.tv_goods_name);
        tvTagNum.setText(userTagBeanRECORD.get(position).getTINYIP());
        tvGoodsNum.setText(userTagBeanRECORD.get(position).getGNAME());
        return convertView;
    }
}

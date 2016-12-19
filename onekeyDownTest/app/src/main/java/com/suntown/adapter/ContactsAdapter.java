package com.suntown.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suntown.R;
import com.suntown.bean.PhoneInfo;
import com.suntown.bean.UserInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class ContactsAdapter extends BaseAdapter{
    private List<UserInfoBean.RECORDBean> lists;
    private Context context;

    public ContactsAdapter( List<UserInfoBean.RECORDBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    //返回集合的数量
    @Override
    public int getCount() {
        return lists==null?0:lists.size();
    }

    //返回当前数据
    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    //获取当前ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, null);
        }
        TextView tvName = ViewHolder.get(convertView, R.id.tv_nick);
        TextView tvNumber = ViewHolder.get(convertView, R.id.tv_number);

        tvName.setText(lists.get(position).getNICKNAME());
        tvNumber.setText(lists.get(position).getTEL());
        return convertView;
    }

}

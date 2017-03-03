package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.UserListBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class NameAdapter extends BaseAdapter{
    Context context;
    List<UserListBean.UsersBean> users;

    public NameAdapter(Context context,List<UserListBean.UsersBean> users) {
        this.context=context;
        this.users=users;
    }

    @Override
    public int getCount() {
        return users.size()==0?0:users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
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
        tvName.setText(users.get(i).userid);
        tvName.setTextColor(Color.parseColor("#000000"));
        return view;
    }
}

package com.suntown.smartscreen.shopCenter.fragment;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.ShopBoardBean;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/2/15.
 */

public class BoardListAdapter extends BaseAdapter {
    Context context ;
    HashMap<String,List<ShopBoardBean.RECORDBean>> hashMap;
    private Set<String> set;
    List<String> names;
    public BoardListAdapter(Context context, HashMap<String, List<ShopBoardBean.RECORDBean>> hashMap,List<String> names) {
        this.context = context;
        this.hashMap=hashMap;
        this.names=names;
    }

    @Override
    public int getCount() {
        return names.size()==0?0:names.size();
    }

    @Override
    public Object getItem(int i) {
        List<ShopBoardBean.RECORDBean> recordBeen = hashMap.get(names.get(i));
        return recordBeen.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        convertView = View.inflate(context,R.layout.board_item, null);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        ((TextView) convertView.findViewById(R.id.tv_count)).setText(hashMap.get(names.get(i)).size()+"");
        tvName.setText(names.get(i));
        return convertView;
    }

}

package com.suntown.scannerproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.scannerproject.R;
import com.suntown.scannerproject.bean.ShopListBean;
import com.suntown.scannerproject.bean.ShopResult;
import com.suntown.scannerproject.utils.Constant;

import java.util.List;

import cn.zhikaizhang.indexview.Util;

/**
 * Created by Administrator on 2016/11/29.
 */
public class ShopListDapter extends BaseAdapter {
    Context context;
    List<ShopListBean> shopList;

    public ShopListDapter(Context context,List<ShopListBean> shopList) {
        this.context=context;
        this.shopList=shopList;
    }


    @Override
    public int getCount() {
        return shopList.size()==0?0:shopList.size();
    }

    @Override
    public Object getItem(int i) {
        return shopList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ShopListBean shopListBean = shopList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.text_items, null);
            viewHolder.indexTextView = (TextView) convertView.findViewById(R.id.catalog);
            viewHolder.userNameTextView = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.userNameTextView.setText(shopListBean.SName);
        char index = Util.getIndex(shopListBean.SName);
        if(position == 0 || Util.getIndex(shopList.get(position - 1).SName) != index){
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

package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.Person;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */
public class GoodsListAdapter extends BaseAdapter{
    private Context context;
    private List<Person> goodsList;
    public GoodsListAdapter(Context context, List<Person> goodsList) {
        this.context =context;
        this.goodsList =goodsList;
    }

    @Override
    public int getCount() {
        return goodsList.size()==0?0:goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        Person person = goodsList.get(position);
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.goods_list_items,null);
            viewHolder.TvShopName = (TextView)convertView.findViewById(R.id.tv_shop_name);
            viewHolder.TvIp= (TextView) convertView.findViewById(R.id.tv_ip);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.TvShopName.setText(person.barcode);
        viewHolder.TvIp.setText(person.gname);
        return convertView;
    }
    class ViewHolder {
        TextView TvShopName;
        TextView TvIp;
    }
}

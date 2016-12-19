package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.ShopXmlBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/7.
 */
public class TinyipAdapter extends BaseAdapter{
    Context context;
    List<ShopXmlBean> shopList;
    public TinyipAdapter(Context context, List<ShopXmlBean> shopList) {
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        ShopXmlBean shopXmlBean = shopList.get(i);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.tyinp_tiem, null);
            viewHolder.tvTag = (TextView) convertView.findViewById(R.id.tv_tag);
            viewHolder.tvIp = (TextView) convertView.findViewById(R.id.tv_ip);
            viewHolder.tvShelf = (TextView) convertView.findViewById(R.id.tv_shelf);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tvGoodsState = (TextView) convertView.findViewById(R.id.tv_goods_state);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (i%3==1){
            viewHolder.tvTag.setTextColor(Color.parseColor("#7A538F"));
        }else if (i%3==2){
            viewHolder.tvTag.setTextColor(Color.parseColor("#43B8DB"));
        }else if(i%3==0){
            viewHolder.tvTag.setTextColor(Color.parseColor("#D94368"));
        }
        String tinyIp = shopXmlBean.TinyIp;
        viewHolder.tvIp.setText(tinyIp);
//        if (tinyIp.length()>5) {
//            tinyIp = tinyIp.substring(0,5)+"\n"+ tinyIp.substring(5,tinyIp.length());
//        }
        viewHolder.tvTag.setText(tinyIp);
        viewHolder.tvShelf.setText(shopXmlBean.SFID);
        viewHolder.tvTime.setText(shopXmlBean.LastDate);

        String gStatus = shopXmlBean.GStatus;
        if ("0".equals(gStatus)){
            viewHolder.tvGoodsState.setText("正常");
        }else if ("1".equals(gStatus)){
            viewHolder.tvGoodsState.setText("缺货");
        }else{
            viewHolder.tvGoodsState.setText("其他状态");
        }
        return convertView;
    }
    class ViewHolder{
        TextView tvTag;
        TextView tvIp;
        TextView tvShelf;
        TextView tvTime;
        TextView tvGoodsState;
    }

}

package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.APInfoBean;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/21.
 */

public class CartItemAdapter extends BaseAdapter{
    Context context;
    List<String> stringList;
    Map<String, List<APInfoBean.RECORDBean>> listMap;
    int intExtra;
    public CartItemAdapter(Context context, List<String> stringList, Map<String, List<APInfoBean.RECORDBean>> listMap,int intExtra) {
        this.context=context;
        this.stringList=stringList;
        this.listMap=listMap;
        this.intExtra=intExtra;
    }

    @Override
    public int getCount() {
        return 0==stringList.size()?0:stringList.size();
    }

    @Override
    public Object getItem(int i) {
        String s = stringList.get(i);
        List<APInfoBean.RECORDBean> recordBeanList = listMap.get(s);
        return recordBeanList;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.cart_item, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
            viewHolder.position = position;

        String s = stringList.get(position);
        List<APInfoBean.RECORDBean> recordBeanList = listMap.get(s);
        APInfoBean.RECORDBean recordBean = recordBeanList.get(0);
        int size = 0;
        for (String s1 : stringList) {
            List<APInfoBean.RECORDBean> recordBeanList1 = listMap.get(s1);
            size+=recordBeanList1.size();
        }
        int recordSize = recordBeanList.size();
        double percent2 = (double) recordSize / (double) size;
        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(1);
        String p1 = nt.format(percent2);
        viewHolder.tvTag= (TextView) convertView.findViewById(R.id.tv_tag);
        viewHolder.tvUnusualNum= (TextView) convertView.findViewById(R.id.tv_unusual_num);
        viewHolder.percent= (TextView) convertView.findViewById(R.id.tv_percent);
        viewHolder.pb= (ProgressBar) convertView.findViewById(R.id.pb);
        String s1 = intExtra == 0 ? "正常AP" : "异常AP";
        viewHolder.tvTag.setText(recordBean.ANAME+s1+":");
        viewHolder.tvUnusualNum.setText(recordSize+"");
        viewHolder.percent.setText(p1);
        viewHolder.pb.setMax(size);
        viewHolder.pb.setProgress(recordSize);
        return convertView;
    }
    class ViewHolder{
        TextView tvTag;
        TextView tvUnusualNum;
        TextView percent;
        ProgressBar pb;
        int position;
    }
}

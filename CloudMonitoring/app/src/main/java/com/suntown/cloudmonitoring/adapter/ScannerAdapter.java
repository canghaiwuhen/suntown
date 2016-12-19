package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.Item2;
import com.suntown.cloudmonitoring.bean.ShopXmlBean;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
public class ScannerAdapter extends BaseAdapter{
    Context mcontext;
    List<Item2> lvBeanList;

    public ScannerAdapter(Context context, List<Item2> lvBeanList) {
        this.mcontext =context;
        this.lvBeanList =lvBeanList;
    }
    public interface OnItemClickCallBack {
        void onItemClick(int position);//告诉外界条目单击
    }
    private OnItemClickCallBack onitemClickCallBack;

    public void SetOnItemClickCallBack(OnItemClickCallBack onitemClickCallBack){
        this.onitemClickCallBack = onitemClickCallBack;
    }
    @Override
    public int getCount() {
        return lvBeanList.size()==0?0:lvBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return lvBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Item2 item2 = lvBeanList.get(position);
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.no_goods_items,null);
            viewHolder.IvDelete = (ImageView)convertView.findViewById(R.id.iv_delete);
            viewHolder.TvGoodsNum = (TextView)convertView.findViewById(R.id.tv_goods_num);
            viewHolder.TvGoodsName = (TextView)convertView.findViewById(R.id.tv_goods_name);
            viewHolder.TvIp= (TextView) convertView.findViewById(R.id.tv_ip);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.IvDelete.setOnClickListener(view -> {
            if (onitemClickCallBack!=null){
                onitemClickCallBack.onItemClick(position);
            }
        });
        viewHolder.TvGoodsNum.setText(item2.Barcode);
        viewHolder.TvGoodsName.setText(item2.GName);
        viewHolder.TvIp.setText(item2.tinyip);
        return convertView;
    }

    class ViewHolder {
        TextView TvGoodsNum;
        TextView TvGoodsName;
        TextView TvIp;
        ImageView IvDelete;
    }
}

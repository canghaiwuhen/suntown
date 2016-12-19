package com.suntown.scannerproject.scanner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntown.scannerproject.R;
import com.suntown.scannerproject.bean.Item2;
import com.suntown.scannerproject.weight.SwipeLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
//        void onItemClick(int position);//告诉外界条目单击
        void deleteItemClick(int position);//告诉外界删除条目的回调
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
//            viewHolder.IvDelete = (ImageView)convertView.findViewById(R.id.iv_delete);
            viewHolder.TvGoodsNum = (TextView)convertView.findViewById(R.id.tv_goods_num);
            viewHolder.TvGoodsName = (TextView)convertView.findViewById(R.id.tv_goods_name);
            viewHolder.TvIp= (TextView) convertView.findViewById(R.id.tv_ip);
            viewHolder.IvDelete = (LinearLayout)convertView.findViewById(R.id.iv_swipe_delete);
            viewHolder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipeLayout);
            viewHolder.IvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onitemClickCallBack!=null){
                        onitemClickCallBack.deleteItemClick(position);
                    }
                }
            });
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.IvDelete.setOnClickListener(view -> {
//            if (onitemClickCallBack!=null){
//                onitemClickCallBack.onItemClick(position);
//            }
//        });
        viewHolder.swipeLayout.close(false);
        viewHolder.swipeLayout.setOnSwipeLayoutChangedListener(onSwipeLayoutChangedListener);
        viewHolder.TvGoodsNum.setText(item2.Barcode);
        viewHolder.TvGoodsName.setText(item2.GName);
        viewHolder.TvIp.setText(item2.tinyip);
        return convertView;
    }

    class ViewHolder {
        TextView TvGoodsNum;
        TextView TvGoodsName;
        TextView TvIp;
        LinearLayout IvDelete;
        SwipeLayout swipeLayout;
    }
    private Set<SwipeLayout> swipeLayoutSet=new HashSet<>();
    private SwipeLayout.OnSwipeLayoutChangedListener onSwipeLayoutChangedListener=new SwipeLayout.OnSwipeLayoutChangedListener(){
        @Override
        public void onOpen(SwipeLayout swipeLayout) {
            //把之前保存的swipeLayout关闭
            for (SwipeLayout sl : swipeLayoutSet) {
                sl.close(true);
            }
            swipeLayoutSet.add(swipeLayout);
        }
        @Override
        public void onClose(SwipeLayout swipeLayout) {
            swipeLayoutSet.remove(swipeLayout);
        }
    };
}

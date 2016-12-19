package com.suntown.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.suntown.R;
import com.suntown.bean.WaitConfirmBean;
import com.suntown.utils.Constant;
import com.suntown.widget.SwipeLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/15.
 */
public class WaitPayAdapter extends BaseAdapter {
    private Context context;
    private List<WaitConfirmBean.RECORDBean> record;
    private Set<SwipeLayout> swipeLayoutSet=new HashSet<>();

    public WaitPayAdapter(Context context, List<WaitConfirmBean.RECORDBean> record) {
        this.context=context;
        this.record=record;
    }
    public interface OnWaitAdapterCallBack{
        void deleteItemClick(int position);//告诉外界删除条目的回调
        void buttonItemCLick(int position);
        void onItemCLick(int position);
    }
    private OnWaitAdapterCallBack onWaitAdapterCallBack;
    public void setOnWaitAdapterCallBack(OnWaitAdapterCallBack onWaitAdapterCallBack) {
        this.onWaitAdapterCallBack = onWaitAdapterCallBack;
    }

    @Override
    public int getCount() {
        return record==null?0:record.size();
    }

    @Override
    public Object getItem(int position) {
        return record.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=View.inflate(context, R.layout.order_items,null);
        }
        WaitConfirmBean.RECORDBean recordBean = record.get(position);
        List<WaitConfirmBean.RECORDBean.ORDERINFOBean> orderinfo = recordBean.getORDERINFO();
        WaitConfirmBean.RECORDBean.ORDERINFOBean orderinfoBean = orderinfo.get(0);

        TextView tvName = ViewHolder.get(convertView, R.id.tv_name);
        TextView tvPrice = ViewHolder.get(convertView, R.id.tv_price);
        TextView tvOrderNumber = ViewHolder.get(convertView, R.id.tv_order_number);
        TextView tvOrderMAC = ViewHolder.get(convertView, R.id.tv_order_mac_number);
        TextView cartNumber = ViewHolder.get(convertView, R.id.cart_number_tv);
        TextView tvCurrentTime = ViewHolder.get(convertView, R.id.tv_current_time);
        TextView tvSwipeDelete = ViewHolder.get(convertView, R.id.tv_swipe_delete);
        SwipeLayout swipeLayout = ViewHolder.get(convertView, R.id.swipeLayout);
        Button btnButton = ViewHolder.get(convertView, R.id.btn_button);
        LinearLayout llMain = ViewHolder.get(convertView, R.id.ll_main);

        btnButton.setText("确认支付");
        tvName.setText(orderinfoBean.getGNAME());
        tvPrice.setText("￥"+recordBean.getMONEY());
        cartNumber.setText(orderinfoBean.getDTYPE());
        tvCurrentTime.setText(recordBean.getADDDATE());
        tvOrderMAC.setText(orderinfoBean.getTINYIP());
        tvOrderNumber.setText(recordBean.getFORMNO());

        swipeLayout.close(false);
        swipeLayout.setOnSwipeLayoutChangedListener(onSwipeLayoutChangedListener);
        btnButton.setTag(position);
        btnButton.setOnClickListener(onButtonClickListener);
        tvSwipeDelete.setTag(position);
        tvSwipeDelete.setOnClickListener(onDeleteListener);
        llMain.setTag(position);
        llMain.setOnClickListener(onItemClikListener);
        return convertView;
    }

    private View.OnClickListener onItemClikListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            onWaitAdapterCallBack.onItemCLick(position);
        }
    };

    private View.OnClickListener onButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (onWaitAdapterCallBack!=null){
                onWaitAdapterCallBack.buttonItemCLick(position);
            }
        }
    };

    private View.OnClickListener onDeleteListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position= (int) v.getTag();
            //1、删除条目，刷新适配器
            //2、通知界面，删除数据库里面的聊天记录
            if (onWaitAdapterCallBack!=null){
                onWaitAdapterCallBack.deleteItemClick(position);
            }
        }
    };

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

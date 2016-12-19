package com.suntown.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntown.R;
import com.suntown.activity.AddressCenterActivity;
import com.suntown.bean.AddressBean;
import com.suntown.widget.SwipeLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/22.
 */
public class AddressAdapter extends BaseAdapter{
    private Context context;
    private List<AddressBean.RECORDBean> record;
    private Set<SwipeLayout> swipeLayoutSet=new HashSet<>();

    public AddressAdapter(Context context, List<AddressBean.RECORDBean> record) {
        this.context=context;
        this.record=record;
    }

    public interface OnAddressAdapterCallBack{
        void deleteItemClick(int position);//告诉外界删除条目的回调
        void onItemClick(int position);
    }
    private OnAddressAdapterCallBack onAddressAdapterCallBack;
    public void setOnAddressAdapterCallBack(OnAddressAdapterCallBack onAddressAdapterCallBack) {
        this.onAddressAdapterCallBack = onAddressAdapterCallBack;
    }

    @Override
    public int getCount() {
        return record==null?0:record.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.address_item, null);
        }
        SwipeLayout swipeLayout = ViewHolder.get(convertView, R.id.swipeLayout);
//        RelativeLayout rlMain = ViewHolder.get(convertView, R.id.rl_main);
        TextView tvName = ViewHolder.get(convertView, R.id.tv_name);
        TextView tvNumber = ViewHolder.get(convertView, R.id.tv_number);
        TextView tvAddress = ViewHolder.get(convertView, R.id.tv_address);
        TextView tvSwipeDelete = ViewHolder.get(convertView, R.id.tv_swipe_delete);
        LinearLayout llMain= ViewHolder.get(convertView, R.id.ll_main);
        AddressBean.RECORDBean recordBean = record.get(position);
        tvName.setText(recordBean.getRECEIVER());
        tvNumber.setText(recordBean.getPHONE());
        tvAddress.setText(recordBean.getADDRESS());

        swipeLayout.close(false);
        swipeLayout.setOnSwipeLayoutChangedListener(onSwipeLayoutChangedListener);

        llMain.setTag(position);
        llMain.setOnClickListener(onItemClickListener);

        tvSwipeDelete.setTag(position);
        tvSwipeDelete.setOnClickListener(onDeleteListener);
        return convertView;
    }

    private View.OnClickListener onDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position= (int) v.getTag();
            //1、删除条目，刷新适配器 通知界面，删除数据库
            if (onAddressAdapterCallBack!=null){
                onAddressAdapterCallBack.deleteItemClick(position);
            }
        }
    };
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int postion = (int) v.getTag();
            if (onAddressAdapterCallBack!= null){
                onAddressAdapterCallBack.onItemClick(postion);
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

package com.suntown.smartscreen.maintain;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.AllMainTainBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */

public class MainTainAdapter extends BaseQuickAdapter<AllMainTainBean.RECORDBean,BaseViewHolder>{
    public MainTainAdapter(List<AllMainTainBean.RECORDBean> data) {
        super(R.layout.main_tain_item, data);
    }
    public interface OnSwipeAdapterCallBack{
        void onItemClick(int position);//告诉外界删除条目的回调
        void onBottomClick(int position);//告诉外界删除条目的回调
    }
    private OnSwipeAdapterCallBack onSwipeAdapterCallBack;
    public void setOnSwipeAdapterCallBack(OnSwipeAdapterCallBack onSwipeAdapterCallBack) {
        this.onSwipeAdapterCallBack = onSwipeAdapterCallBack;
    }
    @Override
    protected void convert(BaseViewHolder holder, AllMainTainBean.RECORDBean recordBean) {
        holder.setText(R.id.tv_type_name,recordBean.DMNAME);
        holder.setText(R.id.tv_type,recordBean.TYPENAME);
        holder.setText(R.id.tv_size,recordBean.SPECNAME);
        int position = holder.getPosition();
        holder.getView(R.id.tv_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSwipeAdapterCallBack.onItemClick(position);
            }
        });
        holder.getView(R.id.ll_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSwipeAdapterCallBack.onBottomClick(position);
            }
        });
    }
}

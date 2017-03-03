package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.MessageBean;

import java.util.List;


/**
 * Created by Administrator on 2016/11/8.
 */
public class QuickAdapter extends BaseQuickAdapter<MessageBean,BaseViewHolder> {
    public QuickAdapter(int layoutResId, List<MessageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, MessageBean messageBean) {
        holder.setText(R.id.tv_time,messageBean.sendTimeStr).setText(R.id.tv_content,messageBean.content);
        ImageView ivMassage = holder.getView(R.id.iv_message);
        ivMassage.setVisibility(messageBean.lookstatus==0?View.VISIBLE:View.INVISIBLE);
    }



}

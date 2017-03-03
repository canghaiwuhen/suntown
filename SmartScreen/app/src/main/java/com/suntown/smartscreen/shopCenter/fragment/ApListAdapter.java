package com.suntown.smartscreen.shopCenter.fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.ApListBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/15.
 */

public class ApListAdapter extends BaseQuickAdapter<ApListBean.RECORDBean,BaseViewHolder>{
    public ApListAdapter(List<ApListBean.RECORDBean> data) {
        super(R.layout.ap_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ApListBean.RECORDBean recordBean) {
        holder.setText(R.id.tv_ip,recordBean.APIP);
        holder.setText(R.id.tv_apaddr,recordBean.APADDR);
        holder.setText(R.id.tv_time,recordBean.LASTTASKDATE);
    }
}

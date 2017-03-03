package com.suntown.cloudmonitoring.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.FormListBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/24.
 */

public class FormListAdapter extends BaseQuickAdapter<FormListBean.WorkformsBean,BaseViewHolder>{


    public FormListAdapter(int layoutResId, List<FormListBean.WorkformsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, FormListBean.WorkformsBean workformsBean) {
        holder.setText(R.id.tv_number,workformsBean.workNo);
        holder.setText(R.id.tv_content,workformsBean.addCause);
        holder.setText(R.id.tv_date,workformsBean.addDateStr);
        holder.setText(R.id.tv_name,workformsBean.acceptUser.userid);
    }
}

package com.suntown.smartscreen.server;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.ServerBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */

public class ServerAdapter extends BaseQuickAdapter<ServerBean.RECORDBean,BaseViewHolder> {
    private List<ServerBean.RECORDBean> record;
    public ServerAdapter(int item, List<ServerBean.RECORDBean> record) {
        super(item, record);
        this.record= record;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, ServerBean.RECORDBean recordBean) {
        viewHolder.setText(R.id.tv_text,recordBean.MODNAME);
    }
}

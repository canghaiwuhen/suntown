package com.suntown.smartscreen.shopCenter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.AllShopBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/14.
 */

public class ShopListAdapter extends BaseQuickAdapter<AllShopBean.CityBean.ShopBean,BaseViewHolder>{
    List<AllShopBean.CityBean.ShopBean> data;
    public ShopListAdapter(int item3, List<AllShopBean.CityBean.ShopBean> data) {
        super(item3,data);
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, AllShopBean.CityBean.ShopBean shopBean) {
        holder.setText(R.id.tv_name,shopBean.name);
        holder.setText(R.id.tv_sid,shopBean.id);
    }
}

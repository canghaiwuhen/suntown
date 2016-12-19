package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.HasGoodsShelfActivity;
import com.suntown.cloudmonitoring.bean.ShelfItemBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/25.
 */
public class ItemDragAdapter extends BaseItemDraggableAdapter<ShelfItemBean> {
    private static final int QUERY_OK = 200;
    Context context;
    List<ShelfItemBean> beanList;
    int parentPosition;


    public ItemDragAdapter(Context context, List<ShelfItemBean> beanList, int parentPosition) {
        super(R.layout.shelf_item,beanList);
        this.context=context;
        this.beanList=beanList;
        this.parentPosition = parentPosition;

    }

    @Override
    protected void convert(BaseViewHolder holder, ShelfItemBean shelfItemBean) {
        int position = holder.getPosition();
        boolean isClick = shelfItemBean.isClick;
        holder.setText(R.id.tv_tag, shelfItemBean.tag);
        holder.setText(R.id.tv_barcode, shelfItemBean.barcode);
        holder.setText(R.id.tv_gname, shelfItemBean.gname);
        //TODO 图标点击
        holder.getView(R.id.rl_item).setOnClickListener(v -> {
            if (mInnerListener != null) {
                mInnerListener.onItemClick(v, position);
            }
        });
        holder.getView(R.id.iv_delete).setOnClickListener(view -> {
            if (mInnerListener != null) {
                mInnerListener.onItemsDelete(view, position);
            }
        });
        if (isClick){
            holder.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
        }else{
            holder.getView(R.id.iv_delete).setVisibility(View.GONE);
        }
    }

    public interface OnInnerItemClickListener {
        void onItemClick(View view, int position);
        void onItemsDelete(View view,int position);
    }

    private OnInnerItemClickListener mInnerListener;
    public void setOnInnerItemClickListener(OnInnerItemClickListener innerListener) {
        mInnerListener = innerListener;
    }
}

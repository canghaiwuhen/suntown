package com.suntown.scannerproject.scanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.scannerproject.R;
import com.suntown.scannerproject.scanner.bean.ShelfItemBean;

import java.util.Collections;
import java.util.List;


/**
 * Created by Administrator on 2016/10/25.
 */
public class ItemDragAdapter extends BaseItemDraggableAdapter<ShelfItemBean>{
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
        boolean isTouch = shelfItemBean.isTouch;
        Log.i("123","isTouch:"+isTouch);
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
        if (isTouch){
            holder.getView(R.id.rl_item).setBackgroundResource(R.drawable.bg_border_stroke);
        }else{
            holder.getView(R.id.rl_item).setBackgroundResource(R.color.colorYellowIcon);
        }
//        holder.getView(R.id.rl_item).setBackgroundResource(isTouch?R.color.colorYellowIcon:);
    }

//    @Override
//    public void onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
//        super.onItemDragMoving(source, target);
//        mInnerListener.onItemDrag();
//    }

    public interface OnInnerItemClickListener {
        void onItemClick(View view, int position);
        void onItemsDelete(View view, int position);
//        void  onItemDrag();
    }

    private OnInnerItemClickListener mInnerListener;
    public void setOnInnerItemClickListener(OnInnerItemClickListener innerListener) {
        mInnerListener = innerListener;
    }
}

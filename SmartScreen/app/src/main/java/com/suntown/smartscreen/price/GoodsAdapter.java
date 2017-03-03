package com.suntown.smartscreen.price;

import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.GoodsInfo;
import com.suntown.smartscreen.weight.SwipeLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/2/9.
 */

public class GoodsAdapter extends BaseQuickAdapter<GoodsInfo.RECORDBean,BaseViewHolder> {
    List<GoodsInfo.RECORDBean> record;
    private Set<SwipeLayout> swipeLayoutSet=new HashSet<>();
    public GoodsAdapter(int item,List<GoodsInfo.RECORDBean> data) {
        super(item,data);
        this.record=data;
    }
    public interface OnSwipeAdapterCallBack{
        void deleteItemClick(int position);//告诉外界删除条目的回调
    }
    private OnSwipeAdapterCallBack onSwipeAdapterCallBack;
    public void setOnSwipeAdapterCallBack(OnSwipeAdapterCallBack onSwipeAdapterCallBack) {
        this.onSwipeAdapterCallBack = onSwipeAdapterCallBack;
    }
    @Override
    protected void convert(BaseViewHolder holder, GoodsInfo.RECORDBean recordBean) {
        holder.setText(R.id.tv_tinyip_name,recordBean.TINYIP);
        holder.setText(R.id.tv_barcode,recordBean.BARCODE);
        holder.setText(R.id.tv_name,recordBean.GNAME);
        SwipeLayout swipeLayout = holder.getView(R.id.swipeLayout);
        TextView tvDelete = holder.getView(R.id.tv_delete);
        swipeLayout.close(false);
        swipeLayout.setOnSwipeLayoutChangedListener(onSwipeLayoutChangedListener);
        tvDelete.setTag(holder.getPosition());
        tvDelete.setOnClickListener(onDeleteListener);
    }
    //点击“删除”要删除条目的方法
    //1、删除条目，刷新适配器  2、通知后台删除数据
    private View.OnClickListener onDeleteListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position= (int) v.getTag();
            //1、删除条目，刷新适配器
            //2、通知界面，删除数据库里面的聊天记录
            if (onSwipeAdapterCallBack!=null){
                onSwipeAdapterCallBack.deleteItemClick(position);
            }
        }
    };

    public SwipeLayout.OnSwipeLayoutChangedListener onSwipeLayoutChangedListener=new SwipeLayout.OnSwipeLayoutChangedListener(){
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
    //获取打开条目的数量
    public int getOpenSwipeLayoutCount() {
        return swipeLayoutSet.size();
    }
    //关闭所有的条目
    public void closeAllSwipeLayout() {
        for (SwipeLayout sl : swipeLayoutSet) {
            sl.close(false);
        }
    }
}

package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.HasGoodsShelfActivity;
import com.suntown.cloudmonitoring.bean.ShelfItemBean;
import com.suntown.cloudmonitoring.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/25.
 */
public class RecyclerMainAdapter extends RecyclerView.Adapter<RecyclerMainAdapter.MyViewHolder> {
    Context context;
    List<Integer> indexList;
    Map<Integer, List<ShelfItemBean>> listMap;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
    private ItemTouchHelper mItemTouchHelper;
    private OnGroupItemClickListener mGroupItemListener;
    public interface OnGroupItemClickListener {
        void onChildItemClick(View view, int group, int position);
        void onChildItemDeleteClick(View view, int group, int position);
        void onAddClick(View view, int group);
        void onCutClick(View view, int group);
        void onItemDrag();
    }

    public void setOnGroupItemClickListener(OnGroupItemClickListener l) {
        mGroupItemListener = l;
    }

    public RecyclerMainAdapter(Context context, List<Integer> indexList, Map<Integer, List<ShelfItemBean>> listMap) {
        this.context=context;
        this.indexList=indexList;
        this.listMap=listMap;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder =new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_main,parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        int i = indexList.size()-position;
        holder.tvTitle.setText("第"+i+"层");
        holder.rlitem.setLayoutManager(new GridLayoutManager(context, 3));
        Integer integer = indexList.get(position);
        Log.i("Adapter","integer-"+integer.toString());
        List<ShelfItemBean> beanList = listMap.get(integer);
//        Log.i("Adapter",beanList.toString());
        boolean isClick = beanList.get(beanList.size()-1).isClick;
        if (isClick){
            holder.ivCut.setImageResource(R.drawable.delete_3);
        }else{
            holder.ivCut.setImageResource(R.drawable.cut);
        }

        ItemDragAdapter mAdapter = new ItemDragAdapter(context,beanList,integer);
        mAdapter.setOnInnerItemClickListener(new ItemDragAdapter.OnInnerItemClickListener() {
            //条目点击
            @Override
            public void onItemClick(View view, int innerPosition) {
//                Toast.makeText(view.getContext(), "out:" + position + " inner:" + innerPosition, Toast.LENGTH_SHORT).show();
                if (mGroupItemListener != null) {
                    mGroupItemListener.onChildItemClick(view, integer, innerPosition);
                }
            }
            //点击删除
            @Override
            public void onItemsDelete(View view, int innerPosition) {
                if (mGroupItemListener != null) {
                    mGroupItemListener.onChildItemDeleteClick(view,integer,innerPosition);
                }
            }
        });
        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(holder.rlitem);
//        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
//        mAdapter.enableSwipeItem();
        mAdapter.enableDragItem(mItemTouchHelper);
        mAdapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder viewHolder, int i, RecyclerView.ViewHolder viewHolder1, int i1) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int i) {
                mGroupItemListener.onItemDrag();
            }
        });
        holder.rlitem.setAdapter(mAdapter);

        holder.ivAdd.setOnClickListener(v -> {
            if (mGroupItemListener != null) {
                mGroupItemListener.onAddClick(v,integer);
            }
        });
        holder.ivCut.setOnClickListener(v -> {
            if (mGroupItemListener != null) {
                mGroupItemListener.onCutClick(v,integer);
            }
        });

    }

    @Override
    public int getItemCount() {
        return indexList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rlitem;
        ImageView ivAdd;
        ImageView ivCut;
        TextView tvTitle;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivAdd = (ImageView) itemView.findViewById(R.id.iv_add);
            ivCut = (ImageView) itemView.findViewById(R.id.iv_cut);
            rlitem = (RecyclerView) itemView.findViewById(R.id.rl_item);
        }

    }
}

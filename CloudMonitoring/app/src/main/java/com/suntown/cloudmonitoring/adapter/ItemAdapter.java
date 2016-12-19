package com.suntown.cloudmonitoring.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemView>{
    private List<String> data;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener ;

    public ItemAdapter(List<String> data) {
        this.data = data;
    }

    public interface OnRecyclerViewItemClickListener {
        void onRecycleViewItemClick(View view , int position);
    }

    public void setOnRecyclerViewItemClickListener (OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this .onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draggable_view, parent, false);
        view.setOnClickListener(v -> {
//            if (onRecyclerViewItemClickListener!= null){
//                //注意这里使用getTag方法获取数据01
//                onRecyclerViewItemClickListener .onRecycleViewItemClick(parent,Integer. parseInt(parent.getTag().toString())) ;
//            }
        });
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        holder.textView.setText(data.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return data.size()==0?0:data.size();
    }


    public static class ItemView extends RecyclerView.ViewHolder {
        TextView textView;
        public ItemView(View itemView){
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.tv_text);
        }
    }
}

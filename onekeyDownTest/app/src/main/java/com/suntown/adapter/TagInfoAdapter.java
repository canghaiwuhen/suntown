package com.suntown.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.suntown.R;
import com.suntown.bean.DeviceInfoBean;
import com.suntown.bean.SwipeData;
import com.suntown.widget.SwipeLayout;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/12.
 */
public class TagInfoAdapter extends BaseAdapter{
    private List<DeviceInfoBean.DeviceInfoAndMemidBean> record;
    private Set<SwipeLayout> swipeLayoutSet=new HashSet<>();
    private Context context;

    public TagInfoAdapter(Context context,List<DeviceInfoBean.DeviceInfoAndMemidBean> record) {
        this.record = record;
        this.context = context;
    }

    public interface OnSwipeAdapterCallBack{
         void onItemClick(int position);//告诉外界条目单击
         void deleteItemClick(int position);//告诉外界删除条目的回调
         void confrimItemClick(int position);//告诉外界点击配置条目的回调
         void bindItemClick(int position);//告诉外界点击绑定条目的回调
    }
    private OnSwipeAdapterCallBack onSwipeAdapterCallBack;
    public void setOnSwipeAdapterCallBack(OnSwipeAdapterCallBack onSwipeAdapterCallBack) {
        this.onSwipeAdapterCallBack = onSwipeAdapterCallBack;
    }

    @Override
    public int getCount() {
        return record == null ? 0 : record.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.device_list_item, null);
        }
        SwipeLayout swipeLayout = ViewHolder.get(convertView, R.id.swipeLayout);
        TextView tvSwipeConfirm = ViewHolder.get(convertView, R.id.tv_swipe_confirm);
        TextView tvSwipeDelete = ViewHolder.get(convertView, R.id.tv_swipe_delete);
        TextView tvSwipeBind = ViewHolder.get(convertView, R.id.tv_swipe_bind);
        RelativeLayout rlMain = ViewHolder.get(convertView, R.id.rl_main);
        TextView tvTagNum = ViewHolder.get(convertView, R.id.tv_tag_num);
        TextView tvGoodsName = ViewHolder.get(convertView, R.id.tv_goods_name);

        tvGoodsName.setText(record.get(position).getGNAME());
        tvTagNum.setText(record.get(position).getTINYIP());
        swipeLayout.close(false);
//        swipeLayout.setTag(position);
        swipeLayout.setOnSwipeLayoutChangedListener(onSwipeLayoutChangedListener);
        //给条目添加单击事件
        rlMain.setTag(position);
        rlMain.setOnClickListener(onItemClickListener);
        //点击“删除”要删除条目
        tvSwipeDelete.setTag(position);
        tvSwipeDelete.setOnClickListener(onDeleteListener);
        //点击配置的回调
        tvSwipeConfirm.setTag(position);
        tvSwipeConfirm.setOnClickListener(onConfirmListener);
        //点击绑定的回调
        tvSwipeBind.setTag(position);
        tvSwipeBind.setOnClickListener(onBindListener);
        return convertView;
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
    //配置的点击事件
    private View.OnClickListener onConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onSwipeAdapterCallBack!=null){
                int  position = (int) view.getTag();
                onSwipeAdapterCallBack.confrimItemClick(position);
            }
        }
    };
    //绑定的点击事件
    private View.OnClickListener onBindListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onSwipeAdapterCallBack!=null){
                int position = (int) view.getTag();
                onSwipeAdapterCallBack.bindItemClick(position);
            }
        }
    };

    //给条目添加单击事件方法
    private View.OnClickListener onItemClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //通知界面。单击了条目
            if(onSwipeAdapterCallBack!=null){
                int position= (int) v.getTag();
                onSwipeAdapterCallBack.onItemClick(position);
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

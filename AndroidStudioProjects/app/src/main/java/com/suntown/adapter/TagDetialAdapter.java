package com.suntown.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.suntown.R;
import com.suntown.bean.DeviceInfoBean;
import com.suntown.widget.SwipeLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/12.
 */
public class TagDetialAdapter extends BaseQuickAdapter<DeviceInfoBean.RECORDBean,BaseViewHolder> {

    private  List<DeviceInfoBean.RECORDBean> list;
    private Context context;
    private Set<SwipeLayout> swipeLayoutSet=new HashSet<>();

    public TagDetialAdapter(Context context, List<DeviceInfoBean.RECORDBean> list) {
        super(R.layout.device_list_item,list);
        this.context=context;
        this.list = list;
    }

    public interface OnSwipeAdapterCallBack{
        void onItemClick(int position);//告诉外界条目单击
        void deleteItemClick(int position);//告诉外界删除条目的回调
        void confrimItemClick(int position);//告诉外界点击配置条目的回调
        void bindItemClick(int position);//告诉外界点击绑定条目的回调
    }
    private TagInfoAdapter.OnSwipeAdapterCallBack onSwipeAdapterCallBack;
    public void setOnSwipeAdapterCallBack(TagInfoAdapter.OnSwipeAdapterCallBack onSwipeAdapterCallBack) {
        this.onSwipeAdapterCallBack = onSwipeAdapterCallBack;
    }


    @Override
    protected void convert(BaseViewHolder holder, DeviceInfoBean.RECORDBean bean) {
        SwipeLayout swipeLayout = holder.getView(R.id.swipeLayout);
        TextView tvSwipeConfirm = holder.getView(R.id.tv_swipe_confirm);
        TextView tvSwipeDelete = holder.getView(R.id.tv_swipe_delete);
        TextView tvSwipeBind = holder.getView(R.id.tv_swipe_bind);
        LinearLayout rlMain = holder.getView(R.id.rl_main);
        TextView tvState = holder.getView(R.id.tv_state);
        ImageView ivPhoto = holder.getView(R.id.iv_photo);
        int position = holder.getPosition();
        holder.setText(R.id.tv_tag_num,bean.TINYIP);
        String gname = bean.GNAME;
        holder.setText(R.id.tv_goods_name, gname);
        String wifiid = bean.WIFIID;
        holder.setText(R.id.tv_link, wifiid);
        String imgpath = bean.IMGPATH;
        if ("".equals(wifiid)||"".equals(gname)) {
            tvState.setBackgroundResource(R.drawable.alert);
            tvState.setText("异常");
        }else{
            tvState.setBackgroundResource(R.drawable.normal);
            tvState.setText("正常");
        }
        Log.i("TagDetialAdapter",imgpath);
        Picasso.with(context).load(imgpath).error(R.drawable.no_photo).into(ivPhoto);
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

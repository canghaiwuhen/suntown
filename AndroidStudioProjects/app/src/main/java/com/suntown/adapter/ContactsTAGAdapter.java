package com.suntown.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.suntown.R;
import com.suntown.bean.DeviceInfoBean;
import com.suntown.bean.UserTagBean;
import com.suntown.widget.SwipeLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/12.
 */
public class ContactsTAGAdapter extends BaseQuickAdapter<UserTagBean.RECORDBean,BaseViewHolder> {

    private List<UserTagBean.RECORDBean> userTagBeanRECORD;
    private Context context;



    public ContactsTAGAdapter(Context context, List<UserTagBean.RECORDBean> userTagBeanRECORD) {
        super(R.layout.constact_items,userTagBeanRECORD);
        this.context=context;
        this.userTagBeanRECORD = userTagBeanRECORD;
    }


    @Override
    protected void convert(BaseViewHolder holder, UserTagBean.RECORDBean recordBean) {
        TextView tvLink = holder.getView(R.id.tv_link);
        TextView tvState = holder.getView(R.id.tv_state);
        ImageView ivPhoto = holder.getView(R.id.iv_photo);
        int position = holder.getPosition();
        holder.setText(R.id.tv_tag_num,recordBean.getTINYIP());
        holder.setText(R.id.tv_goods_name,recordBean.getGNAME());
        Picasso.with(context).load(recordBean.getIMGPATH()).error(R.drawable.goods).into(ivPhoto);
    }
}

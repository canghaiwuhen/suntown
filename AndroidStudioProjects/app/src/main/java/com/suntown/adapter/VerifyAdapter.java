package com.suntown.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suntown.R;
import com.suntown.activity.VerifyFriendsActivity;
import com.suntown.bean.AddListBean;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */
public class VerifyAdapter extends BaseAdapter{
    Context context;
    List<AddListBean.RECORDBean> recordBeanList;
    public VerifyAdapter(Context context, List<AddListBean.RECORDBean> recordBeanList) {
        this.context=context;
        this.recordBeanList=recordBeanList;
    }

    @Override
    public int getCount() {
        return recordBeanList.size()==0?0: recordBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        String userName = SPUtils.getString(context, Constant.NICKNAME);
        AddListBean.RECORDBean recordBean = recordBeanList.get(position);
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.add_list_items, null);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
            viewHolder.tvAgree = (TextView) convertView.findViewById(R.id.tv_agree);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AddListBean.RECORDBean.FMBean fm = recordBean.FM;
        AddListBean.RECORDBean.SELFBean self = recordBean.SELF;

//        FM:接受者 SELF:发送者
        if (userName.equals(self.NICKNAME)) {
            viewHolder.tvContent.setText("你请求添加"+fm.NICKNAME+"为好友");
            String avatar = fm.AVATAR;
            String url = Utils.replaceString(avatar);
            Picasso.with(context).load(url).error(R.drawable.user).into(viewHolder.ivPhoto);
        }else if (userName.equals(fm.NICKNAME)){
            String avatar = self.AVATAR;
            String url = Utils.replaceString(avatar);
            viewHolder.tvContent.setText(self.NICKNAME+"请求添加你为好友");
            Picasso.with(context).load(url).error(R.drawable.user).into(viewHolder.ivPhoto);
        }
//        PASSFLAG 1 -1
        String passflag = recordBean.PASSFLAG;
        if (passflag.equals("1")) {
            viewHolder.tvAgree.setText("已同意");
            viewHolder.tvAgree.setTextColor(Color.RED);
        }else if (passflag.equals("-1")){
            viewHolder.tvAgree.setText("未同意");
        }

        return convertView;
    }
    class ViewHolder {
        ImageView ivPhoto;
        TextView tvAgree;
        TextView tvContent;
    }
}

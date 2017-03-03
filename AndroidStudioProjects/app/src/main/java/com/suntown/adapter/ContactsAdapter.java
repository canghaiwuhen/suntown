package com.suntown.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suntown.R;
import com.suntown.bean.PhoneInfo;
import com.suntown.bean.UserInfoBean;
import com.suntown.utils.Utils;
import com.suntown.widget.CircleImageView;
import com.suntown.widget.RoundAngleImageView;
import com.suntown.widget.SwipeLayout;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/2.
 */
public class ContactsAdapter extends BaseAdapter{
    private static final String TAG = "ContactsAdapter";
    private List<UserInfoBean.RECORDBean> lists;
    private Context context;
    private Set<SwipeLayout> swipeLayoutSet=new HashSet<>();
    public ContactsAdapter( List<UserInfoBean.RECORDBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }
    public interface OnDeleteAdapterCallBack {
        void deleteItemClick(int position);//
        void onItemClick(int position);
    }

    private OnDeleteAdapterCallBack onDeleteAdapterCallBack;
    public void setOnWaitAdapterCallBack(OnDeleteAdapterCallBack onDeleteAdapterCallBack) {
        this.onDeleteAdapterCallBack = onDeleteAdapterCallBack;
    }

    //返回集合的数量
    @Override
    public int getCount() {
        return lists==null?0:lists.size();
    }

    //返回当前数据
    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    //获取当前ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserInfoBean.RECORDBean recordBean = lists.get(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, null);
        }
        TextView tvName = ViewHolder.get(convertView, R.id.tv_nick);
        RelativeLayout rlMain = ViewHolder.get(convertView, R.id.rl_main);
        TextView tvNumber = ViewHolder.get(convertView, R.id.tv_number);
        TextView tvDelete = ViewHolder.get(convertView, R.id.tv_delete);
        SwipeLayout swipeLayout = ViewHolder.get(convertView, R.id.swipeLayout);
        RoundAngleImageView ivPhoto = ViewHolder.get(convertView, R.id.iv_photo);
        String avatar = recordBean.AVATAR;
        if ("".equals(avatar)) {
            ivPhoto.setImageResource(R.drawable.user);
        }else{
            String url = Utils.replaceString(avatar);
            Log.i(TAG,"url:"+url);
            Picasso.with(context).load(url).error(R.drawable.user).into(ivPhoto);
//            ivPhoto.setImageBitmap(getHttpBitmap(avatar));
        }
        swipeLayout.close(false);
        swipeLayout.setOnSwipeLayoutChangedListener(onSwipeLayoutChangedListener);
        tvDelete.setTag(position);
        tvDelete.setOnClickListener(v -> {
            if (onDeleteAdapterCallBack!=null){
                onDeleteAdapterCallBack.deleteItemClick(position);
            }
        });
        rlMain.setOnClickListener(v -> {
            if (onDeleteAdapterCallBack!=null){
                onDeleteAdapterCallBack.onItemClick(position);
            }
        });
        tvName.setText(recordBean.NICKNAME);
        tvNumber.setText(recordBean.TEL);
        return convertView;
    }

    private SwipeLayout.OnSwipeLayoutChangedListener onSwipeLayoutChangedListener=new SwipeLayout.OnSwipeLayoutChangedListener(){
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
}

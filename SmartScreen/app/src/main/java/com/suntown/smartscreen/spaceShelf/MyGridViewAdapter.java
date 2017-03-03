package com.suntown.smartscreen.spaceShelf;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.ShelfInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class MyGridViewAdapter extends BaseAdapter {
    Context context;
    List<ShelfInfoBean.RECORDBean> recordBeen;
    public MyGridViewAdapter(Context context, List<ShelfInfoBean.RECORDBean> recordBeen) {
        this.context =context;
        this.recordBeen =recordBeen;
    }

    @Override
    public int getCount() {
        return recordBeen.size();
    }

    @Override
    public Object getItem(int i) {
        return recordBeen.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (null == convertView) {
            convertView = View.inflate(context, R.layout.gridview_item, null);
        }
        ShelfInfoBean.RECORDBean recordBean = recordBeen.get(i);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvBarcode = (TextView) convertView.findViewById(R.id.tv_barcode);
        TextView tvIp = (TextView) convertView.findViewById(R.id.tv_ip);
        TextView tvShelf = (TextView) convertView.findViewById(R.id.tv_shelf);
        //photo
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
        TextView tvPhotoName = (TextView) convertView.findViewById(R.id.tv_photo_name);
        TextView tvIpName = (TextView) convertView.findViewById(R.id.tv_ip_name);
        LinearLayout llPhoto = (LinearLayout) convertView.findViewById(R.id.ll_photo);
        LinearLayout llDetial = (LinearLayout) convertView.findViewById(R.id.ll_detial);
        tvName.setText(recordBean.GNAME);
        tvPhotoName.setText(recordBean.GNAME);
        tvBarcode.setText(recordBean.BARCODE);
        tvIp.setText(recordBean.TINYIP);
        tvIpName.setText(recordBean.TINYIP);
        tvShelf.setText(recordBean.AID);
        String filename = recordBean.FILENAME;
        if (recordBean.isToggleOn) {
            llPhoto.setVisibility(View.VISIBLE);
            llDetial.setVisibility(View.GONE);
        }else{
            llPhoto.setVisibility(View.GONE);
            llDetial.setVisibility(View.VISIBLE);
        }
        if (!"".equals(filename)) {
            Picasso.with(context).load(filename).error(R.drawable.hodeip).into(ivPhoto);
        }
        return convertView;
    }
}

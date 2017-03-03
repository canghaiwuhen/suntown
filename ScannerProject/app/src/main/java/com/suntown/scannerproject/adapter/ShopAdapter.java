package com.suntown.scannerproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.suntown.scannerproject.R;
import com.suntown.scannerproject.bean.ShopXmlBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ShopAdapter extends BaseAdapter{
    Context context;
    List<ShopXmlBean> shopXmlBeen;

    public ShopAdapter(Context context, List<ShopXmlBean> shopXmlBeen) {
        this.context=context;
        this.shopXmlBeen=shopXmlBeen;
    }

    @Override
    public int getCount() {
        return shopXmlBeen.size()==0?0:shopXmlBeen.size();
    }

    @Override
    public Object getItem(int i) {
        return shopXmlBeen.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context, R.layout.item3, null);
        }
        ((TextView) view.findViewById(R.id.tv_text)).setText(shopXmlBeen.get(i).TinyIp);
        return view;
    }
}

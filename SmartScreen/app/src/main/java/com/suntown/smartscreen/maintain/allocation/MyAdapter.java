package com.suntown.smartscreen.maintain.allocation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.suntown.smartscreen.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/8.
 */

public class MyAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<InfoBean> nameList;
    private boolean isCheched;

    public MyAdapter(Context context, ArrayList<InfoBean> nameList) {
        this.context=context;
        this.nameList=nameList;
    }


//    public interface OnAdapterCallBack{
//        void onChecked(int position);//
//    }
//    private OnAdapterCallBack onAdapterCallBack;
//    public void setOnAdapterCallBack(OnAdapterCallBack onAdapterCallBack) {
//        this.onAdapterCallBack = onAdapterCallBack;
//    }

    @Override
    public int getCount() {
        return nameList.size()==0?0:nameList.size();
    }

    @Override
    public Object getItem(int i) {
        return nameList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        convertView = LayoutInflater.from(context).inflate(R.layout.text_items1, null);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_text);
        RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.rb);
//        LinearLayout llMain = (LinearLayout) convertView.findViewById(R.id.ll_main);
        InfoBean infoBean = nameList.get(i);
        tvName.setText(infoBean.name);
        radioButton.setVisibility(infoBean.isShow?View.VISIBLE:View.GONE);
        isCheched = infoBean.isCheched;
        radioButton.setChecked(isCheched);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCheched =!isCheched;
                infoBean.isCheched = isCheched;
                radioButton.setChecked(isCheched);
            }
        });
        return convertView;
    }
}

package com.suntown.adapter;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.R;
import com.suntown.bean.AddressBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/22.
 */
public class AddressAdapter extends BaseQuickAdapter<AddressBean.RECORDBean,BaseViewHolder> {
    List<AddressBean.RECORDBean> record;
    public AddressAdapter(int address_item, List<AddressBean.RECORDBean> record) {
        super(address_item, record);
        this.record=record;
    }

    @Override
    protected void convert(BaseViewHolder holder, AddressBean.RECORDBean recordBean) {
        ((TextView) holder.getView(R.id.tv_name)).setText(recordBean.RECEIVER);
        ((TextView) holder.getView(R.id.tv_number)).setText(recordBean.PHONE);
        ((TextView) holder.getView(R.id.tv_address)).setText(recordBean.ADDRESS);
        CheckBox checkBox = holder.getView(R.id.cb_check);
        int position = holder.getPosition();
        if (recordBean.ISDEFAULT.equals("1")) {
            checkBox.setChecked(true);
            holder.getView(R.id.tv_default).setVisibility(View.VISIBLE);
        }else{
            checkBox.setChecked(false);
            holder.getView(R.id.tv_default).setVisibility(View.GONE);
        }
//        Log.i("isClick","isClick:"+recordBean.isClick);
        if (recordBean.isClick) {
            holder.getView(R.id.ll_main_edit).setVisibility(View.VISIBLE);
            holder.getView(R.id.view).setVisibility(View.VISIBLE);
        }else{
            holder.getView(R.id.view).setVisibility(View.GONE);
            holder.getView(R.id.ll_main_edit).setVisibility(View.GONE);
        }
        holder.getView(R.id.ll_edit).setOnClickListener(v -> {
            if (onAddressAdapterCallBack!= null){
                onAddressAdapterCallBack.onEditClick(position);
            }
        });
        holder.getView(R.id.ll_main).setTag(position);
        holder.getView(R.id.ll_main).setOnClickListener(v -> {
            if (onAddressAdapterCallBack!= null){
                onAddressAdapterCallBack.onItemClick(position);
            }
        });
        checkBox.setOnClickListener(v -> onAddressAdapterCallBack.onCheckClick(position));
        holder.getView(R.id.ll_delete).setOnClickListener(v -> {
            if (onAddressAdapterCallBack!=null){
                onAddressAdapterCallBack.deleteItemClick(position);
            }
        });
    }

    public interface OnAddressAdapterCallBack{
        void deleteItemClick(int position);//告诉外界删除条目的回调
        void onItemClick(int position);
        void onCheckClick(int position);
        void onEditClick(int position);
    }
    private OnAddressAdapterCallBack onAddressAdapterCallBack;
    public void setOnAddressAdapterCallBack(OnAddressAdapterCallBack onAddressAdapterCallBack) {
        this.onAddressAdapterCallBack = onAddressAdapterCallBack;
    }



    private View.OnClickListener onEditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int postion = (int) v.getTag();
            if (onAddressAdapterCallBack!= null){
                onAddressAdapterCallBack.onItemClick(postion);
            }
        }
    };

}

package com.suntown.smartscreen.price.changePrice;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.GoodsInfo;
import com.suntown.smartscreen.price.GoodsAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/9.
 */

public class ChangeAdapter extends BaseQuickAdapter<GoodsInfo.RECORDBean,BaseViewHolder> {
    ArrayList<GoodsInfo.RECORDBean> recordBeanList;
    public ChangeAdapter(int query_item, ArrayList<GoodsInfo.RECORDBean> recordBeanList) {
        super(query_item, recordBeanList);
        this.recordBeanList = recordBeanList;
    }

    public interface OnSwipeAdapterCallBack{
        void startTimeClick(int position);//
        void endTimeClick(int position);//
    }
    private OnSwipeAdapterCallBack onSwipeAdapterCallBack;
    public void setOnSwipeAdapterCallBack(OnSwipeAdapterCallBack onSwipeAdapterCallBack) {
        this.onSwipeAdapterCallBack = onSwipeAdapterCallBack;
    }

    @Override
    protected void convert(BaseViewHolder holder, GoodsInfo.RECORDBean recordBean) {
        String type = recordBean.TYPE;
        Log.i(TAG,"type"+type);
        //1 调整会员价  调整促销价格  调整现价
        if ("0".equals(type)){
            holder.setText(R.id.tv_change,"调整会员价");
            holder.setText(R.id.tv,"调整会员价");
            holder.setText(R.id.tv_set_price,"会  员  价:");
        }else if ("1".equals(type)){
            holder.setText(R.id.tv,"调整促销价格");
            holder.setText(R.id.tv_set_price,"促  销  价:");
            holder.setText(R.id.tv_change,"调整促销价格");
        }else if ("2".equals(type)){
            holder.setText(R.id.tv,"调整现价");
            holder.setText(R.id.tv_set_price,"现        价:");
            holder.setText(R.id.tv_change,"调整现价");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String currentTime = df.format(new Date());
        int position = holder.getPosition();
        if (position!=0) {
            holder.getView(R.id.tv_start_time).setBackgroundResource(R.drawable.bg);
            holder.getView(R.id.tv_end_time).setBackgroundResource(R.drawable.bg);
        }
        String starttime = recordBean.STARTTIME;
        String endtime = recordBean.ENDTIME;
        String vip = recordBean.VIP;
        EditText etVip =  holder.getView(R.id.et_price);
        etVip.setText("".equals(vip)?"":vip);
        holder.setText(R.id.tv_start_time,"".equals(starttime)?currentTime:starttime);
        holder.setText(R.id.tv_end_time,"".equals(endtime)?currentTime:endtime);
        holder.setText(R.id.tv_price,recordBean.CURPRICE);
        holder.setText(R.id.tv_old_price,recordBean.COSTPRICE);
        holder.setText(R.id.tv_vip_price,recordBean.MEMPRICE);
        holder.setText(R.id.tv_tinyip,recordBean.TINYIP);
        holder.setText(R.id.tv_barcode,recordBean.BARCODE);
        holder.setText(R.id.tv_name,recordBean.GNAME);
        etVip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recordBean.VIP = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.getView(R.id.ll_start_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSwipeAdapterCallBack!=null){
                    onSwipeAdapterCallBack.startTimeClick(position);
                }
            }
        });
        holder.getView(R.id.ll_end_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSwipeAdapterCallBack!=null){
                    onSwipeAdapterCallBack.endTimeClick(position);
                }
            }
        });
    }
}

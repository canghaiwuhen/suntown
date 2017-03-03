package com.suntown.smartscreen.price.changePrice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.GoodsInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/9.
 */

public class ChangeRlAdapter extends RecyclerView.Adapter<ChangeRlAdapter.MyViewHolder>{

    private static final String TAG = "ChangeRlAdapter";
    Context context;
    ArrayList<GoodsInfo.RECORDBean> recordBeanList;
    public ChangeRlAdapter(Context context, ArrayList<GoodsInfo.RECORDBean> recordBeanList) {
        this.context=context;
        this.recordBeanList=recordBeanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder =new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.change_price_item,parent, false));
        return holder;
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        GoodsInfo.RECORDBean recordBean = recordBeanList.get(position);
        String type = recordBean.TYPE;
        Log.i(TAG,"type"+type);
        //1 调整会员价  调整促销价格  调整现价
        if ("0".equals(type)){
            holder.tvChange.setText("调整会员价");
            holder.tv.setText("调整会员价");
            holder.tvSetPrice.setText("会  员  价:");
        }else if ("1".equals(type)){
            holder.tvChange.setText("调整促销价格");
            holder.tv.setText("调整促销价格");
            holder.tvSetPrice.setText("促  销  价:");
        }else if ("2".equals(type)){
            holder.tvChange.setText("调整现价");
            holder.tv.setText("调整现价");
            holder.tvSetPrice.setText("现        价:");

        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String currentTime = df.format(new Date());
        if (position!=0) {
            holder.tvStartTime.setBackgroundResource(R.drawable.bg);
            holder.tvEndtTime.setBackgroundResource(R.drawable.bg);
        }
        String starttime = recordBean.STARTTIME;
        String endtime = recordBean.ENDTIME;
        String vip = recordBean.VIP;
        holder.etPrice.setText("".equals(vip)?"":vip);
        holder.tvStartTime.setText("".equals(starttime)?currentTime:starttime);
        holder.tvEndtTime.setText("".equals(endtime)?currentTime:endtime);
        holder.tvPrice.setText(recordBean.CURPRICE);
        holder.tvOldPrice.setText(recordBean.COSTPRICE);
        holder.tvVipPrice.setText(recordBean.MEMPRICE);
        holder.tvTinyip.setText(recordBean.TINYIP);
        holder.tvBarcode.setText(recordBean.BARCODE);
        holder.tvName.setText(recordBean.GNAME);
        holder.etPrice.addTextChangedListener(new TextWatcher() {
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
        holder.llStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSwipeAdapterCallBack!=null){
                    onSwipeAdapterCallBack.startTimeClick(position);
                }
            }
        });
        holder.llEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSwipeAdapterCallBack!=null){
                    onSwipeAdapterCallBack.endTimeClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvStartTime;
        TextView tvEndtTime;
        TextView tvPrice;
        TextView tvSetPrice;
        TextView tvOldPrice;
        TextView tvVipPrice;
        TextView tvTinyip;
        TextView tvBarcode;
        TextView tvName;
        TextView tvChange;
        TextView tv;
        EditText etPrice;
        LinearLayout llStartTime;
        LinearLayout llEndTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvStartTime = (TextView) itemView.findViewById(R.id.tv_start_time);
            tvEndtTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvSetPrice = (TextView) itemView.findViewById(R.id.tv_set_price);
            tvOldPrice = (TextView) itemView.findViewById(R.id.tv_old_price);
            tvVipPrice = (TextView) itemView.findViewById(R.id.tv_vip_price);
            tvTinyip = (TextView) itemView.findViewById(R.id.tv_tinyip);
            tvBarcode = (TextView) itemView.findViewById(R.id.tv_barcode);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvChange = (TextView) itemView.findViewById(R.id.tv_change);
            tv = (TextView) itemView.findViewById(R.id.tv);
            etPrice = (EditText) itemView.findViewById(R.id.et_price);
            llStartTime = (LinearLayout) itemView.findViewById(R.id.ll_start_time);
            llEndTime = (LinearLayout) itemView.findViewById(R.id.ll_end_time);
        }
    }
}

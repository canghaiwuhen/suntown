package com.suntown.scannerproject.input.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntown.scannerproject.R;
import com.suntown.scannerproject.input.bean.BooleanBean;
import com.suntown.scannerproject.input.bean.InputBean;
import com.suntown.scannerproject.input.bean.PDBean;

import java.util.List;


/**
 * Created by Administrator on 2016/11/1.
 */
public class Check1Adapter extends BaseQuickAdapter<PDBean,BaseViewHolder> {

    private int mCurrentTouchedIndex=-1;

    public Check1Adapter(int check_item, List<PDBean> pdBeanList) {
        super(check_item, pdBeanList);
    }

    public interface OnItemClickCallBack {
        void onItemClick(int position);//告诉外界条目单击
    }
    private OnItemClickCallBack onitemClickCallBack;

    public void SetOnItemClickCallBack(OnItemClickCallBack onitemClickCallBack){
        this.onitemClickCallBack = onitemClickCallBack;
    }


    @Override
    protected void convert(BaseViewHolder holder, PDBean pdBean) {
        String martNum = pdBean.martNum;
        String storeNum = pdBean.storeNum;
        boolean isToggle = pdBean.isToggle;
        Log.i("CheckAdapter","isToggle:"+isToggle);
        holder.setText(R.id.tv_barcode_num,pdBean.BARCODE).setText(R.id.tv_name,pdBean.GOODSNAME);
        EditText etMartStock =  holder.getView(R.id.et_mart_stock);
        EditText etStoreStock = holder.getView(R.id.et_store_stock);
        EditText etMartNum = holder.getView(R.id.et_mart_num);
        EditText etStorkNum = holder.getView(R.id.et_stork_num);
        ImageView ivDelete = holder.getView(R.id.iv_delete);
        etMartNum.setText("".equals(martNum)?"":martNum);
        etStorkNum.setText("".equals(storeNum)?"":storeNum);
        int position = holder.getLayoutPosition();
        ivDelete.setOnClickListener(view -> onitemClickCallBack.onItemClick(position));
        if (isToggle){
            etMartNum.setEnabled(true);
            etStorkNum.setEnabled(false);
            if (position==0){
               etMartNum.requestFocus();
            }
            etMartNum.setBackgroundResource(R.color.colorGary);
            etStorkNum.setBackgroundResource(R.color.colorBlackBg);
        }else{
            etMartNum.setEnabled(false);
            etStorkNum.setEnabled(true);
            if (position==0){
               etStorkNum.requestFocus();
            }
            etStorkNum.setBackgroundResource(R.color.colorGary);
            etMartNum.setBackgroundResource(R.color.colorBlackBg);
        }


        etStorkNum.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mCurrentTouchedIndex = position;
            }
            return false;
        });
        etStorkNum.clearFocus();
        if (position == mCurrentTouchedIndex) {
            // 如果该项中的EditText是要获取焦点的
            etStorkNum.requestFocus();
        }
        etMartNum.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mCurrentTouchedIndex = position;
            }
            return false;
        });
        etMartNum.clearFocus();
        if (position == mCurrentTouchedIndex) {
            // 如果该项中的EditText是要获取焦点的
            etMartNum.requestFocus();
        }

        etMartNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pdBean.martNum= charSequence.toString();

                Log.i("MainActivity", "onTextChanged: " + position + " boxNum - " +  pdBean.martNum);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etStorkNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pdBean.storeNum = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

//    @Override
//    public View getView(int i, View convertView, ViewGroup viewGroup) {
//
//        ViewHolder viewHolder = null;
//        PDBean pdBean = pdBeanList.get(i);
//        boolean istoogleon = booleanBean.istoogleon;
//        Log.i(TAG,"isToggleOn-"+ istoogleon);
//        if (convertView == null) {
//            convertView = View.inflate(context, R.layout.check_item, null);
//            viewHolder = new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        viewHolder.position = i;    // 刷新viewHolder的位置，不要少了这一行！要不然listview里面的数据会错乱
//
//        viewHolder.barcode.setText();
//        viewHolder.gname.setText();
//
//        viewHolder.etMartNum.setText("".equals(martNum)?"":martNum);
//        viewHolder.etStorkNum.setText("".equals(storeNum)?"":storeNum);
//        viewHolder.ivDelete.setOnClickListener(view -> onitemClickCallBack.onItemClick(i));
//        if (istoogleon){
//            viewHolder.etMartNum.setEnabled(true);
//            viewHolder.etStorkNum.setEnabled(false);
//            if (i==0){
//                viewHolder.etMartNum.requestFocus();
//            }
//            viewHolder.etMartNum.setBackgroundResource(R.color.colorGary);
//            viewHolder.etStorkNum.setBackgroundResource(R.color.colorBlackBg);
//        }else{
//            viewHolder.etMartNum.setEnabled(false);
//            viewHolder.etStorkNum.setEnabled(true);
//            if (i==0){
//                viewHolder.etStorkNum.requestFocus();
//            }
//            viewHolder.etStorkNum.setBackgroundResource(R.color.colorGary);
//            viewHolder.etMartNum.setBackgroundResource(R.color.colorBlackBg);
//        }
//
//
//        viewHolder.etStorkNum.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                mCurrentTouchedIndex = i;
//            }
//            return false;
//        });
//        viewHolder.etStorkNum.clearFocus();
//        if (i == mCurrentTouchedIndex) {
//            // 如果该项中的EditText是要获取焦点的
//            viewHolder.etStorkNum.requestFocus();
//        }
//        viewHolder.etMartNum.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                mCurrentTouchedIndex = i;
//            }
//            return false;
//        });
//        viewHolder.etMartNum.clearFocus();
//        if (i == mCurrentTouchedIndex) {
//            // 如果该项中的EditText是要获取焦点的
//            viewHolder.etMartNum.requestFocus();
//        }
//        return convertView;
//    }
    class ViewHolder {
        TextView barcode;
        TextView gname;
        EditText etMartStock;
        EditText etStoreStock;
        EditText etMartNum;
        EditText etStorkNum;
        ImageView ivDelete;

        int position;

        public ViewHolder(View itemView) {
            barcode = (TextView) itemView.findViewById(R.id.tv_barcode_num);
            gname = (TextView) itemView.findViewById(R.id.tv_name);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            //卖场库存
            etMartStock = (EditText) itemView.findViewById(R.id.et_mart_stock);
            etStoreStock = (EditText) itemView.findViewById(R.id.et_store_stock);
            //卖场数量，仓库数量
            etMartNum = (EditText) itemView.findViewById(R.id.et_mart_num);
            etStorkNum = (EditText) itemView.findViewById(R.id.et_stork_num);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);


        }
    }
}

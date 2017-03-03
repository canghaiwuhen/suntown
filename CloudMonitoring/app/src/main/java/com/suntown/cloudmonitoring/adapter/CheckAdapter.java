package com.suntown.cloudmonitoring.adapter;

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

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.BooleanBean;
import com.suntown.cloudmonitoring.bean.PDBean;

import java.util.List;


/**
 * Created by Administrator on 2016/11/1.
 */
public class CheckAdapter extends BaseAdapter{
    private static final String TAG = "CheckAdapter";
    Context context;
    List<PDBean> pdBeanList;
    BooleanBean booleanBean;
    private int mCurrentTouchedIndex = -1;

    public CheckAdapter(Context context, List<PDBean> pdBeanList, BooleanBean booleanBean) {
        this.context = context;
        this.pdBeanList = pdBeanList;
        this.booleanBean = booleanBean;
    }

    public interface OnItemClickCallBack {
        void onItemClick(int position);//告诉外界条目单击
    }
    private OnItemClickCallBack onitemClickCallBack;

    public void SetOnItemClickCallBack(OnItemClickCallBack onitemClickCallBack){
        this.onitemClickCallBack = onitemClickCallBack;
    }


    @Override
    public int getCount() {
        return pdBeanList.size()==0?0:pdBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return pdBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        PDBean pdBean = pdBeanList.get(i);
        boolean istoogleon = booleanBean.istoogleon;
        Log.i(TAG,"isToggleOn-"+ istoogleon);
        if (convertView == null) {
            convertView = View.inflate(context,R.layout.check_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = i;    // 刷新viewHolder的位置，不要少了这一行！要不然listview里面的数据会错乱

        viewHolder.barcode.setText(pdBean.BARCODE);
        viewHolder.gname.setText(pdBean.GOODSNAME);
        String martNum = pdBean.martNum;
        String storeNum = pdBean.storeNum;
        viewHolder.etMartNum.setText("".equals(martNum)?"":martNum);
        viewHolder.etStorkNum.setText("".equals(storeNum)?"":storeNum);
        viewHolder.ivDelete.setOnClickListener(view -> onitemClickCallBack.onItemClick(i));
        if (istoogleon){
            viewHolder.etMartNum.setEnabled(true);
            viewHolder.etStorkNum.setEnabled(false);
            viewHolder.etMartNum.setBackgroundResource(R.color.colorGary);
            viewHolder.etStorkNum.setBackgroundResource(R.color.colorBlackBg);
        }else{
            viewHolder.etMartNum.setEnabled(false);
            viewHolder.etStorkNum.setEnabled(true);
            viewHolder.etStorkNum.setBackgroundResource(R.color.colorGary);
            viewHolder.etMartNum.setBackgroundResource(R.color.colorBlackBg);
        }

        viewHolder.etStorkNum.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mCurrentTouchedIndex = i;
            }
            return false;
        });
        viewHolder.etStorkNum.clearFocus();
        if (i == mCurrentTouchedIndex) {
            // 如果该项中的EditText是要获取焦点的
            viewHolder.etStorkNum.requestFocus();
        }
        viewHolder.etMartNum.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mCurrentTouchedIndex = i;
            }
            return false;
        });
        viewHolder.etMartNum.clearFocus();
        if (i == mCurrentTouchedIndex) {
            // 如果该项中的EditText是要获取焦点的
            viewHolder.etMartNum.requestFocus();
        }

        return convertView;
    }
    class ViewHolder {
        TextView barcode;
        TextView gname;
        TextView etMartStock;
        TextView etStoreStock;
        EditText etMartNum;
        EditText etStorkNum;
        ImageView ivDelete;

        int position;

        public ViewHolder(View itemView) {
            barcode = (TextView) itemView.findViewById(R.id.tv_barcode_num);
            gname = (TextView) itemView.findViewById(R.id.tv_name);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            //卖场库存
            etMartStock = (TextView) itemView.findViewById(R.id.et_mart_stock);
            etStoreStock = (TextView) itemView.findViewById(R.id.et_store_stock);
            //卖场数量，仓库数量
            etMartNum = (EditText) itemView.findViewById(R.id.et_mart_num);
            etStorkNum = (EditText) itemView.findViewById(R.id.et_stork_num);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            if (0<pdBeanList.size()) {
                etMartNum.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (0!=pdBeanList.size()) {
                            PDBean pdBean = pdBeanList.get(position);
                            pdBean.martNum= charSequence.toString();
                            Log.i("MainActivity", "onTextChanged: " + position + " boxNum - " +  pdBean.martNum);
                        }


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
                        if (0!=pdBeanList.size()) {
                            PDBean pdBean = pdBeanList.get(position);
                            pdBean.storeNum = charSequence.toString();
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

        }
    }
}

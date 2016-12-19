package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.inputBean;

import java.util.List;


/**
 * Created by Administrator on 2016/11/1.
 */
public class NoteAdapter extends BaseAdapter{
    List<inputBean> inputBeanList;
    Context context;

    public NoteAdapter(Context context, List<inputBean> inputBeanList) {
        this.context=context;
        this.inputBeanList=inputBeanList;
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
        return inputBeanList.size()==0?0:inputBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return inputBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

     int mCurrentTouchedIndex = -1;

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        inputBean inputBean = inputBeanList.get(i);

        if (convertView == null) {
            convertView = View.inflate(context,R.layout.input_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = i;    // 刷新viewHolder的位置，不要少了这一行！要不然listview里面的数据会错乱

        viewHolder.barcode.setText(inputBean.Barcode);
        viewHolder.gname.setText(inputBean.Gname);
        viewHolder.num.setText(inputBean.num + "");
        viewHolder.boxNum.setText(inputBean.boxNum + "");

        viewHolder.date.setText(inputBean.Date);
        viewHolder.boxNum.setTag(i);
        viewHolder.ivDelete.setOnClickListener(view -> onitemClickCallBack.onItemClick(i));
        viewHolder.boxNum.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mCurrentTouchedIndex = i;
            }
            return false;
        });
        viewHolder.boxNum.clearFocus();
        if (i == mCurrentTouchedIndex) {
            // 如果该项中的EditText是要获取焦点的
            viewHolder.boxNum.requestFocus();
        }
        viewHolder.num.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mCurrentTouchedIndex = i;
            }
            return false;
        });
        viewHolder.num.clearFocus();
        if (i == mCurrentTouchedIndex) {
            // 如果该项中的EditText是要获取焦点的
            viewHolder.num.requestFocus();
        }
        return convertView;
    }
    class ViewHolder {
        TextView barcode;
        TextView gname;
        EditText boxNum;
        EditText num;
        TextView date;
        ImageView ivDelete;

        int position;

        public ViewHolder(View itemView) {
            barcode = (TextView) itemView.findViewById(R.id.tv_barcode_num);
            gname = (TextView) itemView.findViewById(R.id.tv_name);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            boxNum = (EditText) itemView.findViewById(R.id.et_box);
            num = (EditText) itemView.findViewById(R.id.et_num);
            date = (TextView) itemView.findViewById(R.id.et_date);

            boxNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    inputBean bean = inputBeanList.get(position);
                    bean.boxNum= charSequence.toString();

                    Log.i("MainActivity", "onTextChanged: " + position + " boxNum - " + bean.boxNum);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    inputBean bean = inputBeanList.get(position);
                    bean.num = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }
}

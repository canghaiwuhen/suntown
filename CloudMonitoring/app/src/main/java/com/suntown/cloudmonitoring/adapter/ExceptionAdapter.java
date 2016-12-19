package com.suntown.cloudmonitoring.adapter;

import android.util.Log;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.ExceptionLevel0Item;
import com.suntown.cloudmonitoring.bean.ExceptionLevel1Item;

import java.util.List;

/**
 * 可展开的recyclerViewHelper
 */
public class ExceptionAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity> {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    public ExceptionAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.ap_title_item);
        addItemType(TYPE_LEVEL_1, R.layout.ap_items);
    }

    @Override
    protected void convert(BaseViewHolder holder,MultiItemEntity item) {
        switch (holder.getItemViewType()){
            case TYPE_LEVEL_0:
                ExceptionLevel0Item lv0 = (ExceptionLevel0Item) item;
                Log.i("test","lv1:"+lv0.sname+","+lv0.sid);
                holder.setText(R.id.tv_store_name,lv0.sname).setText(R.id.tv_store_num,lv0.sid);
                holder.itemView.setOnClickListener(view -> {
                    int pos = holder.getAdapterPosition();
                    Log.d("test", "Level 0 item pos: " + pos);
                    holder.setImageResource(R.id.iv_arrow,R.drawable.arrow_r);
//                    if (lv0.isExpanded()) {
//                        collapse(pos);
//                        holder.setImageResource(R.id.iv_arrow,R.drawable.arrow_r);
//                    } else {
//                        expand(pos);
//                        holder.setImageResource(R.id.iv_arrow,R.drawable.arrow_d);
//                    }
                });
                break;
            case TYPE_LEVEL_1:
                final ExceptionLevel1Item lv1 = (ExceptionLevel1Item)item;
                Log.i("test","lv1:"+lv1.APIP+","+lv1.APADDR+","+lv1.EDCOUNT+","+lv1.LASTDATE);
                holder.setText(R.id.tv_apid,lv1.APIP).
                        setText(R.id.tv_apaddr,lv1.APADDR).
                        setText(R.id.tv_num, lv1.EDCOUNT+"").
                        setText(R.id.tv_lastTime,lv1.LASTDATE);

                if (lv1.STATUS==1){
                   holder.setVisible(R.id.iv_alarm,true);
                }else{
                   holder.setVisible(R.id.iv_alarm,false);
                }
                break;
        }
    }
}

package com.suntown.smartscreen.shopCenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntown.smartscreen.R;
import com.suntown.smartscreen.data.AllTaskBean;
import com.suntown.smartscreen.data.DmListBean;
import com.suntown.smartscreen.data.ShopBoardBean;
import com.suntown.smartscreen.shopCenter.detial.ExpandableItemAdapter;
import com.suntown.smartscreen.utils.BitmapUtils;

import java.util.HashMap;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Administrator on 2017/2/16.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {
    Context context;
    List<AllTaskBean.RECORDBean> list;
    HashMap<String, List<DmListBean.RECORDBean>> listHashMap;
    private boolean isCheched;

    public ExpandableAdapter(Context context, List<AllTaskBean.RECORDBean> list, HashMap<String, List<DmListBean.RECORDBean>> listHashMap) {
        this.context=context;
        this.list=list;
        this.listHashMap=listHashMap;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        List<DmListBean.RECORDBean> recordBeen = listHashMap.get(list.get(i).TYPENAME);
        return 0==recordBeen.size()?0:recordBeen.size();
    }

    @Override
    public Object getGroup(int i) {
        return list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        List<DmListBean.RECORDBean> recordBeen = listHashMap.get(list.get(i).TYPENAME);
        return recordBeen.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public View getGroupView(int goupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        if (convertView==null){
            convertView =  View.inflate(context, R.layout.board_item, null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvCount = (TextView) convertView.findViewById(R.id.tv_count);
        ImageView ivArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
        LinearLayout llItem = (LinearLayout) convertView.findViewById(R.id.ll_item);
        if (!isExpanded) {
            ivArrow.setImageResource(R.drawable.arrow_r);
        } else {
            ivArrow.setImageResource(R.drawable.arrow_d);
        }
        AllTaskBean.RECORDBean recordBean = list.get(goupPosition);
        String typename = recordBean.TYPENAME;
        int size = listHashMap.get(typename).size();
        tvName.setText(typename);
        tvCount.setText(size==0?"":size+"");
//        tvName.setOnClickListener(view -> onAdapterCallBack.onOpenClick(goupPosition,isExpanded));
        return convertView;
    }

    boolean isOpen;
    @Override
    public View getChildView(int goupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView==null){
            convertView =  View.inflate(context, R.layout.alloca_item, null);
        }
        isOpen = false;
        DmListBean.RECORDBean recordBean = listHashMap.get(list.get(goupPosition).TYPENAME).get(childPosition);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        TextView tvType = (TextView) convertView.findViewById(R.id.tv_type);
        TextView tvSize = (TextView) convertView.findViewById(R.id.tv_size);
        RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.rb);
        ImageView ivArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
        RelativeLayout rlClick = (RelativeLayout) convertView.findViewById(R.id.rl_click);
        LinearLayout llContent = (LinearLayout) convertView.findViewById(R.id.ll_content);
        String dmimg = recordBean.DMIMG;
        if (!"".equals(dmimg)) {
            Bitmap bitmap = BitmapUtils.stringtoBitmap(dmimg);
            ivPhoto.setImageBitmap(bitmap);
        }
        ivArrow.setImageResource(R.drawable.arrow_d);
        tvTitle.setText(recordBean.DMNAME);
        tvType.setText(list.get(0).TYPENAME);
        tvSize.setText(recordBean.SPECNAME);
        rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    //关闭
                    llContent.setVisibility(View.GONE);
                    ivArrow.setImageResource(R.drawable.arrow_d);
                    isOpen = false;
                }else{
                    isOpen = true;
                    llContent.setVisibility(View.VISIBLE);
                    ivArrow.setImageResource(R.drawable.arrow_u);
                }
            }
        });
        isCheched = recordBean.ISCHECHED;
        radioButton.setChecked(isCheched);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCheched =!isCheched;
                recordBean.ISCHECHED = isCheched;
                radioButton.setChecked(isCheched);
//                onAdapterCallBack.onCheckedClick(goupPosition,childPosition);
            }
        });
        return convertView;
    }
    public interface OnAdapterCallBack {
//        void onSettingClick(int parentPosition,int childPosition);
//        void onCheckedClick(int parentPosition,int childPosition);
//        void onOpenClick(int parentPosition,boolean isExpanded);
    }

    private OnAdapterCallBack onAdapterCallBack;

    public void setOnAdapterCallBack(OnAdapterCallBack onAdapterCallBack) {
        this.onAdapterCallBack = onAdapterCallBack;
    }

}

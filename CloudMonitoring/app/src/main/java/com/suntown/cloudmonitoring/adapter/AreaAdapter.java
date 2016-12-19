package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.SortModel;
import com.suntown.cloudmonitoring.utils.CharacterParser;

import java.util.List;


/**
 * Created by Administrator on 2016/9/20.
 */
public class AreaAdapter extends BaseAdapter implements SectionIndexer {
    private final CharacterParser characterParser;
    List<SortModel> arealist;
    Context context;
//    List<ShopInfo> shopInfoList;
    public AreaAdapter(Context context, List<SortModel> arealist) {
        this.context=context;
        this.arealist=arealist;
//        this.shopInfoList=shopInfoList;
        characterParser = CharacterParser.getInstance();
    }

    @Override
    public int getCount() {
        return arealist.size()==0?0:arealist.size();
    }

    @Override
    public Object getItem(int position) {
        return arealist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = View.inflate(context, R.layout.text_items, null);
        }
        TextView tvText = ViewHolder.get(convertView, R.id.tv_text);
        TextView tvLetter=ViewHolder.get(convertView,R.id.catalog);
        String name = arealist.get(position).name;
        tvText.setText(name);
        int section = getSectionForPosition(position);
        //根据position获取分类的首字母的Char ascii值
//            int section = characterParser.convert(arealist.get(position)).charAt(0);
//            int section = PinYinUtils.toPinYin(arealist.get(position)).charAt(0);
            if(position == getPositionForSection(section)){
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText(arealist.get(position).sortLetters);
            }else{
                tvLetter.setVisibility(View.GONE);
            }

        return convertView;
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
//                sortStr = characterParser.convert(arealist.get(i));
//                sortStr = PinYinUtils.toPinYin(arealist.get(i));
            String sortLetters = arealist.get(i).sortLetters;
            char firstChar = sortLetters.toUpperCase().charAt(0);
                if (firstChar == section) {
                     return i;
                }
        }

        return -1;
    }



    @Override
    public Object[] getSections() {
        return null;
    }
    //根据ListView的当前位置获取分类的首字母的Char ascii值
    @Override
    public int getSectionForPosition(int position) {
//            return  PinYinUtils.toPinYin(arealist.get(position)).charAt(0);
        return arealist.get(position).sortLetters.charAt(0);
    }

}

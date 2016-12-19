package com.suntown.cloudmonitoring.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.QueryActivity;
import com.suntown.cloudmonitoring.bean.Person;
import com.suntown.cloudmonitoring.bean.ShopXmlBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */
public class OldListAdapter extends BaseAdapter{
    private Context context;
    private List<Person> oldList;
    public OldListAdapter(Context context, List<Person> oldList) {
        this.context =context;
        this.oldList =oldList;
    }

    @Override
    public int getCount() {
        return oldList.size()==0?0:oldList.size();
    }

    @Override
    public Object getItem(int position) {
        return oldList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        Person person = oldList.get(position);
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.old_list_items,null);
            viewHolder.TvShopName = (TextView)convertView.findViewById(R.id.tv_shop_name);
            viewHolder.TvIp= (TextView) convertView.findViewById(R.id.tv_ip);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.TvShopName.setText(person.name+"( ID:"+person.sid+")");
        viewHolder.TvIp.setText(person.ip);
            return convertView;
    }
    class ViewHolder {
        TextView TvShopName;
        TextView TvIp;

    }
}

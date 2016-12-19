package com.suntown.cloudmonitoring.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.APDetialActivity;
import com.suntown.cloudmonitoring.activity.ApMonitoring;
import com.suntown.cloudmonitoring.activity.RegisterMrnitoring;
import com.suntown.cloudmonitoring.activity.TagDetialActivity;
import com.suntown.cloudmonitoring.adapter.ExceptionExpandAdapter;
import com.suntown.cloudmonitoring.bean.APInfoBean;
import com.suntown.cloudmonitoring.bean.Item0;
import com.suntown.cloudmonitoring.bean.RegisterMor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ShowAllFragment extends Fragment{
    private View inflate;
    private ExceptionExpandAdapter adapter;
    private Map<String, List<APInfoBean.RECORDBean>> listMap;
    private ArrayList<Item0> items;
    private ExpandableListView elv;
    private List<APInfoBean.RECORDBean> allList;
    private LinearLayout llNormal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_show_all, container, false);
        elv = (ExpandableListView)inflate.findViewById(R.id.rv);
        llNormal = (LinearLayout) inflate.findViewById(R.id.ll_normal);
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        initUi();
    }

    private void initUi() {
        allList = ((ApMonitoring) getActivity()).allList;
        Log.i("record","record:"+allList);
        if (0!=allList.size()) {
            llNormal.setVisibility(View.GONE);
        }
        Collections.sort(allList, (o1, o2) -> {
            String sid1 = o1.SID;
            String sid2 = o2.SID;
            int i1 = Integer.parseInt(sid1);
            int i2 = Integer.parseInt(sid2);
            if (i1 >i2){
                return 1;
            }
            return -1;
        });
        refreshStatisticsData();
    }

    private void refreshStatisticsData() {
            listMap = new HashMap<>();
            List<APInfoBean.RECORDBean> beanList;
            items = new ArrayList<>();
            for (APInfoBean.RECORDBean recordBean : allList) {
                String sname = recordBean.SNAME;
                String sid = recordBean.SID;
                if (listMap.containsKey(sid)){
                    beanList = listMap.get(sid);
                    beanList.add(recordBean);
                }else{
                    items.add(new Item0(sname,sid));
                    beanList = new ArrayList<>();
                    beanList.add(recordBean);
                }
                listMap.put(sid,beanList);
            }
        Log.i("fragment","map: "+listMap.toString());
        adapter = new ExceptionExpandAdapter(getActivity(),items,listMap);
        elv.setAdapter(adapter);
        adapter.setOnWaitFlagClickListener(position -> {
            if (elv.isGroupExpanded(position)) {  //如果是打开状态则关闭
                elv.collapseGroup(position);
            } else { //如果是关闭状态则打开
                elv.expandGroup(position);
            }
        });
        elv.setOnChildClickListener((expandableListView, view, i, i1, l) -> {
            String sid = items.get(i).sid;
            List<APInfoBean.RECORDBean> recordBeanList = listMap.get(sid);
            APInfoBean.RECORDBean recordBean = recordBeanList.get(i1);
            Intent intent = new Intent(getActivity(),APDetialActivity.class);
            intent.putExtra("RECORD_BEAN",recordBean);
            startActivity(intent);
            return true;
        });
    }

}

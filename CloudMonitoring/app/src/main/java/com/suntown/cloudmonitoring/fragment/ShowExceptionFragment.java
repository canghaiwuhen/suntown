package com.suntown.cloudmonitoring.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.APDetialActivity;
import com.suntown.cloudmonitoring.activity.ApMonitoring;
import com.suntown.cloudmonitoring.adapter.ExceptionExpandAdapter;
import com.suntown.cloudmonitoring.bean.APInfoBean;
import com.suntown.cloudmonitoring.bean.Item0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ShowExceptionFragment extends Fragment{
    private static final String TAG = "ShowExceptionFragment";
    private View inflate;
    private ExpandableListView mRecyclerView;
    private List<APInfoBean.RECORDBean> exceptionList;
    private ExceptionExpandAdapter adapter;
    private Map<String, List<APInfoBean.RECORDBean>> listMap;
    private List<APInfoBean.RECORDBean> beanList;
    private ArrayList<Item0> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_show_exception, container, false);
        mRecyclerView = (ExpandableListView)inflate.findViewById(R.id.rv);
//        mRecyclerView.setOnChildClickListener();
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        initUi();
    }

    private void initUi() {
        exceptionList = ((ApMonitoring) getActivity()).exceptionList;
        Log.i("fragment","exceptionList:"+exceptionList.toString());
        initData();
    }

    private void initData() {
        listMap = new HashMap<>();
        items = new ArrayList<>();
        for (APInfoBean.RECORDBean recordBean : exceptionList) {
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
        Log.i("fragment",items.toString());
        adapter = new ExceptionExpandAdapter(getActivity(),items,listMap);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnWaitFlagClickListener(position -> {
            Log.i("fragment","点击了");
            if (mRecyclerView.isGroupExpanded(position)) {  //如果是打开状态则关闭
                mRecyclerView.collapseGroup(position);
            } else { //如果是关闭状态则打开
                mRecyclerView.expandGroup(position);
            }
        });
        mRecyclerView.setOnChildClickListener((expandableListView, view, i, i1, l) -> {
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

package com.suntown.cloudmonitoring.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.ApPhote.ShopPhotoAct;
import com.suntown.cloudmonitoring.activity.ApPhote.photoDetialAct;
import com.suntown.cloudmonitoring.adapter.CartItemAdapter;
import com.suntown.cloudmonitoring.bean.APInfoBean;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/21.
 */

public class BaseFragment extends Fragment {
    private View inflate;
    private ListView lvList;
    private List<APInfoBean.RECORDBean> recordList;
    private Map<String, List<APInfoBean.RECORDBean>> listMap;
    private List<String> stringList;
    private CartItemAdapter adapter ;
    private int intExtra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.base_layout, container, false);
        lvList = (ListView) inflate.findViewById(R.id.lv_list);
        recordList = ((photoDetialAct) getActivity()).recordList;
        intExtra = ((photoDetialAct) getActivity()).intExtra;
        initView();
        lvList.setAdapter(adapter);
        lvList.setOnItemClickListener((adapterView, view, i, l) -> {
            String s = stringList.get(i);
            List<APInfoBean.RECORDBean> recordBeanList = listMap.get(s);
            Intent intent = new Intent(getActivity(), ShopPhotoAct.class);
            intent.putParcelableArrayListExtra(Constant.RECORD, (ArrayList<? extends Parcelable>) recordBeanList);
            Log.i("BaseFragment","recordBeanList:"+recordBeanList.toString());
            intent.putExtra(Constant.NUM,intExtra);
            startActivity(intent);
        });
        return inflate;
    }

    private void initView() {
        recordList = ((photoDetialAct) getActivity()).recordList;
        listMap = new HashMap<>();
        List<APInfoBean.RECORDBean>  arrayList;
        stringList = new ArrayList<>();
        for (APInfoBean.RECORDBean recordBean : recordList) {
            String aname = recordBean.ANAME;
            if (listMap.containsKey(aname)){
                arrayList = listMap.get(aname);
                arrayList.add(recordBean);
            }else{
                stringList.add(aname);
                arrayList = new ArrayList<>();
                arrayList.add(recordBean);
            }
            listMap.put(aname,arrayList);
        }
        adapter = new CartItemAdapter(getActivity(),stringList,listMap,intExtra);

    }

}

package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.adapter.AreaAdapter;
import com.suntown.cloudmonitoring.adapter.ItemAdapter;
import com.suntown.cloudmonitoring.adapter.MyAdapter;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.FiltrateBean;
import com.suntown.cloudmonitoring.bean.Item;
import com.suntown.cloudmonitoring.bean.SortModel;
import com.suntown.cloudmonitoring.utils.CharacterParser;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.weight.FlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.zhikaizhang.indexview.PinyinComparator;

public class FiltrateRegisterAct extends Activity {

    private static final String TAG = "FiltrateRegisterAct";
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.fl_title)
    FlowLayout flTitle;
    @BindView(R.id.lv_fast)
    ListView lvFast;
    private List<String> data = new ArrayList<>();
//    private CharacterParser characterParser;
    private List<FiltrateBean> record;
    private List<Item> sortModels1 = new ArrayList<>();
    private List<Item> sortModels = new ArrayList<>();
    private ArrayList<Item> host;
    private MyAdapter areaAdapter;
    private Map<String, List<String>> listMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrate_register);
        ButterKnife.bind(this);
//        characterParser = CharacterParser.getInstance();
        record = getIntent().getParcelableArrayListExtra("record");
        Log.i("record", "record:" + record.toString());
        initUi();
    }

    private void initUi() {
        List<String> shopInfoList = null;
        host = new ArrayList<>();
        listMap = new HashMap<>();
        for (FiltrateBean recordBean : record) {
            String aname = recordBean.ANAME;
            String sname = recordBean.SNAME;
            Log.i("initData", "aname" + aname + "   sname" + sname);
            if (listMap.containsKey(aname)) {
                shopInfoList = listMap.get(aname);
                if (!shopInfoList.contains(sname)) {
                    shopInfoList.add(sname);
                }
                listMap.put(aname, shopInfoList);
            } else {
                host.add(new Item(aname));
                shopInfoList = new ArrayList<>();
                shopInfoList.add(sname);
                listMap.put(aname, shopInfoList);
            }
        }
        Log.i("initData", "host:" + host.toString() + "  listMap:" + listMap.toString());
        sortModels.addAll(host);
        Collections.sort(sortModels, new PinyinComparator<Item>() {
            @Override
            public int compare(Item s1, Item s2) {
                return compare(s1.userName, s2.userName);
            }
        });
        areaAdapter = new MyAdapter(this, sortModels);
        lvFast.setAdapter(areaAdapter);
        lvFast.setOnItemClickListener((parent, view, position, id) -> {
            lvFast.setVisibility(View.VISIBLE);
            Item item = (Item) areaAdapter.getItem(position);
            String name = item.userName;
            //标题栏的改变
            data.add(name);
            Log.i(TAG,"data-->"+data);
            addView(data);
            List<String> shopInfos = listMap.get(name);
            if (shopInfos == null) {
                lvFast.setVisibility(View.GONE);
            } else {
                sortModels1.clear();
                sortModels.clear();
                for (String shopInfo : shopInfos) {
                    sortModels1.add(new Item(shopInfo));
                }
                Collections.sort(sortModels1, new PinyinComparator<Item>() {
                    @Override
                    public int compare(Item s1, Item s2) {
                        return compare(s1.userName, s2.userName);
                    }
                });
                sortModels.addAll(sortModels1);
            }
            areaAdapter.notifyDataSetChanged();
        });
    }
    public void addView(List<String> data){
        flTitle.removeAllViews();
        for (String tag : data) {
//            View view = Utils.createTagView(this, tag);
//            flTitle.addView(view);
            int ranHeight = dip2px(this, 30);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ranHeight);
            lp.setMargins(dip2px(this, 10), 0, dip2px(this, 10), 0);
            TextView tv = new TextView(this);
            tv.setPadding(dip2px(this, 10), 0, dip2px(this, 10), 0);
            tv.setTextColor(Color.parseColor("#FEB73A"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//            tv.setText(tag);
            tv.setText(tag+"  ✕");
            tv.setGravity(Gravity.CENTER_VERTICAL);
//            Drawable drawable= getResources().getDrawable(R.drawable.delete);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            tv.setCompoundDrawables(null,null,drawable,null);
//            tv.setCompoundDrawablePadding(5);
//            tv.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
            tv.setLines(1);
            tv.setBackgroundResource(R.drawable.item_bg);
            flTitle.addView(tv, lp);
            tv.setOnClickListener(OnTagTouchListener);
        }
    }

    @OnClick({R.id.iv_back, R.id.button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.button:
                //TODO 带回数据显示
                String text="";
                int size = data.size();
                if (size==0||size!=2){
                    Utils.showToast(FiltrateRegisterAct.this,"请选择门店");
                   return;
                }
                text = data.get(size - 1);
//                Utils.showToast(FiltrateRegisterAct.this,text);
                Intent intent = new Intent();
                intent.putExtra(Constant.STRING_DATA,text);
                setResult(300, intent);
                finish();
                break;
        }
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private View.OnClickListener OnTagTouchListener = view -> {
        String name = ((TextView) view).getText().toString().replace("  ✕","");
//        Utils.showToast(this,name);
        int i = data.indexOf(name);
        if (i==data.size()-1) {
            lvFast.setVisibility(View.VISIBLE);
            data.remove(i);
            sortModels.clear();
            addView(data);
            if (data.size()==0){
                sortModels.addAll(host);
            }else{
                String aname = data.get(data.size() - 1);
                List<String> list = listMap.get(aname);
                for (String s : list) {
                    Item item = new Item(s);
                    sortModels.add(item);
                }
            }
            Collections.sort(sortModels, new PinyinComparator<Item>() {
                @Override
                public int compare(Item s1, Item s2) {
                    return compare(s1.userName, s2.userName);
                }
            });
            areaAdapter.notifyDataSetChanged();
        }
    };


}

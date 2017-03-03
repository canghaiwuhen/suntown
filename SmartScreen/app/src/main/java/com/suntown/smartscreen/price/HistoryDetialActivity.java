package com.suntown.smartscreen.price;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.suntown.smartscreen.R;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.base.BaseApplication;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.Utils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class HistoryDetialActivity extends BaseActivity {

    private DbManager db;
    private ArrayList<Person> personList;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_goods)
    RecyclerView rlGoods;

    @Override
    protected int getContentView() {
        return R.layout.activity_history_detial;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
        personList = intent.getParcelableArrayListExtra(Constant.PERSON);
        String name = intent.getStringExtra(Constant.SHOP_NAME);
        tvTitle.setText(name);
        rlGoods.setHasFixedSize(true);
        rlGoods.setLayoutManager(new LinearLayoutManager(this));
        OldGoodsAdapter oldGoodsAdapter = new OldGoodsAdapter(R.layout.query_item, personList);
        rlGoods.setAdapter(oldGoodsAdapter);
        oldGoodsAdapter.setOnSwipeAdapterCallBack(new OldGoodsAdapter.OnSwipeAdapterCallBack() {
            @Override
            public void deleteItemClick(int position) {
                Person person = personList.get(position);
                int id = person.id;
                String tinyip = person.TINYIP;
                personList.remove(position);
                oldGoodsAdapter.notifyDataSetChanged();
                try {
                    db.deleteById(Person.class, id);
                    Utils.showToast(HistoryDetialActivity.this,"删除成功");
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}

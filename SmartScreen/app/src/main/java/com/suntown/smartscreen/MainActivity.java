package com.suntown.smartscreen;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.login.LoginActivity;
import com.suntown.smartscreen.maintain.MainTainActivity;
import com.suntown.smartscreen.price.UpdatePriceActivity;
import com.suntown.smartscreen.server.ChooseServerActvity;
import com.suntown.smartscreen.shopCenter.ShopCenterActivity;
import com.suntown.smartscreen.spaceShelf.SpaceShelfActivity;
import com.suntown.smartscreen.updatePsw.UpdatePswActivity;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.tv_server)
    TextView tvServer;
    @BindView(R.id.right_nickname)
    TextView rightNickname;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    @BindView(R.id.right)
    LinearLayout right;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        rightNickname.setText(SPUtils.getString(this, Constant.USER_NAME));
        RxPermissions.getInstance(this).request( Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, Manifest.permission.VIBRATE).subscribe(granted -> {
            if (!granted) {
                //不同意，给提示
                Toast.makeText(MainActivity.this, "请同意软件的权限，才能继续提供服务", Toast.LENGTH_LONG).show();
                System.exit(0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String modname = SPUtils.getString(this, Constant.MODNAME);
        tvServer.setText("".equals(modname)?"选择服务":modname);
    }

    @OnClick({R.id.iv_user, R.id.ll_choose_server, R.id.rl_price, R.id.rl_form_board, R.id.rl_store, R.id.rl_goods_shelf, R.id.ll_update_psw, R.id.ll_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击弹出右侧
            case R.id.iv_user:
                drawerlayout.openDrawer(right);
                break;
            //TODO 开启选择服务 Activity
            case R.id.ll_choose_server:
                startActivity(new Intent(MainActivity.this, ChooseServerActvity.class));
                break;
            //TODO 价格策略
            case R.id.rl_price:
                startActivity(new Intent(MainActivity.this,UpdatePriceActivity.class));
                break;
            //TODO 模板维护
            case R.id.rl_form_board:
                startActivity(new Intent(MainActivity.this,MainTainActivity.class));
                break;
            //TODO 门店管理
            case R.id.rl_store:
                startActivity(new Intent(MainActivity.this,ShopCenterActivity.class));
                break;
            //TODO 空间货位
            case R.id.rl_goods_shelf:
                startActivity(new Intent(MainActivity.this,SpaceShelfActivity.class));
                break;
            //TODO 修改密码
            case R.id.ll_update_psw:
                startActivity(new Intent(MainActivity.this, UpdatePswActivity.class));
                break;
            //退出登录
            case R.id.ll_exit:
                SPUtils.put(this, Constant.USER_NAME, "");
                SPUtils.put(this, Constant.PASS_WORD, "");
                SPUtils.put(this, Constant.USER_ID,"");
                //服务
                SPUtils.put(this,Constant.MODID,"");
                SPUtils.put(this,Constant.MODURL,"");
                SPUtils.put(this,Constant.MODNAME,"");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

}

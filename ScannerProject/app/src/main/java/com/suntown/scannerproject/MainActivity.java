package com.suntown.scannerproject;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suntown.scannerproject.act.ChooseShopActivity;
import com.suntown.scannerproject.act.DisplayDActivity;
import com.suntown.scannerproject.act.LoginActivity;
import com.suntown.scannerproject.act.NewProductsAct;
import com.suntown.scannerproject.act.ScannerActivity;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.bean.Item1;
import com.suntown.scannerproject.change.ChangeTAGAct;
import com.suntown.scannerproject.input.InputAndOutputActivity;
import com.suntown.scannerproject.query.QueryActivity;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.Utils;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.iv_user)
    TextView ivUser;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.right_nickname)
    TextView rightNickname;
    @BindView(R.id.ll_exit)
    LinearLayout llExit;
    @BindView(R.id.right)
    LinearLayout right;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    @BindView(R.id.tv_change_shop)
    TextView tvChangeShop;
    private String sid;
    private String sname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RxPermissions.getInstance(this).request(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
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
        String userName = SPUtils.getString(this, Constant.USER_CODE);
        sname = SPUtils.getString(this, Constant.SHOP_NAME);
        sid = SPUtils.getString(this, Constant.SID);
        Log.i(TAG, "sid:" + sid + ":userName:" + userName + ",sname:" + sname);
        rightNickname.setText("".equals(userName) ? "用户名" : userName);
        tvChangeShop.setText("".equals(sname) ? "门店" : sname);
    }

    @OnClick({R.id.iv_user, R.id.tv_change_shop, R.id.iv_arrow, R.id.rl_scanner,
            R.id.rl_price, R.id.rl_query, R.id.rl_update_goods,R.id.iv_head,
            R.id.rl_service, R.id.rl_input_and_output, R.id.ll_exit})
    public void onClick(View view) {
        Intent intent;
        Item1 item = new Item1(sname, sid);
        switch (view.getId()) {
            //抽屉
            case R.id.iv_user:
//                right.openDrawer
                drawerlayout.openDrawer(right);
                break;
            //切换店铺
            case R.id.tv_change_shop:
            case R.id.iv_arrow:
                if (!Utils.isFastClick()) {
                    startActivity(new Intent(this, ChooseShopActivity.class));
                }
                break;
            //扫描
            case R.id.rl_scanner:
                if ("".equals(sid)) {
                    showDialog();
                } else {
                    intent = new Intent(this, ScannerActivity.class);
                    intent.putExtra(Constant.ITEM, item);
                    startActivity(intent);
                }
                break;
            //更换
            case R.id.rl_price:
                if ("".equals(sid)) {
                    showDialog();
                } else {
                    intent = new Intent(this, ChangeTAGAct.class);
                    intent.putExtra(Constant.ITEM, item);
                    startActivity(intent);
                }
                break;
            //查询
            case R.id.rl_query:
                if ("".equals(sid)) {
                    showDialog();
                } else {
                    startActivity(new Intent(this, QueryActivity.class));
                }
                break;
            //新品上架
            case R.id.rl_update_goods:
//                if ("".equals(sid)) {
//                    showDialog();
//                } else {
//                    intent = new Intent(this, NewProductsAct.class);
//                    intent.putExtra(Constant.ITEM, item);
//                    startActivity(intent);
//                }
                new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("提示")
                        .setContentText("正在持续开发中，敬请期待!")
                        .setCancelText("取消")
                        .setConfirmText("确定")
                        .showCancelButton(true)
                        .setCancelClickListener(sDialog -> sDialog.dismiss()).setConfirmClickListener(sDialog -> sDialog.dismiss())
                        .show();
                break;
            //维护
            case R.id.rl_service:
                if ("".equals(sid)) {
                    showDialog();
                } else {
                    intent = new Intent(this, DisplayDActivity.class);
                    intent.putExtra(Constant.ITEM, item);
                    startActivity(intent);
                }
                break;
            //进销存管理
            case R.id.rl_input_and_output:
                if ("".equals(sid)) {
                    showDialog();
                } else {
                    intent = new Intent(MainActivity.this, InputAndOutputActivity.class);
                    intent.putExtra(Constant.ITEM, item);
                    startActivity(intent);
                }
                break;
            case R.id.ll_exit:
                SPUtils.put(MainActivity.this, Constant.USER_ID, "");
                SPUtils.put(MainActivity.this, Constant.USER_CODE, "");
                SPUtils.put(MainActivity.this, Constant.PASS_WORD, "");
                SPUtils.put(this, Constant.SID, "");
                SPUtils.put(this, Constant.SHOP_NAME, "");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.iv_head:
                Log.i(TAG,"点击了");
                break;
        }

    }

    private void showDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("请选择门店")
//                .setContentText("Won't be able to recover this file!")
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true)
                .setCancelClickListener(sDialog -> sDialog.dismiss()).setConfirmClickListener(sDialog -> sDialog.dismiss())
                .show();
    }

    //一键退出程序
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Utils.showToast(this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

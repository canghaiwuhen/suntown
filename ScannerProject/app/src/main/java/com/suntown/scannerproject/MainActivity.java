package com.suntown.scannerproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.suntown.scannerproject.act.ChooseShopActivity;
import com.suntown.scannerproject.act.DisplayDActivity;
import com.suntown.scannerproject.act.LoginActivity;
import com.suntown.scannerproject.act.ScannerActivity;
import com.suntown.scannerproject.act.SettingPortAct;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.api.ApiService;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.bean.Item1;
import com.suntown.scannerproject.bean.UpdateBean;
import com.suntown.scannerproject.change.ChangeTAGAct;
import com.suntown.scannerproject.input.InputAndOutputActivity;
import com.suntown.scannerproject.netUtils.RxSchedulers;
import com.suntown.scannerproject.query.QueryActivity;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.Utils;
import com.suntown.scannerproject.utils.Xml2Bean;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class MainActivity extends BaseActivity{

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
    @BindView(R.id.tv_change_shop)
    TextView tvChangeShop;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    @BindView(R.id.loadbar)
    ProgressBar loadbar;

    File file = null;
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
        //TODO 检查跟新
        checkUpdate();
    }

    private void checkUpdate() {
        int versionCode = getVersionCode();
        //TODO 请求服务器
//        http://192.168.0.143:8080/esl/PAD_UpdategetLastPdaUpdateInfo?dtype=3&swversion=1
        String ip = Constant.formatBASE_HOST(ApiConstant.UPDATE_URL);
//        Map<String, String> params = new HashMap<>();
//        params.put(Constant.DTYPE, "4");
//        params.put(Constant.SWVERSION, versionCode + "");
//        Log.i(TAG, "ip:" + ip + ":usercode:" + usercode);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).getUpdateList("4",versionCode+"").compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG, "s:" + s.toString());
            try {
                UpdateBean updateBean = new Xml2Bean(s).PullUpdateXML();
                String swversion = updateBean.swversion;
                int version = Integer.parseInt(swversion);
                if (version > versionCode) {
                    //TODO 弹出dialog
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("提示")
                            .setContentText("已检测到新版本，是否更新")
                            .setCancelText("取消")
                            .setConfirmText("确定")
                            .showCancelButton(true)
                            .setCancelClickListener(sDialog -> sDialog.dismiss()).setConfirmClickListener(sDialog -> {
                        //TODO 下载APK
                        downLoadAPK(updateBean);
                        sDialog.dismiss();
                    }).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable.toString());
        });

    }

    //下载APK
    private void downLoadAPK(UpdateBean updateBean) {
        loadbar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                file = getFileFromServer(updateBean,loadbar);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG,e.toString());
            }
//                installApk(file);
        }).start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private int getVersionCode() {
        int versionName = -1;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("版本号获取异常", e.getMessage());
        }
        return versionName;
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
            R.id.rl_price, R.id.rl_query, R.id.rl_update_goods, R.id.iv_head,
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
                Log.i(TAG, "点击了");
                startActivity(new Intent(MainActivity.this,SettingPortAct.class));
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

//    public interface OnFinishListener {
//        void onFinish();
//    }
//    @Override
//    public void onFinish(File file) {
//        installApk(file);
//    }
//
//    public OnFinishListener onFinishListener;
//    public void setOnFinishListener(OnFinishListener onListener) {
//        onFinishListener = onListener;
//    }
    public File getFileFromServer(UpdateBean updateBean, ProgressBar loadbar) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        String fswid = updateBean.fswid;
        String filename = updateBean.filename;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(ApiConstant.UPDATE_URL+"/esl/PAD_UpdatedownloadFile?fswid=" + fswid);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            int contentLength = conn.getContentLength();
            loadbar.setMax(contentLength);
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                int finalTotal = total;
                runOnUiThread(() -> {
                    loadbar.setProgress(finalTotal);
                    if (finalTotal==contentLength) {
                        loadbar.setVisibility(View.GONE);
                        installApk(file);
                    }
                });
            }
            fos.close();
            bis.close();
//            is.close();
            return file;
        } else {
            return null;
        }
    }



}

package com.suntown.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suntown.R;
import com.suntown.utils.BitmapUtils;
import com.suntown.utils.Constant;
import com.suntown.utils.QRCodeUtil;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.widget.CircleImageView;
import com.suntown.widget.RoundAngleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DbarCodeActivity extends BaseActivity {

    private static final String TAG = "DbarCodeActivity";
    @BindView(R.id.iv_header)
    RoundAngleImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_xing)
    ImageView ivXing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbar_code);
        ButterKnife.bind(this);
        tvName.setText(SPUtils.getString(this, Constant.NICKNAME));
        String userName = SPUtils.getString(this, Constant.LOGIN_USER_NAME);
        //TODO 生成二维码
        Bitmap qrImage = QRCodeUtil.createQRImage(userName);
        ivXing.setImageBitmap(qrImage);

//        Bitmap path = BitmapUtils.getBitmapFromPath(userName);
//        if (path != null) {
//            ivHeader.setImageBitmap(path);
//        } else {
//            Log.i("MyActivity", "未找到本地图片");
//            ivHeader.setImageResource(R.drawable.user);
//        }

        String avatar = SPUtils.getString(this, Constant.AVATAR);
        Log.d("MainActivity", "avatar:"+avatar);
        if (!"".equals(avatar)){
            String url = Utils.replaceString(avatar);
//            Bitmap qrImage = QRCodeUtil.createQRImage(avatar);
//            ivUploadPhoto.setImageBitmap(qrImage);
            Log.i(TAG,"url:"+url);
            Picasso.with(this).load(url).error(R.drawable.no_photo).into(ivHeader);
        }else{
            Bitmap path = BitmapUtils.getBitmapFromPath(userName);
            if (path != null) {
                Log.d("MainActivity", userName);
                ivHeader.setImageBitmap(path);
            } else {
                ivHeader.setImageResource(R.drawable.user);
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}

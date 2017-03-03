package com.suntown.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.suntown.R;
import com.suntown.utils.Constant;

public class PicActivity extends BaseActivity {

    private String picName;
    private PicActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity = this;
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            picName = bundle.getString("picName"); //图片名
        }
        ImageView img = (ImageView)this.findViewById(R.id.large_image );
        Picasso.with(this).load(picName).error(R.drawable.no_photo).into(img);
//        Toast toast = Toast.makeText(this, "点击图片即可返回",Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.BOTTOM, 0, 0);
//        toast.show();
        img.setOnClickListener(paramView -> activity.finish());
    }
}

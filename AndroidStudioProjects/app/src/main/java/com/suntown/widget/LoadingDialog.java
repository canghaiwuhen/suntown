package com.suntown.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

import com.suntown.R;


/**
 * Created by Shinelon on 2016/9/25.
 */

public class LoadingDialog extends Dialog {
    private static final long DURATION = 3000;
    private Context context;
    private RotateAnimation animationL2R;
    private RelativeLayout rlMain;

    public LoadingDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        rlMain = (RelativeLayout) findViewById(R.id.rl_main);
        //点击外部不消失
        setCanceledOnTouchOutside(true);
        // 从左到右的旋转动画，设置旋转角度和旋转中心
        LinearInterpolator lin = new LinearInterpolator();
        animationL2R = new RotateAnimation(0f, 359f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        // 设置动画的运行时长
        animationL2R.setInterpolator(lin);
        animationL2R.setDuration(DURATION);
        // 动画运行结束之后，保存结束之后的状态
        animationL2R.setFillAfter(true);
        // 设置重复的次数
        animationL2R.setRepeatCount(-1);
//        animationL2R.setRepeatMode(Animation.RESTART);
        rlMain.setAnimation(animationL2R);
        animationL2R.start();

    }
    @Override
    protected void onStop() {
        super.onStop();
        animationL2R.cancel();
    }
}

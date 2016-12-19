package com.suntown.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.suntown.R;


/**
 * Created by lei on 2016/7/29 0029.
 */
public class AppleDialog extends Dialog implements View.OnClickListener {

    private TextView tvTop;
    private TextView tvBottom;
    private TextView tvCancel;
    private String topText;
    private String bottomText;
    private ColorStateList topTextColor;
    private ColorStateList bottomTextColor;
    private Context context;


    public AppleDialog(Context context, String topText, ColorStateList topTextColor, String bottomText, ColorStateList bottomTextColor) {
        super(context, R.style.AppleDialogStyle);
        this.context = context;
        this.topText = topText;
        this.bottomText = bottomText;
        this.topTextColor = topTextColor;
        this.bottomTextColor = bottomTextColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetContentView();
        initInitialize();
    }

    private void initSetContentView() {
        setContentView(R.layout.dialog_apple);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);
        tvTop = (TextView) findViewById(R.id.tv_top);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvBottom.setText(bottomText);
        tvBottom.setTextColor(bottomTextColor == null ? ContextCompat.getColorStateList(context, R.color.normalTextColor) : bottomTextColor);
        tvTop.setText(topText);
        tvTop.setTextColor(topTextColor == null ? ContextCompat.getColorStateList(context, R.color.normalTextColor) : topTextColor);
    }

    private void initInitialize() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        initClick();
    }

    private void initClick() {
        tvTop.setOnClickListener(this);
        tvBottom.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    public interface AppleDialogListener {
        void onTopClick();

        void onBottomClick();
    }

    public AppleDialog setmDialogListener(AppleDialogListener mDialogListener) {
        this.mDialogListener = mDialogListener;
        return this;
    }

    private AppleDialogListener mDialogListener;

    @Override
    public void onClick(View view) {
        if (mDialogListener != null) {
            switch (view.getId()) {
                case R.id.tv_top:
                    mDialogListener.onTopClick();
                    break;
                case R.id.tv_bottom:
                    mDialogListener.onBottomClick();
                    break;
            }
        }
        this.cancel();
    }
}

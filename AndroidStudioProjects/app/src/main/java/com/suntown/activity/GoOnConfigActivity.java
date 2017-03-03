package com.suntown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.suntown.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoOnConfigActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_on_config);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_goon, R.id.btn_start})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_goon:
                finish();
                break;
            case R.id.btn_start:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
        }
    }
}

package com.suntown.suntownshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
/**
 * 
 * ����ҳ��
 * @author Ǯ��
 * @version 2015��6��8�� ����1:42:36
 *
 */
public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
	}
	
	public void close(View v){
		finish();
	}
	
	
	
}

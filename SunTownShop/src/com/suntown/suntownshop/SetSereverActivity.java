package com.suntown.suntownshop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
/**
 * ѡ����������ѷ���������ʱɾ��
 *
 * @author Ǯ��
 * @version 2015��9��21�� ����9:53:35
 *
 */
public class SetSereverActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setserver);
	}

	public void confirm(View v) {
		EditText et = (EditText) findViewById(R.id.et_servername);
		String serverName = et.getText().toString();
		Constants.DOMAIN_NAME = "http://"+serverName+":8080/";
		finish();
	}
	
	public void close(View v){
		finish();
	}
}

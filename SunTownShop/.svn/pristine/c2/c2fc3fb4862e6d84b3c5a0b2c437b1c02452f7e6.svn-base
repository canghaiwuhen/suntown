package com.suntown.suntownshop.widget;

import com.suntown.suntownshop.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
/**
 * 自定义提示对话框
 * @author Administrator
 *
 */
public class ConfirmDialog {
	boolean result = false;
	AlertDialog dialog;
	Handler mHandler;
	String message;
	String title;
	String confirmText;
	String cancelText;
	public ConfirmDialog(Context context, String message, String title,
			String confirmText, String cancelText) {
		this.message = message;
		this.title = title;
		this.confirmText = confirmText;
		this.cancelText = cancelText;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		//builder.setMessage(message);
		//builder.setTitle(title);
		/*builder.setPositiveButton(confirmText,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						result = true;
						dialog.dismiss();
						Message m = mHandler.obtainMessage();
						mHandler.sendMessage(m);
					}
				});
		builder.setNegativeButton(cancelText,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						result = false;
						dialog.dismiss();
						Message m = mHandler.obtainMessage();
						mHandler.sendMessage(m);
					}
				});*/
		dialog = builder.create();
	}

	public boolean ShowDialog() {

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message mesg) {
				// process incoming messages here
				// super.handleMessage(msg);
				throw new RuntimeException();
			}
		};
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.confirmdialog_layout);
		View viewOk = window.findViewById(R.id.view_ok);
		View viewCancel = window.findViewById(R.id.view_cancel);
		TextView tvOk = (TextView)window.findViewById(R.id.tv_btn_confirm);
		TextView tvCancel = (TextView)window.findViewById(R.id.tv_btn_cancel);
		tvOk.setText(confirmText);
		tvCancel.setText(cancelText);
		TextView tvMsg = (TextView)window.findViewById(R.id.tv_msg);
		tvMsg.setText(message);
		viewOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				result = true;
				dialog.dismiss();
				Message m = mHandler.obtainMessage();
				mHandler.sendMessage(m);
			}
		});
		viewCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				result = false;
				dialog.dismiss();
				Message m = mHandler.obtainMessage();
				mHandler.sendMessage(m);
			}
		});
		try {
			Looper.getMainLooper().loop();
		} catch (RuntimeException e) {
		}
		// Toast.makeText(getActivity(), isDel+"",
		// Toast.LENGTH_SHORT).show();
		return result;
	}

}

package com.suntown.widget.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.suntown.R;


public class PopWindow extends PopupWindow {
	private View conentView;

	public PopWindow(final Activity context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.popup_dialog, null);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		this.setContentView(conentView);
		this.setWidth(w / 3 );
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);

		this.setAnimationStyle(R.style.AnimationPreview);
		LinearLayout item1 = (LinearLayout) conentView
				.findViewById(R.id.ll_item1);
		LinearLayout item2 = (LinearLayout) conentView
				.findViewById(R.id.ll_item2);
		item1.setOnClickListener(arg0 -> {
            mDialogListener.onTopClick();
            PopWindow.this.dismiss();
        });

		item2.setOnClickListener(v -> {
            mDialogListener.onBottomClick();
            PopWindow.this.dismiss();
        });
	}

	public PopWindow setmDialogListener(AddPopWindowListener mDialogListener) {
		this.mDialogListener = mDialogListener;
		return this;
	}
	public interface AddPopWindowListener {
		void onTopClick();

		void onBottomClick();
	}

	private AddPopWindowListener mDialogListener;

	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			this.showAsDropDown(parent, parent.getLayoutParams().width /2, 18);
		} else {
			this.dismiss();
		}
	}
}

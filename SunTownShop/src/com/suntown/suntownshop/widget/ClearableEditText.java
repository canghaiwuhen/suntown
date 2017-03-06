package com.suntown.suntownshop.widget;



import com.suntown.suntownshop.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 带清除按钮的EditText
 * 
 * @author ken
 *
 */
public class ClearableEditText extends EditText {

	public String defaultValue = "";
	private Drawable imgX = getResources().getDrawable(R.drawable.cleanup);

	public ClearableEditText(Context context) {
		super(context);

		init();
	}

	public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	public ClearableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public void setClearImage(int resId) {
		imgX = getResources().getDrawable(resId);
		init();
	}

	void init() {

		// Set bounds of our X button
		imgX.setBounds(0, 0, imgX.getIntrinsicWidth(),
				imgX.getIntrinsicHeight());

		// There may be initial text in the field, so we may need to display the
		// button
		manageClearButton();

		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				ClearableEditText et = ClearableEditText.this;

				// Is there an X showing?
				if (et.getCompoundDrawables() == null)
					return false;
				// Only do this for up touches
				if (event.getAction() != MotionEvent.ACTION_UP)
					return false;
				// Is touch on our clear button?
				if (event.getX() > et.getWidth() - et.getPaddingRight()
						- imgX.getIntrinsicWidth()) {
					et.setText("");
					ClearableEditText.this.removeClearButton();
				}
				return false;
			}

		});

		this.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				ClearableEditText.this.manageClearButton();
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});

		this.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				ClearableEditText.this.manageClearButton();
			}
		});
	}

	void manageClearButton() {
		if (this.getText().toString().equals("") || !this.isFocused())
			removeClearButton();
		else
			addClearButton();
	}

	void addClearButton() {
		this.setCompoundDrawables(this.getCompoundDrawables()[0],
				this.getCompoundDrawables()[1], imgX,
				this.getCompoundDrawables()[3]);
	}

	void removeClearButton() {
		this.setCompoundDrawables(this.getCompoundDrawables()[0],
				this.getCompoundDrawables()[1], null,
				this.getCompoundDrawables()[3]);
	}

}

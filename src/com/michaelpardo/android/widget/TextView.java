package com.michaelpardo.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;

import com.michaelpardo.android.R;
import com.michaelpardo.android.util.Ui;

public class TextView extends android.widget.TextView {
	public TextView(Context context) {
		super(context);
		init(context, null, 0);
	}

	public TextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public TextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Button, defStyle, 0);
		final int textStyle = a.getInt(R.styleable.Button_textStyle, 0);
		final String typeface = a.getString(R.styleable.Button_typeface);

		a.recycle();

		if (typeface != null) {
			Ui.setTypeface(this, typeface);
		}
		else if (textStyle > 0) {
			Ui.setTypefaceByStyle(this, textStyle);
		}
	}

	@Override
	public boolean isFocused() {
		if (getEllipsize() == TruncateAt.MARQUEE) {
			return true;
		}

		return super.isFocused();
	}
}
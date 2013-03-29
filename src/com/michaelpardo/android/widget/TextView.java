package com.michaelpardo.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;

import com.michaelpardo.android.R;
import com.michaelpardo.android.util.Ui;

public class TextView extends android.widget.TextView {
	private static final TransformationMethod TRANSFORMATION_TEXT_ALL_CAPS = new TransformationMethod() {
		@Override
		public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction,
				Rect previouslyFocusedRect) {
		}

		@Override
		public CharSequence getTransformation(CharSequence source, View view) {
			return source.toString().toUpperCase(view.getContext().getResources().getConfiguration().locale);
		}
	};

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
		final int textStyle = a.getInt(R.styleable.TextView_textStyle, 0);
		final String typeface = a.getString(R.styleable.TextView_typeface);
		final boolean textAllCaps = a.getBoolean(R.styleable.TextView_textAllCaps, false);

		a.recycle();

		if (typeface != null) {
			Ui.setTypeface(this, typeface);
		}
		else if (textStyle > 0) {
			Ui.setTypefaceByStyle(this, textStyle);
		}

		if (textAllCaps) {
			setTransformationMethod(TRANSFORMATION_TEXT_ALL_CAPS);
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
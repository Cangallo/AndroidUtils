package com.michaelpardo.android.widget;

/*
 * Copyright (C) 2010 Michael Pardo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.michaelpardo.android.R;

public class AutoResizeTextView extends com.michaelpardo.android.widget.TextView {
	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE CONSTANTS
	//////////////////////////////////////////////////////////////////////////////////////

	private static final float MIN_TEXT_SIZE = 1.0f;
	private static final Canvas TEXT_RESIZE_CANVAS = new Canvas();

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	//////////////////////////////////////////////////////////////////////////////////////

	private boolean mNeedsResize = false;

	private int mMaxLines;

	private float mMaxTextSize = 0.0f;
	private float mMinTextSize = MIN_TEXT_SIZE;
	private float mSpacingMult = 1.0f;
	private float mSpacingAdd = 0.0f;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	//////////////////////////////////////////////////////////////////////////////////////

	public AutoResizeTextView(Context context) {
		super(context);
		init(context, null, 0);
	}

	public AutoResizeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public AutoResizeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// OVERRIDES
	//////////////////////////////////////////////////////////////////////////////////////

	// Properties

	@Override
	public void setLineSpacing(float add, float mult) {
		super.setLineSpacing(add, mult);
		mSpacingMult = mult;
		mSpacingAdd = add;
	}

	@Override
	public void setMaxLines(int maxlines) {
		super.setMaxLines(maxlines);
		mMaxLines = maxlines;
	}

	// Events

	@Override
	protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
		mNeedsResize = true;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (w != oldw || h != oldh) {
			mNeedsResize = true;
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if (changed || mNeedsResize) {
			resizeText(right - left - getCompoundPaddingLeft() - getCompoundPaddingRight());
		}

		super.onLayout(changed, left, top, right, bottom);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public float getMinTextSize() {
		return mMinTextSize;
	}

	public void setMinTextSize(float minTextSize) {
		mMinTextSize = minTextSize;
		requestLayout();
		invalidate();
	}

	public float getMaxTextSize() {
		return mMaxTextSize;
	}

	public void setMaxTextSize(float maxTextSize) {
		mMaxTextSize = maxTextSize;
		requestLayout();
		invalidate();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private void init(Context context, AttributeSet attrs, int defStyle) {
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AutoResizeTextView);
		setMaxTextSize(attributes.getDimension(R.styleable.AutoResizeTextView_maxTextSize, getTextSize()));
		setMinTextSize(attributes.getDimension(R.styleable.AutoResizeTextView_minTextSize, MIN_TEXT_SIZE));
	}

	private void resizeText(int widthPx) {
		CharSequence text = getText();
		// Do not resize if the view does not have dimensions or there is no text
		if (text == null || text.length() == 0 || widthPx <= 0) {
			return;
		}

		// Get the text view's paint object (as a copy, so we don't modify it)
		TextPaint textPaint = new TextPaint();
		textPaint.set(getPaint());

		// If there is a max text size set, use that; otherwise, base the max text size
		// on the current text size.
		float targetTextSize = mMaxTextSize > 0 ? mMaxTextSize : textPaint.getTextSize();

		// Default to a single line for display
		int maxLines = mMaxLines > 0 ? mMaxLines : 1;

		int lineCount = getTextLineCount(text, textPaint, widthPx, targetTextSize);
		while (lineCount > maxLines && targetTextSize > mMinTextSize) {
			targetTextSize -= 1;
			lineCount = getTextLineCount(text, textPaint, widthPx, targetTextSize);
		}

		// Some devices try to auto adjust line spacing, so force default line spacing 
		// and invalidate the layout as a side effect
		setTextSize(TypedValue.COMPLEX_UNIT_PX, targetTextSize);
		setLineSpacing(mSpacingAdd, mSpacingMult);

		// Reset force resize flag
		mNeedsResize = false;
	}

	private int getTextLineCount(CharSequence source, TextPaint paint, int widthPx, float textSize) {
		// Update the text paint object
		paint.setTextSize(textSize);

		// Draw using a static layout
		StaticLayout layout = new StaticLayout(source, paint, widthPx, Alignment.ALIGN_NORMAL, mSpacingMult,
				mSpacingAdd, true);
		layout.draw(TEXT_RESIZE_CANVAS);

		return layout.getLineCount();
	}
}

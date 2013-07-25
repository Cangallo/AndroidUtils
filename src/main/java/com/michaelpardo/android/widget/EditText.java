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
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class EditText extends android.widget.EditText {
	public EditText(Context context) {
		super(context);
	}

	public EditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// Fix for missing error icon taken from:
	// https://github.com/OlegVaskevich/JB-showError-fixed

	/**
	 * Don't send delete key so edit text doesn't capture it and close error
	 */
	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (TextUtils.isEmpty(getText().toString()) && keyCode == KeyEvent.KEYCODE_DEL)
			return true;
		else
			return super.onKeyPreIme(keyCode, event);
	}

	/**
	 * Keep track of which icon we used last
	 */
	private Drawable lastErrorIcon = null;

	/**
	 * Resolve an issue where the error icon is hidden under some cases in JB
	 * due to a bug http://code.google.com/p/android/issues/detail?id=40417
	 */
	@Override
	public void setError(CharSequence error, Drawable icon) {
		super.setError(error, icon);
		lastErrorIcon = icon;

		// if the error is not null, and we are in JB, force
		// the error to show
		if (error != null /* !isFocused() && */) {
			showErrorIconHax(icon);
		}
	}

	/**
	 * In onFocusChanged() we also have to reshow the error icon as the Editor
	 * hides it. Because Editor is a hidden class we need to cache the last used
	 * icon and use that
	 */
	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		showErrorIconHax(lastErrorIcon);
	}

	/**
	 * Use reflection to force the error icon to show. Dirty but resolves the
	 * issue in 4.2
	 */
	private void showErrorIconHax(Drawable icon) {
		if (icon == null)
			return;

		// only for JB 4.2 and 4.2.1
		if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.JELLY_BEAN
				&& android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.JELLY_BEAN_MR1)
			return;

		try {
			Class<?> textview = Class.forName("android.widget.TextView");
			Field tEditor = textview.getDeclaredField("mEditor");
			tEditor.setAccessible(true);
			Class<?> editor = Class.forName("android.widget.Editor");
			Method privateShowError = editor.getDeclaredMethod("setErrorIcon", Drawable.class);
			privateShowError.setAccessible(true);
			privateShowError.invoke(tEditor.get(this), icon);
		}
		catch (Exception e) {
			// e.printStackTrace(); // oh well, we tried
		}
	}
}

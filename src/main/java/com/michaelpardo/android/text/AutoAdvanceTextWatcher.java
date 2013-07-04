package com.michaelpardo.android.text;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class AutoAdvanceTextWatcher implements TextWatcher {
	private View mView;
	private int mMaxLength;

	public AutoAdvanceTextWatcher(View view, int maxLength) {
		mView = view;
		mMaxLength = maxLength;
	}

	public void setMaxLength(int maxLength) {
		mMaxLength = maxLength;
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() >= mMaxLength) {
			mView.focusSearch(View.FOCUS_FORWARD).requestFocus();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
}
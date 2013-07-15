package com.michaelpardo.android.text;

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

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.michaelpardo.android.util.Log;

public class MaskTextWatcher implements TextWatcher {
	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	//////////////////////////////////////////////////////////////////////////////////////

	private MaskFormatter mMaskFormatter;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	//////////////////////////////////////////////////////////////////////////////////////

	public MaskTextWatcher(String mask) {
		mMaskFormatter = new MaskFormatter(mask);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// IMPLEMENTATION
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void afterTextChanged(Editable s) {
		String filtered = mMaskFormatter.valueToString(s);
		Log.t("original: %s - filtered: %s", s, filtered);

		if (!TextUtils.equals(s, filtered)) {
			s.replace(0, s.length(), filtered);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public String getMask() {
		return mMaskFormatter.getMask();
	}

	public void setMask(String mask) {
		mMaskFormatter.setMask(mask);
	}
}
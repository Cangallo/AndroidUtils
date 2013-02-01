package com.michaelpardo.android.text;

import android.text.InputFilter;
import android.text.Spanned;

public class MaskInputFilter implements InputFilter {
	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE CONSTANTS
	//////////////////////////////////////////////////////////////////////////////////////

	private static final char KEY_DIGIT = '#';
	private static final char KEY_LITERAL = '\'';
	private static final char KEY_UPPERCASE = 'U';
	private static final char KEY_LOWERCASE = 'L';
	private static final char KEY_ALPHA_NUMERIC = 'A';
	private static final char KEY_CHARACTER = '?';
	private static final char KEY_WILDCARD = '*';
	private static final char KEY_HEX = 'H';

	private String mMask;

	public MaskInputFilter(String mask) {
		mMask = mask;
	}

	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

		return null;
	}
}
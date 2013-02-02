package com.michaelpardo.android.text;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

public class MaskTextWatcher implements TextWatcher {
	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE CONSTANTS
	//////////////////////////////////////////////////////////////////////////////////////

	private static final char KEY_DIGIT = '#';
	private static final char KEY_ESCAPE = '\'';
	private static final char KEY_UPPERCASE = 'U';
	private static final char KEY_LOWERCASE = 'L';
	private static final char KEY_ALPHA_NUMERIC = 'A';
	private static final char KEY_CHARACTER = '?';
	private static final char KEY_WILDCARD = '*';
	private static final char KEY_HEX = 'H';

	private static final String MASK_CHARS = "#ULA?*H";
	private static final String HEX_CHARS = "0123456789abcdefABCDEF";

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	//////////////////////////////////////////////////////////////////////////////////////

	private String mMask;
	private int mMaskLength;
	private int mEscapedMaskLength;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	//////////////////////////////////////////////////////////////////////////////////////

	public MaskTextWatcher(String mask) {
		mMask = mask;
		mMaskLength = mask.length();
		mEscapedMaskLength = mask.replace("'", "").length();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// IMPLEMENTATION
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void afterTextChanged(Editable s) {
		CharSequence original = s.toString();
		CharSequence filtered = applyMask(original);

		if (!TextUtils.equals(original, filtered)) {
			s.clear();
			s.append(filtered);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private CharSequence applyMask(CharSequence text) {
		// Enforce mask length
		if (text.length() > mEscapedMaskLength) {
			return text.subSequence(0, mEscapedMaskLength);
		}

		// Don't try to mask an empty string
		if (text.length() < 1) {
			return text;
		}

		// Do the masking
		String maskedText = "";
		int textLength = text.length();
		char target = mMask.charAt(0);
		char source = text.charAt(0);
		int offset = 0;

		for (int i = 0; i < mMaskLength; i++) {
			// Break for end of input text
			if (i - offset >= textLength) {
				break;
			}

			// Get mask target and source characters
			target = mMask.charAt(i);
			source = text.charAt(i - offset);

			// If this is an escape character step and add it
			if (isEscape(target)) {
				i++;
				offset += 2;
				maskedText += mMask.charAt(i);
			}
			// Not a mask character
			else if (MASK_CHARS.indexOf(target) < 0) {
				// Doesn't match the literal
				if (target != source) {
					offset++;
				}

				// Don't allow this non-maks character
				// to be the last character in our text
				if (i - offset == textLength - 1) {
					break;
				}

				maskedText += target;
			}
			// Is a mask character and matches type
			else if (matches(target, source)) {
				maskedText += filter(target, source);
			}
			// Is a mask character and doesn't match type
			else {
				break;
			}
		}

		return maskedText;
	}

	private boolean isEscape(char target) {
		return target == KEY_ESCAPE;
	}

	private boolean matches(char target, char source) {
		switch (target) {
		case KEY_DIGIT: {
			return Character.isDigit(source);
		}
		case KEY_ESCAPE: {
			return true;
		}
		case KEY_UPPERCASE:
		case KEY_LOWERCASE:
		case KEY_CHARACTER: {
			return Character.isLetter(source);
		}
		case KEY_ALPHA_NUMERIC: {
			return Character.isLetter(source) || Character.isDigit(source);
		}
		case KEY_WILDCARD: {
			return true;
		}
		case KEY_HEX: {
			return HEX_CHARS.indexOf(source) >= 0;
		}
		}

		return false;
	}

	private Character filter(char target, char source) {
		switch (target) {
		case KEY_UPPERCASE: {
			return Character.toUpperCase(source);
		}
		case KEY_LOWERCASE: {
			return Character.toLowerCase(source);
		}
		}

		return source;
	}
}
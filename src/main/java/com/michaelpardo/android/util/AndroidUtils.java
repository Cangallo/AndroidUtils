package com.michaelpardo.android.util;

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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;

import com.michaelpardo.android.R;

public class AndroidUtils {
	private AndroidUtils() {
	}

	public static String getAppName(Context context) {
		return getAppName(context, null);
	}

	public static String getAppName(Context context, String packageName) {
		String applicationName;

		if (packageName == null) {
			packageName = context.getPackageName();
		}

		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
			applicationName = context.getString(packageInfo.applicationInfo.labelRes);
		}
		catch (Exception e) {
			Log.w("Failed to get version number.", e);
			applicationName = "";
		}

		return applicationName;
	}

	public static String getAppVersionNumber(Context context) {
		return getAppVersionNumber(context, null);
	}

	public static String getAppVersionNumber(Context context, String packageName) {
		String versionName;

		if (packageName == null) {
			packageName = context.getPackageName();
		}

		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
			versionName = packageInfo.versionName;
		}
		catch (Exception e) {
			Log.w("Failed to get version number.", e);
			versionName = "";
		}

		return versionName;
	}

	public static String getAppVersionCode(Context context) {
		return getAppVersionCode(context, null);
	}

	public static String getAppVersionCode(Context context, String packageName) {
		String versionCode;

		if (packageName == null) {
			packageName = context.getPackageName();
		}

		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
			versionCode = Integer.toString(packageInfo.versionCode);
		}
		catch (Exception e) {
			Log.w("Failed to get version code.", e);
			versionCode = "";
		}

		return versionCode;
	}

	public static int getSdkVersion() {
		try {
			return Build.VERSION.class.getField("SDK_INT").getInt(null);
		}
		catch (Exception e) {
			return 3;
		}
	}

	@Deprecated
	/*
	 * This method is deprecated. Use BuildConfig.DEBUG instead
	 */
	public static boolean isRelease(Context context) {
		final String releaseSignatureString = context.getString(R.string.release_signature);
		if (releaseSignatureString == null || releaseSignatureString.length() == 0) {
			throw new RuntimeException("Release signature string is null or missing.");
		}

		final Signature releaseSignature = new Signature(context.getString(R.string.release_signature));

		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature sig : pi.signatures) {
				if (sig.equals(releaseSignature)) {
					Log.v("Determined that this is a RELEASE build.");
					return true;
				}
			}
		}
		catch (Exception e) {
			Log.w("Exception thrown when detecting if app is signed by a release keystore, assuming this is a release build.",
					e);

			// Return true if we can't figure it out
			return true;
		}

		Log.v("Determined that this is a DEBUG build.");

		return false;
	}

	public static boolean isEmulator() {
		return Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk");
	}
}

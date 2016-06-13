package com.uc.ronrwin.uctopic.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.uc.ronrwin.uctopic.application.UCTopicApplication;

/**
 * 参数辅助类.
 * 
 */
public class PreferencesHelper {

	private static final String LOCAL_DATA = "local_data";
	private static final String TAB = "tab";
	private static final String LAST_UPDATE_TIME = "last_update_time";

	/**
	 * 获取Context实例.
	 * 
	 * @return
	 */
	public static Context getContext() {
		return UCTopicApplication.mContext;
	}

	/**
	 * 获取SharedPreferences实例.
	 * 
	 * @param name
	 *            SP名
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(String name) {
		return getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public static void saveTabData(String data) {
		SharedPreferences.Editor editor = getSharedPreferences(LOCAL_DATA).edit();
		editor.putString(TAB, data);
		editor.apply();
	}

	public static String getTabData() {
		SharedPreferences sharedPreferences = getSharedPreferences(LOCAL_DATA);
		return sharedPreferences.getString(TAB, "");
	}

	public static void saveLastUpdateTime(long last) {
		SharedPreferences.Editor editor = getSharedPreferences(LOCAL_DATA).edit();
		editor.putLong(LAST_UPDATE_TIME, last);
		editor.apply();
	}

	public static long getLastUpdateTime() {
		SharedPreferences sharedPreferences = getSharedPreferences(LOCAL_DATA);
		return sharedPreferences.getLong(LAST_UPDATE_TIME, 0L);
	}
}

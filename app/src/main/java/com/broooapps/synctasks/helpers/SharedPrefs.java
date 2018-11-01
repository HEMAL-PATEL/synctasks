package com.broooapps.synctasks.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    private static SharedPreferences prefs = null;
    private final static String PREFS_NAME = "SYNC_TASKS_PREF";
    public static final String PREF_TASKS = "PREF_TASKS";

    public static String getValue(Context context, String key,
                                  String defaultValue) {
        prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, defaultValue);
    }

    /**
     * To set/update value for a particular key in Shared Preference
     *
     * @param context context associated with view
     * @param key The key in which the value would be set
     * @param value The value which would be set to key
     */
    public static void setValue(Context context, String key, String value) {
        prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
}

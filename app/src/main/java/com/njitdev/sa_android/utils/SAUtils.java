package com.njitdev.sa_android.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yl on 6/11/17.
 */

public class SAUtils {
    // Write a KV pair to local storage
    public static void writeKVStore(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("sa-android", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, value);
        e.apply();
    }

    // Read a KV pair from local storage
    public static String readKVStore(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("sa-android", Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }
}

package com.njitdev.sa_android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

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

    // Get installation_id (generate if not already)
    public static String installationID(Context context) {
        if (SAGlobal.installation_id == null) {
            String uuid = readKVStore(context, "installation_id");
            if (uuid == null) {
                uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
                writeKVStore(context, "installation_id", uuid);
            }
            SAGlobal.installation_id = uuid;
        }
        return SAGlobal.installation_id;
    }
}

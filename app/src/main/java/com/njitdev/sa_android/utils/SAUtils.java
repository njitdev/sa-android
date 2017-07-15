/*
    sa-android
    Copyright (C) 2017 sa-android authors

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.njitdev.sa_android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

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

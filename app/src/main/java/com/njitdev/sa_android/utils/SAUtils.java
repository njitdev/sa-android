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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.njitdev.sa_android.BuildConfig;
import com.njitdev.sa_android.models.analytics.AnalyticsModels;
import com.njitdev.sa_android.models.version.VersionModels;
import com.rollbar.android.Rollbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SAUtils {
    // Application initialization
    public static void appInit(Context context) {
        // Set developmentMode
        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            // Development mode
            SAConfig.developmentMode = true;
            SAConfig.baseURL = "https://api.blupig.net/sa-api-dev";

            Toast.makeText(context, "Debug mode: " + BuildConfig.FLAVOR, Toast.LENGTH_LONG).show();
        } else {
            // Production mode
            SAConfig.developmentMode = false;
            SAConfig.baseURL = "https://api.blupig.net/sa-api-prd";

            // Error reporting
            // Reports uncaught exceptions by default
            Rollbar.init(context, SAConfig.rollbarClientID, "production");
        }

        // Google Analytics
        SAGlobal.getGATracker(context);

        // Create shared request queue
        SAGlobal.sharedRequestQueue = Volley.newRequestQueue(context);

        // Send start event
        AnalyticsModels.sendStartEvent(installationID(context));
    }

    // Version check
    public static void checkForNewVersion(final Context context) {
        VersionModels.fetchLatestVersion(new ModelListener<String>() {
            @Override
            public void onData(String result, String message) {
                if (result != null) {
                    if (result.compareTo(BuildConfig.VERSION_NAME) > 0) {
                        // Update available
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        // Generate message
                        String updateMessage = BuildConfig.VERSION_NAME + " -> " + result + "\n\n旧版本 bug 很多！建议及时更新 :)";
                        if (BuildConfig.FLAVOR.equals("market")) {
                            updateMessage += "\n请到你下载本应用的市场更新";
                        }

                        builder.setTitle("发现新版本");
                        builder.setMessage(updateMessage);
                        builder.setCancelable(true);

                        // Different dialog for download version and market version
                        if (BuildConfig.FLAVOR.equals("market")) {
                            builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            builder.setNegativeButton("直接下载安装包", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(SAConfig.apkDownloadURL));
                                    context.startActivity(intent);
                                    dialogInterface.dismiss();
                                }
                            });
                        } else {
                            builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(SAConfig.apkDownloadURL));
                                    context.startActivity(intent);
                                    dialogInterface.dismiss();
                                }
                            });

                            builder.setNegativeButton("先不更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                        }

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        });
    }

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

    // Get installationID (generate if not already)
    public static String installationID(Context context) {
        if (SAGlobal.installationID == null) {
            String uuid = readKVStore(context, "installationID");
            if (uuid == null) {
                uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
                writeKVStore(context, "installationID", uuid);
            }
            SAGlobal.installationID = uuid;
        }
        return SAGlobal.installationID;
    }

    // Get device model
    // https://stackoverflow.com/a/12707479/1690380
    public static String getDeviceModel() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    // Get current reference week (number of weeks since 1970-01-05)
    public static int currentReferenceWeek() {
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date ref = f.parse("1970-01-05");
            Date current = new Date();
            return (int)(((float)(current.getTime() - ref.getTime())) / 1000 / 86400 / 7);
        } catch (Exception e) {
            return 0;
        }
    }
}

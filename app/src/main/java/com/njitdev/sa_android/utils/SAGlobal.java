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

import com.android.volley.RequestQueue;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.njitdev.sa_android.R;

public class SAGlobal {
    public static RequestQueue sharedRequestQueue;
    public static String student_session_id;
    public static String installation_id;

    // Google Analytics
    private static GoogleAnalytics analytics;
    private static Tracker tracker;

    synchronized public static Tracker getGATracker(Context context) {
        if (tracker == null) {
            analytics = GoogleAnalytics.getInstance(context);
            tracker = analytics.newTracker(R.xml.ga_app_tracker);
        }
        return tracker;
    }
}

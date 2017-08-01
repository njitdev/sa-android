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

package com.njitdev.sa_android.models.analytics;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.njitdev.sa_android.BuildConfig;
import com.njitdev.sa_android.utils.SAConfig;
import com.njitdev.sa_android.utils.SAGlobal;
import com.njitdev.sa_android.utils.SAUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AnalyticsModels {
    private static String baseURL = SAConfig.baseURL + "/app/analytics";

    // Submit new post
    public static void sendStartEvent(String installationID) {
        // Prepare parameters
        Map<String, String> map = new HashMap<>();
        map.put("installation_id", installationID);
        map.put("school", SAConfig.schoolIdentifier);
        map.put("client_version", BuildConfig.VERSION_NAME);
        map.put("device_info", SAUtils.getDeviceModel());
        JSONObject params = new JSONObject(map);

        // Make request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, baseURL + "/start", params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        SAGlobal.sharedRequestQueue.add(jsonObjectRequest);
    }
}

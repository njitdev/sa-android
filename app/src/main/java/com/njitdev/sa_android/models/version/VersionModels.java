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

package com.njitdev.sa_android.models.version;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.njitdev.sa_android.utils.ModelListener;
import com.njitdev.sa_android.utils.SAConfig;
import com.njitdev.sa_android.utils.SAGlobal;

import org.json.JSONObject;

public class VersionModels {
    private static String baseURL = SAConfig.baseURL + "/app/version";

    // Fetch latest version string
    public static void fetchLatestVersion(final ModelListener<String> listener) {
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, baseURL + "/latest", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject result = response.getJSONObject("result");
                            String version = result.getString("android");
                            listener.onData(version, "ok");
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onData(null, "服务器通信失败 (1)");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onData(null, "服务器通信失败 (1)");
            }
        });
        SAGlobal.sharedRequestQueue.add(r);
    }
}

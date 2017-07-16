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

package com.njitdev.sa_android.models.school;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.njitdev.sa_android.utils.ModelListener;
import com.njitdev.sa_android.utils.SAConfig;
import com.njitdev.sa_android.utils.SAGlobal;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SchoolSystemModels {
    private static String baseURL = SAConfig.baseURL + "/school/" + SAConfig.schoolIdentifier;

    // Authentication - submit
    public static void submitAuthInfo(String installation_id, String student_login, String student_password, String captcha, final ModelListener<String> listener) {
        // Prepare parameters
        Map<String, String> map = new HashMap<>();
        map.put("installation_id", installation_id);
        map.put("student_login", student_login);
        map.put("student_password", student_password);
        map.put("captcha", captcha); // Optional
        JSONObject params = new JSONObject(map);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, baseURL + "/auth/submit", params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("result");
                    // Check auth_result
                    if (result.getBoolean("auth_result")) {
                        // Pass session_id
                        listener.onData(result.getString("session_id"), "ok");
                    } else {
                        listener.onData(null, "登录失败，请检查用户名和密码");
                    }
                } catch (JSONException e) {
                    listener.onData(null, "数据解析失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onData(null, "连接学校服务器失败");
            }
        });
        SAGlobal.sharedRequestQueue.add(req);
    }

    // Fetch student basic info
    public static void studentBasicInfo(String session_id, String student_id, final ModelListener<StudentBasicInfo> listener) {
        // Build GET URL
        // TODO: Find a better way
        String url = baseURL + "/student/basic-info?session_id=";
        try {
            url += URLEncoder.encode(session_id, "UTF-8");

            // Optional fields
            if (student_id != null) url += "&student_id=" + URLEncoder.encode(student_id, "UTF-8");
        } catch (Exception e) { e.printStackTrace(); }

        // Make request
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("result");
                    StudentBasicInfo basicInfo = new StudentBasicInfo();

                    basicInfo.student_name = result.getString("student_name");
                    basicInfo.student_department = result.getString("student_department");

                    // Optional fields
                    if (result.has("student_id") && !result.isNull("student_id"))
                        basicInfo.student_id = result.getString("student_id");

                    if (result.has("student_enroll_year") && !result.isNull("student_enroll_year"))
                        basicInfo.student_id = result.getString("student_enroll_year");

                    if (result.has("student_major") && !result.isNull("student_major"))
                        basicInfo.student_id = result.getString("student_major");

                    if (result.has("student_class") && !result.isNull("student_class"))
                        basicInfo.student_id = result.getString("student_class");

                    listener.onData(basicInfo, "ok");
                } catch (JSONException e) {
                    listener.onData(null, "数据解析失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onData(null, "连接学校服务器失败");
            }
        });
        SAGlobal.sharedRequestQueue.add(req);
    }
}

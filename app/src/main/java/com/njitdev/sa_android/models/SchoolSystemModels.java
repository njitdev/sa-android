package com.njitdev.sa_android.models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.njitdev.sa_android.R;
import com.njitdev.sa_android.utils.ModelListener;
import com.njitdev.sa_android.utils.SAConfig;
import com.njitdev.sa_android.utils.SAGlobal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yl on 6/11/17.
 */

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
                Log.d("sa-android", new String(error.networkResponse.data));
            }
        });
        SAGlobal.sharedRequestQueue.add(req);
    }
}

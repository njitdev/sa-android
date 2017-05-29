package com.njitdev.sa_android.messageboard;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.njitdev.sa_android.utils.ModelListener;
import com.njitdev.sa_android.utils.SAConfig;
import com.njitdev.sa_android.utils.SAGlobal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WZ on 4/9/17.
 */

public class Models {
    private static String baseURL = SAConfig.baseURL + "/app/msgboard/" + SAConfig.schoolIdentifier;

    static void fetchList(int page, final ModelListener listener) {
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, baseURL + "/posts?page=" + page,  null,
                                                    new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray posts = result.getJSONArray("posts");

                    ArrayList<Post> list = new ArrayList<>();
                    for(int i = 0; i < posts.length(); i++){
                        JSONObject post = posts.getJSONObject(i);

                        Post p = new Post();

                        // Required fields
                        p.user_name = post.getString("user_name");
                        p.text = post.getString("text");
                        p.creation_time = post.getString("creation_time");

                        // Optional fields
                        if (post.has("user_title")) p.user_title = post.getString("user_title");
                        if (post.has("user_department")) p.user_department = post.getString("user_department");

                        list.add(p);
                    }
                    listener.onData(list, "ok");
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onData(null, "获取留言信息失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onData(null, "获取留言信息失败");
            }
        });
        SAGlobal.sharedRequestQueue.add(r);
    }

    // Submit new post
    static void submitPost(String installation_id, Post post, final ModelListener listener) {
        String url = baseURL + "/posts";

        // Prepare parameters
        Map<String, String> postMessage = new HashMap<>();
        postMessage.put("installation_id", installation_id);
        postMessage.put("text", post.text);
        postMessage.put("user_name", post.user_name);
        postMessage.put("user_contact", post.user_contact);
        JSONObject parameters = new JSONObject(postMessage);

        // Make request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onData(true, "添加留言成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onData(false, "添加留言失败");
            }
        });
        SAGlobal.sharedRequestQueue.add(jsonObjectRequest);
    }
}

class Post{
    String user_name;
    String user_contact;
    String user_title;
    String user_department;
    String text;
    String creation_time;
}

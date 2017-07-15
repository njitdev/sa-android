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
import java.util.List;
import java.util.Map;

public class MessageBoardModels {
    private static String baseURL = SAConfig.baseURL + "/app/msgboard/" + SAConfig.schoolIdentifier;

    // Fetch list of posts
    static void fetchList(int page, final ModelListener<List<Post>> listener) {
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, baseURL + "/posts?page=" + page, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject result = response.getJSONObject("result");
                            JSONArray posts = result.getJSONArray("posts");

                            // Generate result
                            ArrayList<Post> list = new ArrayList<>();
                            for (int i = 0; i < posts.length(); i++) {
                                JSONObject post = posts.getJSONObject(i);

                                Post p = new Post();

                                // Required fields
                                p.user_name = post.getString("user_name");
                                p.text = post.getString("text");
                                p.creation_time = post.getString("creation_time");

                                // Optional fields
                                if (post.has("user_title") && !post.isNull("user_title"))
                                    p.user_title = post.getString("user_title");
                                if (post.has("user_department") && !post.isNull("user_title"))
                                    p.user_department = post.getString("user_department");

                                list.add(p);
                            }
                            listener.onData(list, "ok");
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onData(null, "获取留言失败");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onData(null, "获取留言失败");
            }
        });
        SAGlobal.sharedRequestQueue.add(r);
    }

    // Submit new post
    static void submitPost(Post post, final ModelListener<Boolean> listener) {
        // Prepare parameters
        Map<String, String> map = new HashMap<>();
        map.put("installation_id", post.installation_id);
        map.put("text", post.text);
        map.put("user_name", post.user_name);
        map.put("user_contact", post.user_contact);
        JSONObject params = new JSONObject(map);

        // Make request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, baseURL + "/posts", params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onData(true, "发送留言成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onData(false, "发送留言失败");
            }
        });
        SAGlobal.sharedRequestQueue.add(jsonObjectRequest);
    }
}

class Post {
    String installation_id;
    String user_name;
    String user_contact;
    String user_title;
    String user_department;
    String text;
    String creation_time;
}

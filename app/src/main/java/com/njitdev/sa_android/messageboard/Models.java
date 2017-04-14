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

/**
 * Created by WZ on 4/9/17.
 */

public class Models {

    private static  String baseURL = SAConfig.baseURL;

  static void fetchList(final ModelListener listener) {

        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, baseURL + "/app/msgboard/posts?page=0",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Thread.sleep(1000);

                    JSONObject result = response.getJSONObject("result");
                    JSONArray posts = result.getJSONArray("posts");

                    ArrayList<Post> list = new ArrayList<>();
                    for(int i=0;i<posts.length();i++){
                        JSONObject post = posts.getJSONObject(i);

                        Post p = new Post();
//                        p.id = post.getInt("_id");
                     //   p.user_name = post.getString("user_name");
                        p.text = post.getString("text");
                     //   p.creation_time = post.getString("creation_time");
                        list.add(p);
                    }

                    listener.onData(true,list);

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onData(false, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onData(false, null);
            }
        });

        SAGlobal.getInstance().sharedRequestQueue.add(r);

  }

}

class Post{
    int id;
    String user_name;
    String user_title;
    String user_department;
    String text;
    String creation_time;
}
package com.njitdev.sa_android.schoolAnnouncement;

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
 * Created by WZ on 5/28/17.
 */

class AnnouncementModels {
    private static String baseURL = SAConfig.baseURL + "/school/" + SAConfig.schoolIdentifier + "/announcements";

    static void fetchList(int category, final ModelListener listener) {
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, baseURL + "?category=" + category,  null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray result = response.getJSONArray("result");

                    ArrayList<Article> list = new ArrayList<>();
                    for(int i = 0; i < result.length(); i++){
                        JSONObject object = result.getJSONObject(i);

                        Article article = new Article();

                        // Required fields
                        article.article_id = object.getString("article_id");
                        article.article_title = object.getString("article_title");

                        // Optional fields
                        if (object.has("article_department")) article.article_department = object.getString("article_department");
                        if (object.has("article_date")) article.article_date = object.getString("article_date");

                        list.add(article);
                    }
                    listener.onData(list, "ok");
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onData(null, "获取通知信息失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onData(null, "获取通知信息失败");
            }
        });
        SAGlobal.sharedRequestQueue.add(r);
    }
}

class Article {
    String article_id;
    String article_title;
    String article_department;
    String article_date;
}

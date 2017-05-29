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

public class AnnouncementModels {
    private static String SAnnouncementURL = SAConfig.SAnnouncementURL;

    static void fetchList(final ModelListener listener, int category) {
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET,
                SAnnouncementURL + category,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray result = response.getJSONArray("result");

                    ArrayList<Article> list = new ArrayList<>();
                    for(int i = 0; i < result.length(); i++){
                        JSONObject post = result.getJSONObject(i);

                        Article article = new Article();
                        article.article_title = post.getString("article_title");
                        article.article_department = post.getString("article_department");
                        list.add(article);
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

class Article{
    String article_title;
    String article_department;

    public String getArticle_title() {
        return article_title;
    }

    public String getArticle_department() {
        return article_department;
    }
}

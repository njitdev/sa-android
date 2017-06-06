package com.njitdev.sa_android.school_announcement;

import android.app.DownloadManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.njitdev.sa_android.utils.ModelListener;
import com.njitdev.sa_android.utils.SAConfig;
import com.njitdev.sa_android.utils.SAGlobal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by WZ on 6/5/17.
 */

public class AnnouncementArticleModels {

    private static String baseURL = SAConfig.baseURL + "/school/" + SAConfig.schoolIdentifier + "/announcements/article";

    static void fetchList(int articleID, final ModelListener listener) {
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, baseURL + "?article_id=" + articleID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject article = response.getJSONObject("result");
                            String article_body = article.getString("article_body");
                            listener.onData(article_body,"ok");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onData(null,"fail");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onData(null,"onErrorListenerfail");
            }
        });
        SAGlobal.sharedRequestQueue.add(r);
    }

}

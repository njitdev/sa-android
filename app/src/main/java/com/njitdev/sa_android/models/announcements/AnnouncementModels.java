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

package com.njitdev.sa_android.models.announcements;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.njitdev.sa_android.utils.ModelListener;
import com.njitdev.sa_android.utils.SAConfig;
import com.njitdev.sa_android.utils.SAGlobal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnnouncementModels {
    private static String baseURL = SAConfig.baseURL + "/school/" + SAConfig.schoolIdentifier + "/announcements";
    private static String articleURL = baseURL + "/article";

    public static void fetchArticleList(int category, final ModelListener<ArrayList<Article>> listener) {
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, baseURL + "?category=" + category, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray result = response.getJSONArray("result");
                            ArrayList<Article> list = new ArrayList<>();

                            for (int i = 0; i < result.length(); i++) {
                                JSONObject object = result.getJSONObject(i);

                                Article article = new Article();

                                // Required fields
                                article.article_id = object.getString("article_id");
                                article.article_title = object.getString("article_title");

                                // Optional fields
                                if (object.has("article_department"))
                                    article.article_department = object.getString("article_department");
                                if (object.has("article_date"))
                                    article.article_date = object.getString("article_date");

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

    public static void fetchArticleBody(String articleID, final ModelListener<String> listener) {
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, articleURL + "?article_id=" + articleID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject article = response.getJSONObject("result");
                            String article_body = article.getString("article_body");
                            listener.onData(article_body, "ok");
                        } catch (JSONException e) {
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


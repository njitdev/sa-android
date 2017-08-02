package com.njitdev.sa_android.class_schedule;

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
 * Created by WZ on 8/1/17.
 */

public class ClassScheduleModels {
    private static String baseURL = SAConfig.baseURL + "/school/" + SAConfig.schoolIdentifier;
    private static int currentWeek;

    //getter() for return currentWeek
    public int getCurrentWeek(){
        return currentWeek;
    }


    // Fetch Syllabus
    static void fetchList(int sessionID, final ModelListener<ArrayList<ArrayList<Syllabus>>> listener) {
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET,
                baseURL + "/class/term?session_id=" + sessionID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    //override onResponseMethod
                    public void onResponse(JSONObject response) {
                        //deserilizing jsonObject
                        try {
                            JSONObject result = response.getJSONObject("result");
                            currentWeek = result.getInt("current_week");
                            JSONArray classes = result.getJSONArray("classes");

                            // populate result
                            ArrayList<ArrayList<Syllabus>> classesList = new ArrayList<>();
                            for(int j = 0; j < classes.length(); j++){
                                ArrayList<Syllabus> classesDayList = new ArrayList<>();
                                JSONArray classesArrayForWeek = classes.getJSONArray(j);
                                for(int i = 0; i < classesArrayForWeek.length(); i++){
                                    JSONObject classesForDay = classesArrayForWeek.getJSONObject(i);
                                    Syllabus syllabus = new Syllabus();

                                    syllabus.week = classesForDay.getInt("week");
                                    syllabus.day_of_week = classesForDay.getInt("day_of_week");
                                    syllabus.classes_in_day = classesForDay.getString("classes_in_day");
                                    syllabus.title = classesForDay.getString("title");
                                    syllabus.instructor = classesForDay.getString("instructor");
                                    syllabus.location = classesForDay.getString("location");
                                    syllabus.type = classesForDay.getString("type");

                                    classesDayList.add(syllabus);
                                }
                                classesList.add(classesDayList);
                            }

                            listener.onData(classesList, "ok");
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onData(null, "数据错误，获取课程表失败");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onData(null, "网络错误，获取课程表失败");
            }
        });
        SAGlobal.sharedRequestQueue.add(r);
    }
}

class Syllabus {
    int week;
    int day_of_week;
    String classes_in_day;
    String title;
    String instructor;
    String location;
    String type;
}

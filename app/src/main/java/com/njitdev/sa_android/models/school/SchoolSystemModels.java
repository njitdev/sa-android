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

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.njitdev.sa_android.utils.ModelListener;
import com.njitdev.sa_android.utils.SAConfig;
import com.njitdev.sa_android.utils.SAGlobal;
import com.njitdev.sa_android.utils.SAUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolSystemModels {
    private static String baseURL = SAConfig.baseURL + "/school/" + SAConfig.schoolIdentifier;

    // Authentication - initialization
    public static void authInit(final ModelListener<AuthInitInfo> listener) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, baseURL + "/auth/init", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("result");

                    AuthInitInfo authInitInfo = new AuthInitInfo();
                    authInitInfo.session_id = result.getString("session_id");
                    authInitInfo.captcha_enabled = result.getBoolean("captcha_enabled");

                    listener.onData(authInitInfo, "ok");
                } catch (JSONException e) {
                    listener.onData(null, "教务系统返回非法数据");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onData(null, "教务系统太烂无法通信，请稍后再试");
            }
        });
        SAGlobal.sharedRequestQueue.add(req);
    }

    // Authentication - captcha
    public static void authCaptcha(String session_id, final ModelListener<Bitmap> listener) {
        // Build GET URL
        // TODO: Find a better way
        String url = baseURL + "/auth/captcha?session_id=";
        try {
            url += URLEncoder.encode(session_id, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create ImageRequest, which automatically decodes image data
        ImageRequest req = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                listener.onData(response, "ok");
            }
        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_4444, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onData(null, "获取验证码失败 :( 请重试几次");
            }
        });
        SAGlobal.sharedRequestQueue.add(req);
    }

    // Authentication - submit
    public static void authSubmit(String installation_id, String session_id, String student_login, String student_password, String captcha, final ModelListener<AuthResult> listener) {
        // Prepare parameters
        Map<String, String> map = new HashMap<>();
        map.put("installation_id", installation_id);
        map.put("session_id", session_id);
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
                        // Handle session_id
                        String session_id = null;
                        if (!result.isNull("session_id")) {
                            session_id = result.getString("session_id");
                        }
                        listener.onData(new AuthResult(true, session_id), "ok");
                    } else {
                        listener.onData(new AuthResult(false, null), "登录失败，可能因为输入不正确，或教务系统无法连接");
                    }
                } catch (JSONException e) {
                    listener.onData(new AuthResult(false, null), "教务系统返回非法数据");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onData(new AuthResult(false, null), "教务系统太烂无法通信，请稍后再试");
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
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                        basicInfo.student_enroll_year = result.getString("student_enroll_year");

                    if (result.has("student_major") && !result.isNull("student_major"))
                        basicInfo.student_major = result.getString("student_major");

                    if (result.has("student_class") && !result.isNull("student_class"))
                        basicInfo.student_class = result.getString("student_class");

                    listener.onData(basicInfo, "ok");
                } catch (JSONException e) {
                    listener.onData(null, "数据解析失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onData(null, "学校服务器太破，连接失败。请尝试重新登录~");
            }
        });
        SAGlobal.sharedRequestQueue.add(req);
    }

    // Fetch ClassSchedule
    public static void fetchClassSchedule(String session_id, final ModelListener<List<List<ClassSchedule>>> listener) {
        // Build GET URL
        String url = baseURL + "/class/term?session_id=";
        try {
            url += URLEncoder.encode(session_id, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Deserialize JSON
                try {
                    JSONObject jsonResult = response.getJSONObject("result");
                    JSONArray jsonClasses = jsonResult.getJSONArray("classes");

                    // TODO: find a better way to store current week number
                    SAGlobal.dataClassStartReferenceWeek = jsonResult.getInt("start_reference_week");

                    // Populate result
                    List<List<ClassSchedule>> classes = new ArrayList<>();

                    // Loop through all weeks
                    for (int j = 0; j < jsonClasses.length(); j++) {
                        List<ClassSchedule> classesOfWeek = new ArrayList<>();
                        JSONArray jsonClassesOfWeek = jsonClasses.getJSONArray(j);

                        for (int i = 0; i < jsonClassesOfWeek.length(); i++) {
                            JSONObject jsonClassSchedule = jsonClassesOfWeek.getJSONObject(i);
                            ClassSchedule classSchedule = new ClassSchedule();
                            classSchedule.week = jsonClassSchedule.getInt("week");
                            classSchedule.day_of_week = jsonClassSchedule.getInt("day_of_week");
                            classSchedule.classes_in_day = jsonClassSchedule.getString("classes_in_day");
                            classSchedule.title = jsonClassSchedule.getString("title");
                            classSchedule.instructor = jsonClassSchedule.getString("instructor");
                            classSchedule.location = jsonClassSchedule.getString("location");

                            // Optional fields
                            if (jsonClassSchedule.has("type") || !jsonClassSchedule.isNull("type"))
                                classSchedule.type = jsonClassSchedule.getString("type");

                            classesOfWeek.add(classSchedule);
                        }

                        // Sort classes by sections in day
                        Collections.sort(classesOfWeek, new Comparator<ClassSchedule>() {
                            @Override
                            public int compare(ClassSchedule c1, ClassSchedule c2) {
                                return c1.classes_in_day.compareTo(c2.classes_in_day);
                            }
                        });

                        classes.add(classesOfWeek);
                    }
                    listener.onData(classes, "ok");
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onData(null, "数据错误，获取课程表失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onData(null, "学校服务器太破，连接失败。请尝试重新登录~");
            }
        });
        SAGlobal.sharedRequestQueue.add(r);
    }

    public static int safeCurrentWeek(int start_reference_week, int week_array_size) {
        if (start_reference_week == 0) {
            return 0;
        }

        int current_week = SAUtils.currentReferenceWeek() - start_reference_week + 1;
        if (current_week < 1 || current_week >= week_array_size) {
            current_week = 0;
        }
        return current_week;
    }

    // Fetch grades
    public static void fetchGrades(String session_id, final ModelListener<List<GradeItem>> listener) {
        // Build GET URL
        String url = baseURL + "/grades?session_id=";
        try {
            url += URLEncoder.encode(session_id, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Make request
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<GradeItem> result = new ArrayList<>();
                try {
                    JSONArray jsonGrades = response.getJSONArray("result");

                    // Loop for all courses
                    for (int i = 0; i < jsonGrades.length(); i++) {
                        JSONObject jsonGrade = jsonGrades.getJSONObject(i);
                        GradeItem gradeItem = new GradeItem();

                        gradeItem.course_name = jsonGrade.getString("course_name");

                        // Optional fields
                        if (jsonGrade.has("course_id") && !jsonGrade.isNull("course_id"))
                            gradeItem.course_id = jsonGrade.getString("course_id");
                        if (jsonGrade.has("score") && !jsonGrade.isNull("score"))
                            gradeItem.score = jsonGrade.getString("score");
                        if (jsonGrade.has("course_category") && !jsonGrade.isNull("course_category"))
                            gradeItem.course_category = jsonGrade.getString("course_category");
                        if (jsonGrade.has("course_isrequired") && !jsonGrade.isNull("course_isrequired"))
                            gradeItem.course_isrequired = jsonGrade.getString("course_isrequired");
                        if (jsonGrade.has("course_hours") && !jsonGrade.isNull("course_hours"))
                            gradeItem.course_hours = jsonGrade.getString("course_hours");
                        if (jsonGrade.has("term") && !jsonGrade.isNull("term"))
                            gradeItem.term = jsonGrade.getString("term");
                        if (jsonGrade.has("credits") && !jsonGrade.isNull("credits"))
                            gradeItem.credits = jsonGrade.getString("credits");
                        if (jsonGrade.has("grade_point") && !jsonGrade.isNull("grade_point"))
                            gradeItem.grade_point = jsonGrade.getString("grade_point");
                        if (jsonGrade.has("exam_type") && !jsonGrade.isNull("exam_type"))
                            gradeItem.exam_type = jsonGrade.getString("exam_type");

                        result.add(gradeItem);
                    }
                    listener.onData(result, "OK");
                } catch (JSONException e) {
                    listener.onData(null, "解析数据失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onData(null, "学校服务器太破，连接失败。请尝试重新登录~");
            }
        });
        SAGlobal.sharedRequestQueue.add(req);
    }
}

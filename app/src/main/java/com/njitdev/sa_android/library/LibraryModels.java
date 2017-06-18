package com.njitdev.sa_android.library;

import android.telecom.Call;

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

/**
 * Created by meiyan on 6/10/17.
 */

class LibraryModels {
    private static String baseURL = SAConfig.baseURL + "/library/" + SAConfig.schoolIdentifier;

    public static void search(String keyword, final ModelListener listener) {
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, baseURL + "/search?keyword=" + keyword, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<Book> result = new ArrayList<>();
                try {
                    JSONArray books = response.getJSONArray("result");
                    for (int i = 0; i < books.length(); i++) {
                        JSONObject jsonBook = books.getJSONObject(i);
                        Book book = new Book();
                        book.id = jsonBook.getString("id");
                        book.title = jsonBook.getString("title");

                        if (jsonBook.has("author") && !jsonBook.isNull("author")) book.author = jsonBook.getString("author");
                        if (jsonBook.has("publisher") && !jsonBook.isNull("publisher")) book.publisher = jsonBook.getString("publisher");
                        if (jsonBook.has("year") && !jsonBook.isNull("year")) book.year = jsonBook.getString("year");
                        if (jsonBook.has("acquisition_number") && !jsonBook.isNull("acquisition_number")) book.acquisition_number = jsonBook.getString("acquisition_number");
                        if (jsonBook.has("inventory") && !jsonBook.isNull("inventory")) book.inventory = jsonBook.getString("inventory");
                        if (jsonBook.has("available") && !jsonBook.isNull("available")) book.available = jsonBook.getString("available");
                        result.add(book);
                    }
                    listener.onData(result, "OK");
                } catch (JSONException e) {
                    listener.onData(null, "解析数据失败");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onData(null, "连接学校服务器超时");
            }
        });
        SAGlobal.sharedRequestQueue.add(r);
    }

    public static void details(String bookId, final ModelListener listener) {
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, baseURL + "/details?book_id=" +bookId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Parse response here
                ArrayList<BookInventory> result = new ArrayList<>();
                try {
                    JSONArray bookInventorys = response.getJSONObject("result").getJSONArray("inventory");
                    for (int i = 0; i < bookInventorys.length(); i++) {
                        JSONObject jsonDetails = bookInventorys.getJSONObject(i);
                        BookInventory bookInventory = new BookInventory();

                        if (jsonDetails.has("location") && !jsonDetails.isNull("location")) bookInventory.location = jsonDetails.getString("location");
                        if (jsonDetails.has("login_number") && !jsonDetails.isNull("login_number")) bookInventory.login_number = jsonDetails.getString("login_number");
                        if (jsonDetails.has("year") && !jsonDetails.isNull("year")) bookInventory.year = jsonDetails.getString("year");
                        if (jsonDetails.has("acquisition_number") && !jsonDetails.isNull("acquisition_number")) bookInventory.acquisition_number = jsonDetails.getString("acquisition_number");
                        if (jsonDetails.has("type") && !jsonDetails.isNull("type")) bookInventory.type = jsonDetails.getString("type");
                        if (jsonDetails.has("inventory") && !jsonDetails.isNull("inventory")) bookInventory.inventory = jsonDetails.getString("inventory");
                        if (jsonDetails.has("availability") && !jsonDetails.isNull("availability")) bookInventory.availability = jsonDetails.getString("availability");
                        result.add(bookInventory);
                    }
                    listener.onData(result, "OK");
                } catch (JSONException e) {
                    listener.onData(null, "解析数据失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onData(null, "连接学校服务器超时");
            }
        });
        SAGlobal.sharedRequestQueue.add(r);
    }
}

class Book {
    String id;
    String title;
    String author;
    String publisher;
    String year;
    String acquisition_number;
    String inventory;
    String available;
}

class BookInventory {
    String location;
    String login_number;
    String year;
    String acquisition_number;
    String type;
    String inventory;
    String availability;
}

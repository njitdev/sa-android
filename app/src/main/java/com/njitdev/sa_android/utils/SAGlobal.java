package com.njitdev.sa_android.utils;

import com.android.volley.RequestQueue;

/**
 * Created by WZ on 4/9/17.
 */

public class SAGlobal {
    private static final SAGlobal ourInstance = new SAGlobal();
    public RequestQueue sharedRequestQueue;
    public static SAGlobal getInstance() {
        return ourInstance;
    }
    private SAGlobal() {}
}

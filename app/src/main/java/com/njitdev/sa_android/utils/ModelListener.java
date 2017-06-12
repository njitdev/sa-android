package com.njitdev.sa_android.utils;


/**
 * Created by WZ on 4/9/17.
 */

public interface ModelListener<T> {
    void onData(T result, String message);
}

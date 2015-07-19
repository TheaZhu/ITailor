package com.thea.itailor.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.HttpEntity;

/**
 * Created by hunter on 2015/6/24.
 */
public class HttpUtil {
    private static final String BASE_URL = "http://192.168.0.102:8080";
    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setTimeout(10000);
    }

    public static AsyncHttpClient getClient() {
        return client;
    }

    public static void get(Context context, String urlStr, AsyncHttpResponseHandler handler) {
        client.get(context, urlStr, handler);
    }

    public static void get(Context context, String urlStr, RequestParams params,
        AsyncHttpResponseHandler handler) {
        client.get(context, urlStr, params, handler);
    }

    public static void get(Context context, String urlStr, JsonHttpResponseHandler handler) {
        client.get(context, urlStr, handler);
    }

    public static void get(Context context, String urlStr, RequestParams params,
        JsonHttpResponseHandler handler) {
        client.get(context, urlStr, params, handler);
    }

    public static void get(Context context, String urlStr, BinaryHttpResponseHandler handler) {
        client.get(context, urlStr, handler);
    }

    public static void get(Context context, String urlStr, RequestParams params,
        BinaryHttpResponseHandler handler) {
        client.get(context, urlStr, params, handler);
    }

    public static void post(Context context, String urlStr, HttpEntity entity,
        String contentType, ResponseHandlerInterface handlerInterface) {
        client.post(context, urlStr, entity, contentType, handlerInterface);
    }

    public static void post(Context context, String urlStr, RequestParams params,
        ResponseHandlerInterface handlerInterface) {
        client.post(context, urlStr, params, handlerInterface);
    }
}

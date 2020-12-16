package com.example.coolweather.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @Auther: mada
 * @Date: 2020/12/16 19:27
 * @Description:
 */
public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Log.d("TAG", "sendOkHttpRequest: "+address);
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}

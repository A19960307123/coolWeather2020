package com.example.coolweather.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.coolweather.R;

import okhttp3.Request;
import okhttp3.RequestBody;

public class MyTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test);

    }
    public static void start(Context context) {
        Intent starter = new Intent(context, MyTestActivity.class);
        context.startActivity(starter);
        int i=10;
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();
        RequestBody body = request.body();
    }
}
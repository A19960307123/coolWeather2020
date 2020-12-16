package com.example.coolweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.coolweather.ui.activity.MyTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MyTestActivity.start(this);
    }
}
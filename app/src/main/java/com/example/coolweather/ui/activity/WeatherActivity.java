package com.example.coolweather.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coolweather.R;
import com.example.coolweather.gson.DaysWeather;
import com.example.coolweather.gson.LifeSuggestion;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView humidityText;

    private TextView aqiLevelTextView;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        humidityText = findViewById(R.id.aqi_humidity);
        comfortText = findViewById(R.id.comfort_text);
        aqiLevelTextView = findViewById(R.id.aqi_level);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        bingPicImg =findViewById(R.id.bing_pic_img);
        mContext = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String weatherString = prefs.getString("weather", null);

        String bing_pic = prefs.getString("bing_pic", null);
        if (bing_pic != null){
            Glide.with(mContext).load(bing_pic).into(bingPicImg);
        }else {
            loadBingPic();
        }

//        if (weatherString != null) {
//            //如果有缓存直接解析天气数据
//            Weather weather = Utility.handleWeatherResponse(weatherString);
//            showWeatherInfo(weather);
//        } else {
//            //无缓存区服务器查询天气
//            String weatherId = getIntent().getStringExtra("weather_id");
//            weatherLayout.setVisibility(View.INVISIBLE);
//            requestWeather(weatherId);
//        }
        String weatherId = getIntent().getStringExtra("weather_id");
        weatherLayout.setVisibility(View.INVISIBLE);
        requestWeather(weatherId);

    }

    //加载bing每日一图
    private void loadBingPic() {
        String requestBingPic = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray images = jsonObject.getJSONArray("images");
                    final String address  = "https://www.bing.com/"+images.getJSONObject(0).getString("url");
                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                    edit.putString("bing_pic", address);
                    edit.apply();
                    runOnUiThread(() -> {
                        Glide.with(mContext).load(address).into(bingPicImg);
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    /**
     * 功能描述:
     * 根据城市id请求城市天气信息
     */
    private void requestWeather(String weatherId) {
        String cityId = weatherId.substring(2);
        String weatherUrl = "https://tianqiapi.com/api?version=v1&appid=83719921&appsecret=eilpb1IT&cityid="+cityId;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(() -> {
                    if (weather != null) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                        editor.putString("weather", responseText);
                        editor.apply();
                        showWeatherInfo(weather);
                    } else {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.city;
        String updateTime = weather.update_time.split(" ")[1];
        List<DaysWeather> daysWeatherList = weather.daysWeatherList;
        DaysWeather todayWeather = daysWeatherList.get(0);
        String degree = daysWeatherList.get(0).tem;
        String weatherInfo = daysWeatherList.get(0).wea;
        List<LifeSuggestion> suggestions = todayWeather.lifeSuggestions;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        //设置未来七天的天气
        for (DaysWeather daysWeather:daysWeatherList ){
            View view = LayoutInflater.from(forecastLayout.getContext()).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(daysWeather.date);
            infoText.setText(daysWeather.wea);
            maxText.setText(daysWeather.temMax);
            minText.setText(daysWeather.temMin);
            forecastLayout.addView(view);
        }
        //设置空气质量情况
        if (todayWeather.air!= null){
            aqiText.setText(todayWeather.air);
            aqiLevelTextView.setText(todayWeather.air_level);
            humidityText.setText(todayWeather.humidity);
        }
        for (LifeSuggestion lifeSuggestion:suggestions){
            if ("运动指数".equals(lifeSuggestion.title)){
                sportText.setText("运动建议： " +lifeSuggestion.desc);
            }else if ("洗车指数".equals(lifeSuggestion.title)){
                carWashText.setText("洗车建议： "+lifeSuggestion.desc);
            }else if ("穿衣指数".equals(lifeSuggestion.title)){
                comfortText.setText("穿衣建议： "+lifeSuggestion.desc);
            }
        }
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
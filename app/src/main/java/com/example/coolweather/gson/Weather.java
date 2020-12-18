package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Auther: mada
 * @Date: 2020/12/17 10:33
 * @Description:
 */
public class Weather {

    public String update_time;

    public String city;

    @SerializedName("cityid")
    public String cityId;

    @SerializedName("data")
    public List<DaysWeather> daysWeatherList;

}

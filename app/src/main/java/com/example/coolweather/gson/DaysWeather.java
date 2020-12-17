package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Auther: mada
 * @Date: 2020/12/17 10:34
 * @Description:
 */
public class DaysWeather {

    @SerializedName("date")
    public String date;

    public String week;

    //天气
    public String wea;

    //实时温度
    public String tem;

    //最高温度
    @SerializedName("tem1")
    public String temMax;

    //最低温度
    @SerializedName("tem2")
    public String temMin;

    //空气质量
    public String air;

    //湿度
    public String humidity;

    //空气质量品级
    public String air_level;

    //空气质量描述
    public String air_tips;

    @SerializedName("index")
    public List<LifeSuggestion> lifeSuggestions;
}

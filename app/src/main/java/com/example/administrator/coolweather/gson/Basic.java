package com.example.administrator.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/7/2.
 */

public class Basic {
    @SerializedName("city")
    public String cityname;
    @SerializedName("id")
    public String weatherId;
    @SerializedName("update") public Update update;
    public class Update{
        @SerializedName("loc") public String updatetime;
    }
}

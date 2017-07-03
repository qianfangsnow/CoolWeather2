package com.example.administrator.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/7/2.
 */

public class Forecast {
    @SerializedName("date") public String date;
    @SerializedName("tmp")public Temperature temperature;
    @SerializedName("cond")public  More more;
    public class Temperature {
        @SerializedName("max") public String max;
        @SerializedName("min") public String min;
    }

    public class More {
        @SerializedName("txt_d")public String info;
    }
}

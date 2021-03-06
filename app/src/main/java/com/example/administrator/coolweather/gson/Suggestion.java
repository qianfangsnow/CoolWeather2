package com.example.administrator.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/7/2.
 */

public class Suggestion {
    @SerializedName("comf")public Comfort comfort;
    @SerializedName("cw")public CarWash carWash;
    @SerializedName("sprot") public Sport sport;
    public class Comfort {
        @SerializedName("txt")public String info;
    }

    public class CarWash {
        @SerializedName("txt")public String info;
    }

    public class Sport {
        @SerializedName("txt")public String info;
    }
}

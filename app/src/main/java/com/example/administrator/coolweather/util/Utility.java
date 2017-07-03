package com.example.administrator.coolweather.util;

import android.text.TextUtils;

import com.example.administrator.coolweather.db.City;
import com.example.administrator.coolweather.db.County;
import com.example.administrator.coolweather.db.Province;
import com.example.administrator.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/29.
 */

public class Utility {
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces=new JSONArray(response);
                for (int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject=allProvinces.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return  true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities=new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject cityObject=allCities.getJSONObject(i);
                    City city=new City();
                    city.setcityName(cityObject.getString("name"));
                    city.setcityCode(cityObject.getInt("code"));
                    city.setId(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCountryResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCountry=new JSONArray(response);
                for(int i=0;i<allCountry.length();i++){
                    JSONObject countryObject=allCountry.getJSONObject(i);
                    County county=new County();
                    county.setCountryName(countryObject.getString("name"));
                    county.setWeatherId(countryObject.getString("weather_Id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

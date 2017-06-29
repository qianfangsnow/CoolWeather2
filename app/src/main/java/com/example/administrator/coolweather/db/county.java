package com.example.administrator.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/6/29.
 */

public class county extends DataSupport {
    private  int id;
    private String countryName;
    private String weatherId;
    private int cityId;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.cityId=id;
    }
    public String getCountryName(){
        return countryName;
    }
    public void setCountryName(String countryName){
        this.countryName=countryName;
    }
    public String getWeatherId(){
        return weatherId;
    }
    public void setCityId(int cityId){
        this.cityId=cityId;
    }
    public int getCityId(){
        return cityId;
    }

}

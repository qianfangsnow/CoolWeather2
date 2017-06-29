package com.example.administrator.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/6/29.
 */

public class City extends DataSupport {
    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String cityName(){
        return cityName;
    }
    public void setcityName(String cityName){
        this.cityName=cityName;
    }
    public int getcityCode(){
        return cityCode;
    }
    public void setcityCode(int cityCode){
        this.cityCode=cityCode;
    }
    public int getprinceId(){
        return provinceId;
    }
    public void setProvinceId(int provinceId){
        this.provinceId=provinceId;
    }
}

package com.example.test2o4.gson;

import com.google.gson.annotations.SerializedName;

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond_txt")
    public String cloudamount;

    public String getWind_dir() {
        return wind_dir;
    }

    public void setWind_dir(String wind_dir) {
        this.wind_dir = wind_dir;
    }

    public String wind_dir;

    public String getWind_spd() {
        return wind_spd;
    }

    public void setWind_spd(String wind_spd) {
        this.wind_spd = wind_spd;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public String wind_spd;

    public String hum;

    public String vis;

    public String getPres() {
        return pres;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public String pres;

    public String getCloudamount() {
        return cloudamount;
    }

    public void setCloudamount(String cloud) {
        this.cloudamount = cloud;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTmp(String tmp) {
        this.temperature=temperature;
    }
}
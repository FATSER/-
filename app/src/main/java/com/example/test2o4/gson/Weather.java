package com.example.test2o4.gson;

import java.util.List;

public class Weather {

    public Basic basic;
    public Now now;
    public String status;



    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public Basic getBasic() {
        return basic;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Now getNow() {
        return now;
    }

    public void setNow(Now now) {
        this.now = now;
    }

}

package com.ygs.wheather.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IP {
    @SerializedName("ip")
    @Expose
    String ip;

    public String getIP() {
        return ip;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return ip;
    }
    public IP(String ip){
        this.ip = ip;
    }
    public IP(){

    }
}

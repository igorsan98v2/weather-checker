package com.ygs.weather.server.common;

public class Request {
    private String ip;
    private String username;
    public Request(){}
    public Request(String username){
        this.username =username;
    }
    public Request(String ip,String username){
        this.ip =ip;
        this.username = username;
    }
    public String getUsername(){return username;}
    public String getIP() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

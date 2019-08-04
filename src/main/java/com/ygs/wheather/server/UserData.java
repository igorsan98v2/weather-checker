package com.ygs.wheather.server;

import com.ygs.wheather.common.Location;
import com.ygs.wheather.common.Weather;

import java.util.HashMap;
import java.util.Map;

public class UserData {
    //private int id;
    private String ip;
    private Weather  wheather;
    private Location location;
    public  UserData(String ip){
        this.ip = ip;
        if(ip.contains("127.0.0.1")){
            location = new Location(48.7231,37.5563,"fail");//grant LatLong coord

        }
        else {
            location = LocationParser.getLocation(ip);
            if(location.getStatus().equals("fail")){
                location.setLat(48.7231);
                location.setLon(37.5563);
            }

        }
        wheather = WeatherParser.getWeather(location.getLat(),location.getLon());
    }



    public void setWheather(Weather wheather) {
        this.wheather = wheather;
    }

    public Weather getWheather() {
        return wheather;
    }
    public  String getIp(){
        return ip;
    }

    public Location getLocation() {
        return location;
    }
    /*
    public Location getLocation() {
        return location;
    }

 */
}

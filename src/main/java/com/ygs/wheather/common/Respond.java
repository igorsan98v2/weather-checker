package com.ygs.wheather.common;

public class Respond {
    private String username;
    private Weather weather;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public Weather getWeather(){
        return weather;
    }
    public void setWeather(Weather weather){
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "it is data of "+username;
    }
    public  Respond(){

    }
    public Respond(String username,Weather weather){
        this.username = username;
        this.weather = weather;
    }
}

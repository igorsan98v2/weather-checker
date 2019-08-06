package com.ygs.weather.server;

import java.time.LocalTime;


public class Respond {
    private String username;
    private Weather weather;
    private String serverTime;
    public String getUsername() {
        return username;
    }

    public void setUsername(
            String username) {
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
        double temp = weather.getHourly().getData().get(0).getTemperature();
        double pressure = weather.getHourly().getData().get(0).getPressure();
        double windSpeed = weather.getHourly().getData().get(0).getWindSpeed();
        String total = weather.getHourly().getSummary();

        String res = String.format("\nWeather params:"+
                "\nSummary weat1her look like this: %s"+
                "\ntemperature is %.2f C" +
                "\npressure is %.2f hPa"+
                "\nwind speed is %.2f m/s" +
                        "\nServer time: %s\n",
                total,temp,pressure,windSpeed,serverTime);

        return res;
    }
    public  Respond(){

    }
    public Respond(String username,Weather weather){
        this.username = username;
        this.weather = weather;

        serverTime = LocalTime.now().withNano(0).toString();

    }

    public void setTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getTime() {
        return serverTime;
    }
}

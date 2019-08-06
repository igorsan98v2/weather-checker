package com.ygs.weather.server.common;

public class PingPong {
    private long ping;
    private long pong;
    private String username;
    public PingPong() {

    }
    public PingPong(String username){
        this.username = username;
        setPing();
    }
    public long calcDelay(){
        return pong-ping;
    }
    public void setPing(){
        ping = System.currentTimeMillis();
    }
    public void setPong(){
        pong = System.currentTimeMillis();
    }
    public void setPing(long ping) {
        this.ping = ping;
    }

    public void setPong(long pong) {
        this.pong = pong;
    }

    public long getPing() {
        return ping;
    }

    public long getPong() {
        return pong;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

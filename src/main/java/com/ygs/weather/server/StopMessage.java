package com.ygs.weather.server;

public class StopMessage {
   private String message;

    public StopMessage(){

    }
    public StopMessage(String message){
        this.message = message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return  message;
    }
}

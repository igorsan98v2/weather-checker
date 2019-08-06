package com.ygs.weather.client;

import com.ygs.weather.server.common.IP;
import retrofit2.Call;
import retrofit2.http.GET;


public interface IP_Service {

    @GET("https://api.ipify.org?format=json")
    Call<IP> getIP();
}

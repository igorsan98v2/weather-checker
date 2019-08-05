package com.ygs.wheather.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ygs.wheather.common.IP;
import com.ygs.wheather.common.Location;
import com.ygs.wheather.server.LocationService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class IP_Parser {
    private  IP_Service ipService;

    public IP_Parser() {
        Gson gson = new GsonBuilder().create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://api.ipify.org?format=json")
                .client(client.build())
                .build();
        ipService = retrofit.create(IP_Service.class);

    }
    public IP getIP(){
        IP ip=null;
        try {
            Response<IP> response = ipService.getIP().execute();
            ip=response.body();
            response.message();
            // System.out.println(response.body().getTimezone());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return  ip;
    }
}

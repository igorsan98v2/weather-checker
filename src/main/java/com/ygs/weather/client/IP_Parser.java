package com.ygs.weather.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ygs.weather.server.IP;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class IP_Parser {
    private  IP_Service ipService;

    public IP_Parser() {
        Gson gson = new GsonBuilder().create();

        OkHttpClient.Builder client = new OkHttpClient.Builder();
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
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return  ip;
    }
}

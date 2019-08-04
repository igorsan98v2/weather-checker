package com.ygs.wheather.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ygs.wheather.common.Location;
import com.ygs.wheather.common.Weather;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Map;

public class LocationParser {
    private static LocationService locationService;

    static {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ip-api.com/json/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client.build())
                .build();
        locationService = retrofit.create(LocationService.class);

    }
    public static Location getLocation(String ip){
        Location location=null;
        try {
            Response<Location> response = locationService.getLocation(ip).execute();
            location=response.body();
            response.message();
            // System.out.println(response.body().getTimezone());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return  location;
    }
}

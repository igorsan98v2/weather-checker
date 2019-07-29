package com.ygs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.io.IOException;
import java.util.Map;

public class WeatherParser {

    public final  String API_KEY ="06f2d25947ba00f83f43f34e6bc23faa";
    private static WeatherService weatherService;


    public WeatherParser(){
        Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
             .create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.darksky.net/forecast/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client.build())
                .build();
        weatherService = retrofit.create(WeatherService.class);

    }
    public Weather getWeather(double lat,double lng,Map<String,String> queryParams){
        Weather weather=null;
        try {
            Response<Weather> response = weatherService.getWeather(API_KEY,lat,lng,queryParams).execute();
            weather=response.body();
             response.message();
      // System.out.println(response.body().getTimezone());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return  weather;
    }

}

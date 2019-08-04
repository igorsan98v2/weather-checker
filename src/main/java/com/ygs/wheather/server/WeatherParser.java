package com.ygs.wheather.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.ygs.wheather.common.Weather;
import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WeatherParser {

    public static final  String API_KEY ="06f2d25947ba00f83f43f34e6bc23faa";
    private static WeatherService weatherService;


    static {
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
    public  static Weather getWeather(double lat, double lng, Map<String,String> queryParams){

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
    public  static Weather getWeather(double lat, double lng){
        Map<String,String> queryParams = new HashMap<String, String>();
        queryParams.put("exclude","currently");
        queryParams.put("lang","ru");
        queryParams.put("units","si");

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

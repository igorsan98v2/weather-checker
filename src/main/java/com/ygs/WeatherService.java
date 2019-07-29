package com.ygs;

import com.ygs.wheather_service.Weather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;


public interface WeatherService {

        @GET("{API_KEY}/{lat},{long}")
        Call<Weather> getWeather(@Path("API_KEY") String api,
                                 @Path("lat") double lat,
                                 @Path("long") double longt,
                                 @QueryMap Map<String,String> queryParams);
}

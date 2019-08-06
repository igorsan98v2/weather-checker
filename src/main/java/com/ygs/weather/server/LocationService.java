package com.ygs.weather.server;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import com.ygs.weather.server.common.Location;

public interface LocationService {

    @GET("{ip}?fields=status,message,city,lat,lon")
    Call<Location> getLocation(@Path("ip") String ip);


}

package com.ygs.wheather.server;

import com.ygs.wheather.common.Weather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import com.ygs.wheather.common.Location;
import java.util.Map;

public interface LocationService {

    @GET("{ip}?fields=status,message,city,lat,lon")
    Call<Location> getLocation(@Path("ip") String ip);


}

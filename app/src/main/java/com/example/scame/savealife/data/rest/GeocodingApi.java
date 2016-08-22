package com.example.scame.savealife.data.rest;


import com.example.scame.savealife.data.entities.GeocodingEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GeocodingApi {

    @GET("maps/api/geocode/json")
    Observable<GeocodingEntity> reverseGeocode(@Query("latlng") String latlng,
                                               @Query("key") String key);
}

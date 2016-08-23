package com.example.scame.savealife.data.rest;

import com.example.scame.savealife.data.entities.DirectionEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface DirectionsApi {

    @GET("maps/api/directions/json")
    Observable<DirectionEntity> computeDirections(@Query("origin") String origin,
                                                  @Query("destination") String destination,
                                                  @Query("key") String key);
}

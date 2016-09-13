package com.example.scame.savealife.data.rest;

import com.example.scame.savealife.data.entities.DestinationEntity;
import com.example.scame.savealife.data.entities.LocationMessageEntity;
import com.example.scame.savealife.data.entities.TokenUpdateEntity;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;

public interface ServerApi {

    @PUT("http://10.0.1.57:8080/rest/user")
    Observable<ResponseBody> tokenUpdateRequest(@Body TokenUpdateEntity tokenEntity);

    @POST("http://10.0.1.57:8080/rest/send/")
    Observable<ResponseBody> sendLocationToServer(@Body LocationMessageEntity locationEntity);

    @POST("http://10.0.1.57:8080/rest/send/")
    Observable<ResponseBody> sendDestination(@Body DestinationEntity destinationEntity);
}

package com.example.scame.savealife.data.repository;


import com.example.scame.savealife.PrivateValues;
import com.example.scame.savealife.SaveAlifeApp;
import com.example.scame.savealife.data.entities.GeocodingEntity;
import com.example.scame.savealife.data.rest.GeocodingApi;

import retrofit2.Retrofit;
import rx.Observable;

public class GeocodingDataManagerImp implements IGeocodingDataManager {

    @Override
    public Observable<GeocodingEntity> getHumanReadableAddress(String latLng) {
        Retrofit retrofit = SaveAlifeApp.getAppComponent().getRetrofit();
        GeocodingApi geocodingApi = retrofit.create(GeocodingApi.class);

        return geocodingApi.reverseGeocode(latLng, PrivateValues.GEOCODING_KEY);
    }
}

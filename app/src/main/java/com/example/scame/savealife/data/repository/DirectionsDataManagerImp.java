package com.example.scame.savealife.data.repository;


import com.example.scame.savealife.PrivateValues;
import com.example.scame.savealife.SaveAlifeApp;
import com.example.scame.savealife.data.entities.DirectionEntity;
import com.example.scame.savealife.data.entities.LatLongPair;
import com.example.scame.savealife.data.rest.DirectionsApi;

import retrofit2.Retrofit;
import rx.Observable;

public class DirectionsDataManagerImp implements IDirectionsDataManager {


    @Override
    public Observable<DirectionEntity> getDirections(LatLongPair origin, LatLongPair destination) {
        Retrofit retrofit = SaveAlifeApp.getAppComponent().getRetrofit();
        DirectionsApi directionsApi = retrofit.create(DirectionsApi.class);

        return directionsApi.computeDirections(origin.getLatitude() + "," + origin.getLongitude(),
                                            destination.getLatitude() + "," + destination.getLongitude(),
                                            PrivateValues.GOOGLE_KEY);
    }
}

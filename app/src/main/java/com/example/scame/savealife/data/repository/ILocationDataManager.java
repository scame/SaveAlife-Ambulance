package com.example.scame.savealife.data.repository;


import com.example.scame.savealife.data.entities.LatLongPair;

import rx.Observable;

public interface ILocationDataManager {

    Observable<LatLongPair> startLocationUpdates();

    void stopLocationUpdates();

    void saveCurrentLocation(LatLongPair latLongPair);
}

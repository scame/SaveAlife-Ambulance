package com.example.scame.savealife.data.repository;


import com.example.scame.savealife.data.entities.GeocodingEntity;

import rx.Observable;

public interface IGeocodingDataManager {

    Observable<GeocodingEntity> getHumanReadableAddress(String latLng);
}

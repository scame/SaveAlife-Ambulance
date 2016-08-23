package com.example.scame.savealife.data.repository;


import com.example.scame.savealife.data.entities.DirectionEntity;
import com.example.scame.savealife.data.entities.LatLongPair;

import rx.Observable;

public interface IDirectionsDataManager {

    Observable<DirectionEntity> getDirections(LatLongPair origin, LatLongPair destination);
}

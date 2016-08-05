package com.example.scame.savealife.data.repository;


import com.example.scame.savealife.data.entities.LatLongPair;
import com.graphhopper.PathWrapper;

import java.util.List;

import rx.Observable;

public interface IMapsDataManager {

    Observable<List<String>> getRemoteAreaList();

    Observable<List<String>> getLocalAreaList();

    Observable<Integer> downloadMap(String downloadUrl);

    Observable<PathWrapper> calculatePath(LatLongPair pair);

    Observable<Void> loadGraphStorage(String path);
}

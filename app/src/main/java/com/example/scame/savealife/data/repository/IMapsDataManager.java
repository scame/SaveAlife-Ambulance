package com.example.scame.savealife.data.repository;


import java.util.List;

import rx.Observable;

public interface IMapsDataManager {

    Observable<List<String>> getRemoteAreaList();

    Observable<List<String>> getLocalAreaList();

}

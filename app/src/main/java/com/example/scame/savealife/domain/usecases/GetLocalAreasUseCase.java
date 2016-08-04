package com.example.scame.savealife.domain.usecases;


import com.example.scame.savealife.data.repository.IMapsDataManager;
import com.example.scame.savealife.domain.schedulers.ObserveOn;
import com.example.scame.savealife.domain.schedulers.SubscribeOn;

import java.util.List;

import rx.Observable;

public class GetLocalAreasUseCase extends UseCase<List<String>> {

    private IMapsDataManager dataManager;

    public GetLocalAreasUseCase(IMapsDataManager dataManager, SubscribeOn subscribeOn,
                                ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.dataManager = dataManager;
    }

    @Override
    protected Observable<List<String>> getUseCaseObservable() {
        return dataManager.getLocalAreaList();
    }
}

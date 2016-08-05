package com.example.scame.savealife.domain.usecases;


import com.example.scame.savealife.data.repository.IMapsDataManager;
import com.example.scame.savealife.domain.schedulers.ObserveOn;
import com.example.scame.savealife.domain.schedulers.SubscribeOn;

import rx.Observable;

public class LoadGraphStorageUseCase extends UseCase<Void> {

    private IMapsDataManager dataManager;

    private String path;

    public LoadGraphStorageUseCase(IMapsDataManager dataManager,
                                   SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(subscribeOn, observeOn);
        this.dataManager = dataManager;
    }

    @Override
    protected Observable<Void> getUseCaseObservable() {
        return dataManager.loadGraphStorage(path);
    }

    public void setPath(String path) {
        this.path = path;
    }
}

package com.example.scame.savealife.domain.usecases;

import com.example.scame.savealife.data.entities.LatLongPair;
import com.example.scame.savealife.data.repository.IMapsDataManager;
import com.example.scame.savealife.domain.schedulers.ObserveOn;
import com.example.scame.savealife.domain.schedulers.SubscribeOn;
import com.graphhopper.PathWrapper;

import rx.Observable;

public class CalculatePathUseCase extends UseCase<PathWrapper> {

    private IMapsDataManager dataManager;

    private LatLongPair pair;

    public CalculatePathUseCase(IMapsDataManager dataManager,
                                SubscribeOn subscribeOn, ObserveOn observeOn) {

        super(subscribeOn, observeOn);
        this.dataManager = dataManager;
    }

    @Override
    protected Observable<PathWrapper> getUseCaseObservable() {
        return dataManager.calculatePath(pair);
    }

    public void setPair(LatLongPair pair) {
        this.pair = pair;
    }
}

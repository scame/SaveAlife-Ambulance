package com.example.scame.savealife.domain.usecases;

import com.example.scame.savealife.data.entities.LatLongPair;
import com.example.scame.savealife.data.mappers.DirectionModelMapper;
import com.example.scame.savealife.data.repository.IDirectionsDataManager;
import com.example.scame.savealife.domain.schedulers.ObserveOn;
import com.example.scame.savealife.domain.schedulers.SubscribeOn;
import com.example.scame.savealife.presentation.models.DirectionModel;

import rx.Observable;

public class ComputeDirectionUseCase extends UseCase<DirectionModel> {

    private IDirectionsDataManager dataManager;

    private LatLongPair origin;
    private LatLongPair destination;

    public ComputeDirectionUseCase(SubscribeOn subscribeOn, ObserveOn observeOn,
                                   IDirectionsDataManager dataManager) {

        super(subscribeOn, observeOn);
        this.dataManager = dataManager;
    }

    @Override
    protected Observable<DirectionModel> getUseCaseObservable() {
        DirectionModelMapper mapper = new DirectionModelMapper();

        return dataManager.getDirections(origin, destination)
                .map(mapper::convert);
    }

    public void setOrigin(LatLongPair origin) {
        this.origin = origin;
    }

    public void setDestination(LatLongPair destination) {
        this.destination = destination;
    }
}

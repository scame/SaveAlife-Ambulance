package com.example.scame.savealife.domain.usecases;


import com.example.scame.savealife.data.repository.IGeocodingDataManager;
import com.example.scame.savealife.domain.schedulers.ObserveOn;
import com.example.scame.savealife.domain.schedulers.SubscribeOn;
import com.example.scame.savealife.data.mappers.AddressModelMapper;
import com.example.scame.savealife.presentation.models.AddressModel;

import rx.Observable;

public class ReverseGeocodeUseCase extends UseCase<AddressModel> {

    private IGeocodingDataManager dataManager;
    private AddressModelMapper mapper;

    private String latLng;

    public ReverseGeocodeUseCase(SubscribeOn subscribeOn, ObserveOn observeOn,
                                 IGeocodingDataManager dataManager) {
        super(subscribeOn, observeOn);

        this.dataManager = dataManager;
        mapper = new AddressModelMapper();
    }

    @Override
    protected Observable<AddressModel> getUseCaseObservable() {

        return dataManager.getHumanReadableAddress(latLng)
                .map(mapper::convert);
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }
}

package com.example.scame.savealife.domain.usecases;


import com.example.scame.savealife.data.repository.IMapsDataManager;
import com.example.scame.savealife.domain.schedulers.ObserveOn;
import com.example.scame.savealife.domain.schedulers.SubscribeOn;

import rx.Observable;

public class DownloadMapUseCase extends UseCase<Integer> {

    private IMapsDataManager dataManager;

    private String downloadUrl;

    public DownloadMapUseCase(IMapsDataManager dataManager, SubscribeOn subscribeOn,
                              ObserveOn observeOn) {

        super(subscribeOn, observeOn);
        this.dataManager = dataManager;
    }

    @Override
    protected Observable<Integer> getUseCaseObservable() {
        return dataManager.downloadMap(downloadUrl);
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}

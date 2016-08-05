package com.example.scame.savealife.presentation.di.modules;


import com.example.scame.savealife.data.repository.IMapsDataManager;
import com.example.scame.savealife.domain.schedulers.ObserveOn;
import com.example.scame.savealife.domain.schedulers.SubscribeOn;
import com.example.scame.savealife.domain.usecases.DownloadMapUseCase;
import com.example.scame.savealife.domain.usecases.GetLocalAreasUseCase;
import com.example.scame.savealife.domain.usecases.GetRemoteAreasUseCase;
import com.example.scame.savealife.presentation.di.PerActivity;
import com.example.scame.savealife.presentation.presenters.IMapSelectionPresenter;
import com.example.scame.savealife.presentation.presenters.MapSelectionPresenterImp;

import dagger.Module;
import dagger.Provides;

import static com.example.scame.savealife.presentation.presenters.IMapSelectionPresenter.MapSelectionView;


@Module
public class MapSelectionModule {

    @PerActivity
    @Provides
    GetRemoteAreasUseCase getLocalAreasUseCase(IMapsDataManager dataManager,
                                               SubscribeOn subscribeOn, ObserveOn observeOn) {
        return new GetRemoteAreasUseCase(dataManager, subscribeOn, observeOn);
    }

    @PerActivity
    @Provides
    GetLocalAreasUseCase getRemoteAreasUseCase(IMapsDataManager dataManager,
                                               SubscribeOn subscribeOn, ObserveOn observeOn) {
        return new GetLocalAreasUseCase(dataManager, subscribeOn, observeOn);
    }

    @PerActivity
    @Provides
    DownloadMapUseCase getDownloadMapUseCase(IMapsDataManager dataManager,
                                    SubscribeOn subscribeOn, ObserveOn observeOn) {
        return new DownloadMapUseCase(dataManager, subscribeOn, observeOn);
    }

    @PerActivity
    @Provides
    IMapSelectionPresenter<MapSelectionView> provideMapSelectionPresenter(
            GetRemoteAreasUseCase remoteUseCase, GetLocalAreasUseCase localUseCase,
            DownloadMapUseCase downloadMapUseCase) {

        return new MapSelectionPresenterImp<>(remoteUseCase, localUseCase, downloadMapUseCase);
    }
}

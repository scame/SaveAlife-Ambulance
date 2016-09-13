package com.example.scame.savealife.presentation.di.modules;

import com.example.scame.savealife.data.repository.IDirectionsDataManager;
import com.example.scame.savealife.data.repository.IGeocodingDataManager;
import com.example.scame.savealife.data.repository.ILocationDataManager;
import com.example.scame.savealife.data.repository.IMessagesDataManager;
import com.example.scame.savealife.domain.schedulers.ObserveOn;
import com.example.scame.savealife.domain.schedulers.SubscribeOn;
import com.example.scame.savealife.domain.usecases.ComputeDirectionUseCase;
import com.example.scame.savealife.domain.usecases.LocationUpdatesUseCase;
import com.example.scame.savealife.domain.usecases.ReverseGeocodeUseCase;
import com.example.scame.savealife.domain.usecases.SetupDestinationUseCase;
import com.example.scame.savealife.presentation.di.PerActivity;
import com.example.scame.savealife.presentation.presenters.IPointLocationPresenter;
import com.example.scame.savealife.presentation.presenters.PointLocationPresenterImp;

import dagger.Module;
import dagger.Provides;

import static com.example.scame.savealife.presentation.presenters.IPointLocationPresenter.PointLocationView;

@Module
public class PointLocationModule {

    @PerActivity
    @Provides
    IPointLocationPresenter<PointLocationView> providePointLocationPresenter(ReverseGeocodeUseCase reverseGeocodeUseCase,
                                                                             ComputeDirectionUseCase computeDirectionUseCase,
                                                                             LocationUpdatesUseCase locationUpdatesUseCase,
                                                                             SetupDestinationUseCase destinationUseCase) {
        return new PointLocationPresenterImp<>(reverseGeocodeUseCase, computeDirectionUseCase,
                locationUpdatesUseCase, destinationUseCase);
    }

    @PerActivity
    @Provides
    ReverseGeocodeUseCase provideReverseGeocodeUseCase(SubscribeOn subscribeOn, ObserveOn observeOn,
                                                       IGeocodingDataManager dataManager) {

        return new ReverseGeocodeUseCase(subscribeOn, observeOn, dataManager);
    }

    @PerActivity
    @Provides
    ComputeDirectionUseCase computeDirectionUseCase(SubscribeOn subscribeOn, ObserveOn observeOn,
                                                    IDirectionsDataManager dataManager) {

        return new ComputeDirectionUseCase(subscribeOn, observeOn, dataManager);
    }

    @PerActivity
    @Provides
    LocationUpdatesUseCase locationUpdatesUseCase(SubscribeOn subscribeOn, ObserveOn observeOn,
                                                  ILocationDataManager dataManager) {

        return new LocationUpdatesUseCase(subscribeOn, observeOn, dataManager);
    }

    @PerActivity
    @Provides
    SetupDestinationUseCase setupDestinationUseCase(SubscribeOn subscribeOn, ObserveOn observeOn,
                                                    IMessagesDataManager dataManager) {

        return new SetupDestinationUseCase(subscribeOn, observeOn, dataManager);
    }
}

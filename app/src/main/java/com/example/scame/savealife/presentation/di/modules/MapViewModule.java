package com.example.scame.savealife.presentation.di.modules;

import android.app.Activity;

import com.example.scame.savealife.data.repository.IMapsDataManager;
import com.example.scame.savealife.domain.schedulers.ObserveOn;
import com.example.scame.savealife.domain.schedulers.SubscribeOn;
import com.example.scame.savealife.domain.usecases.CalculatePathUseCase;
import com.example.scame.savealife.domain.usecases.LoadGraphStorageUseCase;
import com.example.scame.savealife.presentation.di.PerActivity;
import com.example.scame.savealife.presentation.presenters.IMapViewPresenter;
import com.example.scame.savealife.presentation.presenters.MapViewPresenterImp;

import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.cache.TileCache;

import dagger.Module;
import dagger.Provides;

@Module
public class MapViewModule {

    private Activity activity;

    public MapViewModule(Activity activity) {
        this.activity = activity;
    }

    @PerActivity
    @Provides
    MapView provideMapView() {
        MapView mapView = new MapView(activity);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);

        return mapView;
    }

    @PerActivity
    @Provides
    TileCache provideTileCache(MapView mapView) {
        return AndroidUtil.createTileCache(activity, getClass().getSimpleName(),
                mapView.getModel().displayModel.getTileSize(), 1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());
    }

    @PerActivity
    @Provides
    CalculatePathUseCase provideCalculatePathUseCase(IMapsDataManager dataManager,
                                                     SubscribeOn subscribeOn, ObserveOn observeOn) {

        return new CalculatePathUseCase(dataManager, subscribeOn, observeOn);
    }

    @PerActivity
    @Provides
    LoadGraphStorageUseCase provideLoadGraphStorageUseCase(IMapsDataManager dataManager,
                                                           SubscribeOn subscribeOn, ObserveOn observeOn) {

        return new LoadGraphStorageUseCase(dataManager, subscribeOn, observeOn);
    }

    @PerActivity
    @Provides
    IMapViewPresenter<IMapViewPresenter.MapViewView> provideMapViewPresenter(
                        LoadGraphStorageUseCase storageUseCase,
                        CalculatePathUseCase calculateUseCase) {

        return new MapViewPresenterImp<>(calculateUseCase, storageUseCase);
    }
}

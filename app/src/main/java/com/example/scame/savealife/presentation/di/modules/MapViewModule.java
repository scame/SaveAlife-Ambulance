package com.example.scame.savealife.presentation.di.modules;

import android.app.Activity;

import com.example.scame.savealife.presentation.di.PerActivity;

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


    /**
     * usecases & presenters go here
     */
}

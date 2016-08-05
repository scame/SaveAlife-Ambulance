package com.example.scame.savealife.presentation.presenters;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.layer.overlay.Polyline;

public interface IMapViewPresenter<T> extends Presenter<T> {

    interface MapViewView {

        void addPolyline(Polyline polyline);

        void updateLayers(LatLong latLong, int res);

        void removeLayers();
    }

    boolean onMapTap(LatLong latLong);

    void loadGraphStorage();

    void setFilePath(String filePath);
}

package com.example.scame.savealife.presentation.presenters;

import com.graphhopper.PathWrapper;

import org.mapsforge.core.model.LatLong;

public interface IMapViewPresenter<T> extends Presenter<T> {

    interface MapViewView {

        void addPolyline(PathWrapper pathWrapper);

        void updateLayers(LatLong latLong, int res);

        void removeLayers();
    }

    boolean onMapTap(LatLong latLong);

    void loadGraphStorage();

    void setFilePath(String filePath);
}

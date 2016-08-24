package com.example.scame.savealife.presentation.presenters;

import com.example.scame.savealife.data.entities.LatLongPair;
import com.google.android.gms.maps.model.PolylineOptions;

public interface IPointLocationPresenter<T> extends Presenter<T> {

    interface PointLocationView {

        void showHumanReadableAddress(String latLng);

        void drawDirectionPolyline(PolylineOptions polyline);

        void updateCurrentLocation(LatLongPair latLongPair);
    }

    void geocodeToHumanReadableFormat(String latLng);

    void computeDirection(LatLongPair origin, LatLongPair destination);

    void startLocationUpdates();
}

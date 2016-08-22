package com.example.scame.savealife.presentation.presenters;

public interface IPointLocationPresenter<T> extends Presenter<T> {

    interface PointLocationView {

        void showHumanReadableAddress(String latLng);
    }

    void geocodeToHumanReadableFormat(String latLng);
}

package com.example.scame.savealife.presentation.presenters;

public interface IPointLocationPresenter<T> extends Presenter<T> {

    interface PointLocationView {

        void updateLocation(double latitude, double longitude);
    }

    void showMyLocation();
}

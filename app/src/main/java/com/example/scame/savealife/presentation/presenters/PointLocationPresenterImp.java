package com.example.scame.savealife.presentation.presenters;

import com.example.scame.savealife.data.entities.LatLongPair;
import com.example.scame.savealife.domain.usecases.ComputeDirectionUseCase;
import com.example.scame.savealife.domain.usecases.DefaultSubscriber;
import com.example.scame.savealife.domain.usecases.ReverseGeocodeUseCase;
import com.example.scame.savealife.presentation.models.AddressModel;
import com.example.scame.savealife.presentation.models.DirectionModel;

public class PointLocationPresenterImp<T extends IPointLocationPresenter.PointLocationView>
                                            implements IPointLocationPresenter<T> {

    private ReverseGeocodeUseCase reverseGeocodeUseCase;

    private ComputeDirectionUseCase computeDirectionUseCase;

    private T view;

    public PointLocationPresenterImp(ReverseGeocodeUseCase reverseGeocodeUseCase,
                                     ComputeDirectionUseCase computeDirectionUseCase) {

        this.reverseGeocodeUseCase = reverseGeocodeUseCase;
        this.computeDirectionUseCase = computeDirectionUseCase;
    }

    @Override
    public void geocodeToHumanReadableFormat(String latLng) {
        reverseGeocodeUseCase.setLatLng(latLng);
        reverseGeocodeUseCase.execute(new ReverseGeocodeSubscriber());
    }

    @Override
    public void computeDirection(LatLongPair origin, LatLongPair destination) {
        computeDirectionUseCase.setDestination(destination);
        computeDirectionUseCase.setOrigin(origin);

        computeDirectionUseCase.execute(new DirectionSubscriber());
    }

    @Override
    public void setView(T view) {
        this.view = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    private final class ReverseGeocodeSubscriber extends DefaultSubscriber<AddressModel> {

        @Override
        public void onNext(AddressModel addressModel) {
            super.onNext(addressModel);

            String latLng = addressModel.getFormattedAddress();
            view.showHumanReadableAddress(latLng);
        }
    }

    private final class DirectionSubscriber extends DefaultSubscriber<DirectionModel> {

        @Override
        public void onNext(DirectionModel directionModel) {
            super.onNext(directionModel);

            view.drawDirectionPolyline(directionModel.getPolyline());
        }
    }
}

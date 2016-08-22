package com.example.scame.savealife.presentation.presenters;

import com.example.scame.savealife.domain.usecases.DefaultSubscriber;
import com.example.scame.savealife.domain.usecases.ReverseGeocodeUseCase;
import com.example.scame.savealife.presentation.models.AddressModel;

public class PointLocationPresenterImp<T extends IPointLocationPresenter.PointLocationView>
                                            implements IPointLocationPresenter<T> {

    private ReverseGeocodeUseCase reverseGeocodeUseCase;

    private T view;

    public PointLocationPresenterImp(ReverseGeocodeUseCase useCase) {
        reverseGeocodeUseCase = useCase;
    }

    @Override
    public void geocodeToHumanReadableFormat(String latLng) {
        reverseGeocodeUseCase.setLatLng(latLng);
        reverseGeocodeUseCase.execute(new ReverseGeocodeSubscriber());
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
}

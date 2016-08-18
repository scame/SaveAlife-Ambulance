package com.example.scame.savealife.presentation.presenters;

public class PointLocationPresenterImp<T extends IPointLocationPresenter.PointLocationView>
                                            implements IPointLocationPresenter<T> {

    private T view;

    public PointLocationPresenterImp() {}

    @Override
    public void showMyLocation() {

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
}

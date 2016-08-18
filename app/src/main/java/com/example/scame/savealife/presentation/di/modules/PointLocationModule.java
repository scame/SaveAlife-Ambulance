package com.example.scame.savealife.presentation.di.modules;

import com.example.scame.savealife.presentation.di.PerActivity;
import com.example.scame.savealife.presentation.presenters.IPointLocationPresenter;
import com.example.scame.savealife.presentation.presenters.PointLocationPresenterImp;

import dagger.Module;
import dagger.Provides;

@Module
public class PointLocationModule {

    @PerActivity
    @Provides
    IPointLocationPresenter<IPointLocationPresenter.PointLocationView> providePointLocationPresenter() {
        return new PointLocationPresenterImp<>();
    }
}

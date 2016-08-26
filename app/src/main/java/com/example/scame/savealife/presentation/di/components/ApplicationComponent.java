package com.example.scame.savealife.presentation.di.components;

import android.app.Application;

import com.example.scame.savealife.FusedLocationService;
import com.example.scame.savealife.data.di.DataModule;
import com.example.scame.savealife.presentation.di.modules.ApplicationModule;
import com.example.scame.savealife.presentation.di.modules.PointLocationModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {DataModule.class, ApplicationModule.class})
public interface ApplicationComponent {

    Application getApp();

    Retrofit getRetrofit();

    PointLocationComponent getPointLocationComponent(PointLocationModule module);

    void inject(FusedLocationService fusedLocationService);
}

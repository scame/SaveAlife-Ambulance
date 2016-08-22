package com.example.scame.savealife.presentation.di.components;

import com.example.scame.savealife.data.di.DataModule;
import com.example.scame.savealife.presentation.di.modules.ApplicationModule;
import com.example.scame.savealife.presentation.di.modules.PointLocationModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {DataModule.class, ApplicationModule.class})
public interface ApplicationComponent {

    Retrofit getRetrofit();

    PointLocationComponent getPointLocationComponent(PointLocationModule module);
}

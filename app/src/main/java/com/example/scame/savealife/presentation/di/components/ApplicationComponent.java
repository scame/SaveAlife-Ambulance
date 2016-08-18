package com.example.scame.savealife.presentation.di.components;

import com.example.scame.savealife.data.di.DataModule;
import com.example.scame.savealife.presentation.di.modules.ApplicationModule;
import com.example.scame.savealife.presentation.di.modules.PointLocationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModule.class, ApplicationModule.class})
public interface ApplicationComponent {

    PointLocationComponent getPointLocationComponent(PointLocationModule module);
}

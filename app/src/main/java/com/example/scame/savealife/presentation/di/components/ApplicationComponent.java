package com.example.scame.savealife.presentation.di.components;

import com.example.scame.savealife.data.di.DataModule;
import com.example.scame.savealife.data.repository.IFileDataManager;
import com.example.scame.savealife.presentation.di.modules.ApplicationModule;
import com.example.scame.savealife.presentation.di.modules.MapSelectionModule;
import com.example.scame.savealife.presentation.di.modules.MapViewModule;
import com.graphhopper.GraphHopper;
import com.graphhopper.util.Downloader;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModule.class, ApplicationModule.class})
public interface ApplicationComponent {

    IFileDataManager getFileDataManager();

    Downloader provideDownloader();

    GraphHopper provideGraphhopper();

    MapViewComponent getMapViewComponent(MapViewModule mapViewModule);

    MapSelectionComponent getMapSelectionComponent(MapSelectionModule module);
}

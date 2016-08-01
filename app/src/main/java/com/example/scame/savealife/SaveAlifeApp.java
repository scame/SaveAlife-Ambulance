package com.example.scame.savealife;

import android.app.Application;
import android.content.Context;

import com.example.scame.savealife.data.di.DataModule;
import com.example.scame.savealife.presentation.di.components.ApplicationComponent;
import com.example.scame.savealife.presentation.di.components.DaggerApplicationComponent;
import com.example.scame.savealife.presentation.di.modules.ApplicationModule;

public class SaveAlifeApp extends Application {

    private static ApplicationComponent appComponent;

    public static SaveAlifeApp getApp(Context context) {
        return (SaveAlifeApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        buildAppComponent();
    }

    private void buildAppComponent() {
        appComponent = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this))
        .dataModule(new DataModule())
        .build();
    }

    public static ApplicationComponent getAppComponent() {
        return appComponent;
    }
}

package com.example.scame.savealife;

import android.app.Application;
import android.content.Context;

import com.example.scame.savealife.presentation.di.components.ApplicationComponent;
import com.example.scame.savealife.presentation.di.DaggerApplicationComponent;

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
        appComponent = DaggerApplicationComponent.create();
    }

    public static ApplicationComponent getAppComponent() {
        return appComponent;
    }
}

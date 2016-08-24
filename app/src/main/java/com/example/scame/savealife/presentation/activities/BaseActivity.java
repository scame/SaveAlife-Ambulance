package com.example.scame.savealife.presentation.activities;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.scame.savealife.SaveAlifeApp;
import com.example.scame.savealife.presentation.di.components.ApplicationComponent;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inject(getAppComponent());
    }

    protected void replaceFragment(int containerViewId, Fragment fragment, String TAG) {

        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(containerViewId, fragment, TAG)
                    .commit();
        }
    }

    protected abstract void inject(ApplicationComponent component);

    protected ApplicationComponent getAppComponent() {
        return SaveAlifeApp.getAppComponent();
    }

    protected boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

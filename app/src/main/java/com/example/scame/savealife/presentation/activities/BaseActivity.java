package com.example.scame.savealife.presentation.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.scame.savealife.SaveAlifeApp;
import com.example.scame.savealife.presentation.di.components.ApplicationComponent;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        inject(getAppComponent());
    }

    protected void replaceFragment(int containerViewId, Fragment fragment, String TAG) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, TAG)
                .commit();
    }

    protected abstract void inject(ApplicationComponent component);

    protected ApplicationComponent getAppComponent() {
        return SaveAlifeApp.getAppComponent();
    }
}

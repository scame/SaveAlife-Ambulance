package com.example.scame.savealife.presentation.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.scame.savealife.R;
import com.example.scame.savealife.presentation.di.HasComponent;
import com.example.scame.savealife.presentation.di.components.ApplicationComponent;
import com.example.scame.savealife.presentation.di.components.MapViewComponent;
import com.example.scame.savealife.presentation.di.modules.MapViewModule;
import com.example.scame.savealife.presentation.fragments.MapDisplayFragment;

public class MapDisplayActivity extends BaseActivity implements HasComponent<MapViewComponent> {

    public static final String MAP_DISPLAY_TAG = "mapDisplay";

    private MapViewComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_display_activity);

        if (getSupportFragmentManager().findFragmentByTag(MAP_DISPLAY_TAG) == null) {
            replaceFragment(R.id.map_display_fl, getMapDisplayFragment(), MAP_DISPLAY_TAG);
        }
    }

    @Override
    protected void inject(ApplicationComponent component) {
        this.component = component.getMapViewComponent(new MapViewModule(this));
        this.component.inject(this);
    }

    private MapDisplayFragment getMapDisplayFragment() {
        String area = getIntent().getStringExtra(getString(R.string.AREA_KEY));
        Bundle args = new Bundle();
        args.putString(getString(R.string.AREA_KEY), area);

        MapDisplayFragment fragment = new MapDisplayFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public MapViewComponent getComponent() {
        return component;
    }
}

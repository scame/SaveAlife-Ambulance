package com.example.scame.savealife.presentation.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.scame.savealife.R;
import com.example.scame.savealife.presentation.di.HasComponent;
import com.example.scame.savealife.presentation.di.components.ApplicationComponent;
import com.example.scame.savealife.presentation.di.components.MapSelectionComponent;
import com.example.scame.savealife.presentation.di.modules.MapSelectionModule;
import com.example.scame.savealife.presentation.fragments.MapSelectionFragment;

public class MapSelectionActivity extends BaseActivity implements HasComponent<MapSelectionComponent>,
                                                            MapSelectionFragment.MapSelectionListener {

    public static final String MAP_SELECTION_TAG = "mapSelection";

    private MapSelectionComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_selection_activity);

        if (getSupportFragmentManager().findFragmentByTag(MAP_SELECTION_TAG) == null) {
            replaceFragment(R.id.map_selection_fl, new MapSelectionFragment(),
                    MAP_SELECTION_TAG);
        }
    }

    @Override
    protected void inject(ApplicationComponent component) {
        this.component = component.getMapSelectionComponent(new MapSelectionModule());
        this.component.inject(this);
    }


    @Override
    public MapSelectionComponent getComponent() {
        return component;
    }

    @Override
    public void loadMap(String area) {
        Intent i = new Intent(this, MapDisplayActivity.class);
        i.putExtra(getString(R.string.AREA_KEY), area);
        startActivity(i);
    }
}

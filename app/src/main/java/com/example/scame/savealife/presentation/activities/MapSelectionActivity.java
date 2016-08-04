package com.example.scame.savealife.presentation.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.scame.savealife.R;
import com.example.scame.savealife.SaveAlifeApp;
import com.example.scame.savealife.presentation.di.HasComponent;
import com.example.scame.savealife.presentation.di.components.ApplicationComponent;
import com.example.scame.savealife.presentation.di.components.MapSelectionComponent;
import com.example.scame.savealife.presentation.di.modules.MapSelectionModule;
import com.example.scame.savealife.presentation.fragments.MapSelectionFragment;

public class MapSelectionActivity extends BaseActivity implements HasComponent<MapSelectionComponent> {

    public static final String MAP_SELECTION_TAG = "mapSelection";

    private MapSelectionComponent selectionComponent;

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
        selectionComponent = SaveAlifeApp
                .getAppComponent()
                .getMapSelectionComponent(new MapSelectionModule());

        selectionComponent.inject(this);
    }


    @Override
    public MapSelectionComponent getComponent() {
        return selectionComponent;
    }
}

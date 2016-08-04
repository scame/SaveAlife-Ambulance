package com.example.scame.savealife.presentation.di.components;


import com.example.scame.savealife.presentation.activities.MapSelectionActivity;
import com.example.scame.savealife.presentation.di.PerActivity;
import com.example.scame.savealife.presentation.di.modules.MapSelectionModule;
import com.example.scame.savealife.presentation.fragments.MapSelectionFragment;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = MapSelectionModule.class)
public interface MapSelectionComponent {

    void inject(MapSelectionActivity activity);

    void inject(MapSelectionFragment fragment);
}

package com.example.scame.savealife.presentation.di.components;


import com.example.scame.savealife.presentation.activities.MapDisplayActivity;
import com.example.scame.savealife.presentation.di.PerActivity;
import com.example.scame.savealife.presentation.di.modules.MapViewModule;
import com.example.scame.savealife.presentation.fragments.MapDisplayFragment;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = MapViewModule.class)
public interface MapViewComponent {

    void inject(MapDisplayActivity activity);

    void inject(MapDisplayFragment fragment);
}

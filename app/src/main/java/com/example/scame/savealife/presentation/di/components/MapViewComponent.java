package com.example.scame.savealife.presentation.di.components;


import com.example.scame.savealife.presentation.di.PerActivity;
import com.example.scame.savealife.presentation.di.modules.MapViewModule;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = MapViewModule.class)
public class MapViewComponent {
}

package com.example.scame.savealife.presentation.di.components;


import com.example.scame.savealife.presentation.activities.PointLocationActivity;
import com.example.scame.savealife.presentation.di.PerActivity;
import com.example.scame.savealife.presentation.di.modules.PointLocationModule;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = PointLocationModule.class)
public interface PointLocationComponent {

    void inject(PointLocationActivity activity);
}

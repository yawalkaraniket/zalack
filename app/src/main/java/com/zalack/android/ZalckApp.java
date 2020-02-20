package com.zalack.android;

import android.app.Application;

import com.droidnet.DroidNet;
import com.zalack.android.dagger.component.DaggerPreferenceComponent;
import com.zalack.android.dagger.component.PreferenceComponent;
import com.zalack.android.dagger.module.SharedPreferencesModule;

public class ZalckApp extends Application {

    PreferenceComponent preferenceComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        DroidNet.init(this);

        preferenceComponent = DaggerPreferenceComponent.builder()
                .sharedPreferencesModule(new SharedPreferencesModule(getApplicationContext()))
                .build();

    }

    public PreferenceComponent getMyComponent() {
        return preferenceComponent;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners();
    }
}

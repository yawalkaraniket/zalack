package com.zalack.android;

import android.app.Application;

import com.droidnet.DroidNet;

public class ZalackApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DroidNet.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners();
    }
}

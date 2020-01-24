package com.zalack.android.dagger.module;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.zalack.android.dagger.scope.MyApplicationScope;
import com.zalack.android.data.ZalackPreferences;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferencesModule {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public SharedPreferencesModule(Context context) {
        SharedPreferencesModule.context = context;
    }

    @Provides
    @MyApplicationScope
    static SharedPreferences provideSharedPreferences() {
        return context.getSharedPreferences("PrefName",Context.MODE_PRIVATE);
    }

}

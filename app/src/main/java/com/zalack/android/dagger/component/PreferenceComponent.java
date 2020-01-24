package com.zalack.android.dagger.component;

import com.zalack.android.dagger.module.SharedPreferencesModule;
import com.zalack.android.dagger.scope.MyApplicationScope;
import com.zalack.android.ui.activity.LoginActivity;

import dagger.Component;

@Component(modules = SharedPreferencesModule.class)
@MyApplicationScope
public interface PreferenceComponent {
    void inject(LoginActivity loginActivity);
}

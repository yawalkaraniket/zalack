package com.zalack.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.zalack.android.R;
import com.zalack.android.ZalckApp;
import com.zalack.android.data.ZalckPreferences;

import javax.inject.Inject;

public class LauncherActivity extends BaseActivity {

    @Inject
    ZalckPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        ((ZalckApp) getApplicationContext()).getMyComponent().inject(this);

        new Handler().postDelayed(() -> {
            LauncherActivity.this.finish();
            if (prefs.getToken() == null) {
                Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(LauncherActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        }, 3000);
    }
}

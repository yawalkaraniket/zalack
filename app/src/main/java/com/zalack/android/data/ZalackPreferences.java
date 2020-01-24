package com.zalack.android.data;

import android.content.SharedPreferences;

import javax.inject.Inject;


public class ZalackPreferences{

    private SharedPreferences mSharedPreferences;
    private final String EMAIL = "email";

    @Inject
    public ZalackPreferences(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public void putEmail(String email) {
        putData(EMAIL, email);
    }

    public String getEmail() {
        return mSharedPreferences.getString(EMAIL,"");
    }

    public void putData(String key, int data) {
        mSharedPreferences.edit().putInt(key,data).apply();
    }

    private void putData(String key, String data) {
        mSharedPreferences.edit().putString(key,data).apply();
    }
}
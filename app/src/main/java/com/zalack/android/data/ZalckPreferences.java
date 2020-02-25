package com.zalack.android.data;

import android.content.SharedPreferences;

import javax.inject.Inject;

// Reference Dagger : https://android.jlelse.eu/dagger-2-the-simplest-approach-3e23502c4cab
public class ZalckPreferences {

    private SharedPreferences mSharedPreferences;
    private final String EMAIL = "email";
    private final String TOKEN = "token";
    private final String USER_NAME = "name";
    private final String MOBILE_NUMBER = "mobile";
    private final String CURRENT_PROJECT_ID = "Project ID";
    private final String CURRENT_PROJECT_NAME = "Project Name";

    @Inject
    public ZalckPreferences(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public void setEmail(String email) {
        putData(EMAIL, email);
    }

    public void setMobileNumber(String number) {
        putData(MOBILE_NUMBER, number);
    }

    public void setCurrentProjectId(int projectId) {
        putData(CURRENT_PROJECT_ID, projectId);
    }

    public void setProjectName(String projectName) {
        putData(CURRENT_PROJECT_NAME, projectName);
    }

    public void setToken(String token) {
        putData(TOKEN, token);
    }

    public void setName(String name) {
        putData(USER_NAME, name);
    }

    public String getEmail() {
        return mSharedPreferences.getString(EMAIL,"");
    }

    public String getToken() {
        return mSharedPreferences.getString(TOKEN, null);
    }

    public String getName() {
        return mSharedPreferences.getString(USER_NAME, "");
    }

    public String getNumber() {
        return mSharedPreferences.getString(MOBILE_NUMBER, "");
    }

    public int getCurrentProjectId() {
        try {
            return mSharedPreferences.getInt(CURRENT_PROJECT_ID, -1);
        } catch (Exception e) {
            setCurrentProjectId(-1);
            return mSharedPreferences.getInt(CURRENT_PROJECT_ID, -1);

        }
    }

    public String getCurrentProjectName() {
        return mSharedPreferences.getString(CURRENT_PROJECT_NAME, "");
    }

    public void putData(String key, int data) {
        mSharedPreferences.edit().putInt(key,data).apply();
    }

    private void putData(String key, String data) {
        mSharedPreferences.edit().putString(key,data).apply();
    }
}

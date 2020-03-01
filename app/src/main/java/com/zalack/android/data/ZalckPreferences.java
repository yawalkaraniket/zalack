package com.zalack.android.data;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zalack.android.data.models.all_projects.ProjectData;

import java.lang.reflect.Type;
import java.util.List;

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
    private final String PROJECTS = "Projects";
    private static final String KEY_APP_FIRST_LAUNCH = "app_first_launch";

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

    public <T> void setProjects(List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        mSharedPreferences.edit().putString(PROJECTS, json).apply();
    }

    public List<ProjectData> getAllProjects() {
        List<ProjectData> projectData;
        String json = mSharedPreferences.getString(PROJECTS, "");
        Gson gson=new Gson();
        Type type = new TypeToken<List<ProjectData>>() {}.getType();
        projectData = gson.fromJson(json, type);
        return projectData;
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

    private void putData(String key, int data) {
        mSharedPreferences.edit().putInt(key,data).apply();
    }

    private void putData(String key, String data) {
        mSharedPreferences.edit().putString(key,data).apply();
    }

    public boolean isAppFirstLaunch() {
        return mSharedPreferences.getBoolean(KEY_APP_FIRST_LAUNCH, true);
    }

    public void setAppFirstLaunch(boolean appFirstLaunch) {
        mSharedPreferences.edit().putBoolean(KEY_APP_FIRST_LAUNCH, appFirstLaunch).apply();
    }

}

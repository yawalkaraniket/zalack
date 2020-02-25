package com.zalack.android.data.models.update_profile;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable
{

    @SerializedName("user")
    @Expose
    private User user;
    private final static long serialVersionUID = -8081845885954596715L;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
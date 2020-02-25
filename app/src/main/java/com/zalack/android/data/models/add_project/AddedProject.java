package com.zalack.android.data.models.add_project;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddedProject implements Serializable
{

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private AddedProjectData data;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = -2872733565684312132L;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public AddedProjectData getData() {
        return data;
    }

    public void setData(AddedProjectData data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
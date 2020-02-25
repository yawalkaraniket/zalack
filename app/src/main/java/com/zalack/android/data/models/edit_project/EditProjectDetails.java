package com.zalack.android.data.models.edit_project;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditProjectDetails implements Serializable
{

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private ProjectDetails data;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = -2874666160371076726L;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ProjectDetails getData() {
        return data;
    }

    public void setData(ProjectDetails data) {
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

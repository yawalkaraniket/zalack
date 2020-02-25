package com.zalack.android.data.models.edit_task;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditTask implements Serializable
{

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private TaskDetails data;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = -5191251639876294523L;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public TaskDetails getData() {
        return data;
    }

    public void setData(TaskDetails data) {
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

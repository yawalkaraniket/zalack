package com.zalack.android.data.models.create_ticket;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateTicket implements Serializable
{

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private TicketDetails data;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = 6124356641890321525L;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public TicketDetails getData() {
        return data;
    }

    public void setData(TicketDetails data) {
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

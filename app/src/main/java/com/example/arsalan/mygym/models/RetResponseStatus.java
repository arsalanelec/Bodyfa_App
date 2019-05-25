package com.example.arsalan.mygym.models;

import com.example.arsalan.mygym.webservice.MyWebService;

public class RetResponseStatus {
    int status;
    String message;

    public RetResponseStatus() {
        status= MyWebService.STATUS_WAITING;
        message="";
    }

    public RetResponseStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

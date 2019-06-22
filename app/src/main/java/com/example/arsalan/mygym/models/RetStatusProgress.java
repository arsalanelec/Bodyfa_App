package com.example.arsalan.mygym.models;

import com.example.arsalan.mygym.webservice.MyWebService;

public class RetStatusProgress {
    int progress;
    int status;
    String error;
    public RetStatusProgress() {
        status=MyWebService.STATUS_WAITING;
        error="";
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

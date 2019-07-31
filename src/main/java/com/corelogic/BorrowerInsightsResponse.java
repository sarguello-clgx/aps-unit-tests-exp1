package com.corelogic;

public class BorrowerInsightsResponse<T> {
    public enum Status {
        SUCCESS, ERROR
    }

    public BorrowerInsightsResponse() {}

    public BorrowerInsightsResponse(Status status, T data, String errorInfo) {
        this.status = status;
        this.data = data;
        this.errorInfo = errorInfo;
    }

    private Status status;
    private String errorInfo;
    private T data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

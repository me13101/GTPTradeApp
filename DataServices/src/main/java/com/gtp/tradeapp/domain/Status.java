package com.gtp.tradeapp.domain;

public class Status {

    private int code;
    private String message;
    private Object info;

    public Status() {
    }

    public Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Status(int code, String message, Object info) {
        this.code = code;
        this.message = message;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }
}

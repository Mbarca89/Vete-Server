package com.mbarca.vete.domain;

public class AfipResponseObject {
    private String code;
    private String msg;

    public AfipResponseObject(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public AfipResponseObject() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "AfipResponseObject{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
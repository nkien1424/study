package com.example.study.Entity;

public enum ErrorCode {
    User_Existed(1001,"User already existed"),
    User_Not_Existed(1002,"User not existed"),
    UnAuthenticated(1003,"Account is UnAuthenticated");
    private int code;
    private String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;

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

}

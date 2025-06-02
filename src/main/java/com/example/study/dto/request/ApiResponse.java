package com.example.study.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse <T>{
    private int code;
    private String message;
    private T result;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(T data) {
        this.result = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return result;
    }
}


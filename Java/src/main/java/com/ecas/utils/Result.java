package com.ecas.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


@JsonIgnoreProperties
public class Result<T,t> implements Serializable {
    private int status;
    private String msg;
    private T data;
    private t errors;

    public t getErrors() {
        return errors;
    }

    public Result<T,t> setErrors(t errors) {
        this.errors = errors;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Result<T,t> setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<T,t> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T,t> setData(T data) {
        this.data = data;
        return this;
    }
}


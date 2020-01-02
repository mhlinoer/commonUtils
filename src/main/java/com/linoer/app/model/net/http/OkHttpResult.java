package com.linoer.app.model.net.http;

/**
 * okhttp封装的response类
 * @param <T>
 */
public class OkHttpResult<T> {

    private int status;
    private String result;
    private T resultObject;

    public OkHttpResult() {
    }

    public OkHttpResult(int status, String result, T resultObject) {
        this.status = status;
        this.result = result;
        this.resultObject = resultObject;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public T getResultObject() {
        return resultObject;
    }

    public void setResultObject(T resultObject) {
        this.resultObject = resultObject;
    }
}

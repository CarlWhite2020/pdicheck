package com.eucleia.pdicheck.net.model;

public class PDIResponse {

    /**
     * "success":false,
     * "message":"账号密码错误",//如果是禁用这里会有用户禁用的提示
     * "stackTrace":null
     */

    private boolean success;
    private String message;
    private String stackTrace;
    private String result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

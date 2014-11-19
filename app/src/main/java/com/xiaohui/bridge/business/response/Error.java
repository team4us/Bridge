package com.xiaohui.bridge.business.response;

/**
 * Created by xiaohui on 14/11/18.
 */
public class Error {

    private static final Error SUCCESS = new Error(1, "成功");
    private static final Error FAILURE = new Error(-1, "失败");
    private int code;
    private String message;

    public static Error Success() {
        return SUCCESS;
    }

    public static Error Failure() {
        return FAILURE;
    }

    public static Error newInstance(int code, String message) {
        return new Error(code, message);
    }

    public Error(int code, String message) {
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

    public boolean equals(Error e) {
        return code == e.getCode() && message.equalsIgnoreCase(e.getMessage());
    }
}

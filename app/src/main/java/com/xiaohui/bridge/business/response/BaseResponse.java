package com.xiaohui.bridge.business.response;

import com.google.gson.annotations.Expose;

/**
 * Created by xiaohui on 14/11/18.
 */
public class BaseResponse implements IResponse {

    @Expose
    private boolean success;
    @Expose
    private String message;

    @Override
    public Error verify() {
        if (isSuccess()) {
            return Error.Failure();
        } else {
            return Error.Success();
        }
    }

    @Override
    public void onParseComplete() {

    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}

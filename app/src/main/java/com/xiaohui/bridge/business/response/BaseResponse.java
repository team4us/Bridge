package com.xiaohui.bridge.business.response;

/**
 * Created by xiaohui on 14/11/18.
 */
public class BaseResponse implements IResponse {
    @Override
    public Error verify() {
        return Error.Success();
    }

    @Override
    public void onParseComplete() {

    }
}

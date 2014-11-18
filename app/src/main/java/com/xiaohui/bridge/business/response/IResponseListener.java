package com.xiaohui.bridge.business.response;

import com.xiaohui.bridge.business.request.AbstractRequest;

/**
 * Created by xiaohui on 13-11-20.
 */
public interface IResponseListener<T extends IResponse> {

    public void onSuccess(AbstractRequest<T> request, T response);

    public void onFail(AbstractRequest<T> request, Error error);
}

package com.xiaohui.bridge.business.request;

import com.xiaohui.bridge.business.response.IResponse;

/**
 * Created by xiaohui on 14/11/19.
 */
public abstract class Get<T extends IResponse> extends AbstractRequest<T> {
    public Get(String path) {
        super(Method.GET, path);
    }
}

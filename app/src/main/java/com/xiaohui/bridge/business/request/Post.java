package com.xiaohui.bridge.business.request;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyLog;
import com.xiaohui.bridge.business.response.BaseResponse;
import com.xiaohui.bridge.business.response.IResponse;
import com.xiaohui.bridge.util.JsonUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by xiaohui on 14/11/19.
 */
public abstract class Post<T extends BaseResponse> extends AbstractRequest<T> {
    public Post(String path) {
        super(Method.POST, path);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (getParams() != null) {
            return super.getBody();
        } else {
            String body = JsonUtil.toJson(this, true);
            try {
                return body == null ? null : body.getBytes(getParamsEncoding());
            } catch (UnsupportedEncodingException uee) {
                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                        body, getParamsEncoding());
                return null;
            }
        }
    }
}

package com.xiaohui.bridge.business.request;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.xiaohui.bridge.business.response.Error;
import com.xiaohui.bridge.business.response.IResponse;
import com.xiaohui.bridge.business.response.IResponseListener;
import com.xiaohui.bridge.util.JsonUtil;
import com.xiaohui.bridge.util.LogUtil;

import java.lang.reflect.Type;

/**
 * Created by xiaohui on 14/11/18.
 */
public abstract class AbstractRequest<T extends IResponse> extends Request<T> {
    protected IResponseListener<T> responseListener;

    private static final String HOST = "";

    public AbstractRequest(int method, String path) {
        super(method, HOST + path, null);
    }

    public void setResponseHandler(IResponseListener<T> listener) {
        responseListener = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        if (responseListener != null) {
            responseListener.onSuccess(this, response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        if (responseListener != null) {
            responseListener.onFail(this, Error.newInstance(-1, "失败"));
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        VolleyError volleyError;
        try {
            if (response.statusCode / 100 == 2) { //Http code是2xx，则表示Http请求成功
                String charset = HttpHeaderParser.parseCharset(response.headers);
                T resp = parseResponse(response.data, charset);
                resp.onParseComplete();
                Error error = resp.verify();
                if (!error.equals(Error.Success())) {
                    volleyError = new ParseError();
                } else {
                    return Response.success(resp, HttpHeaderParser.parseCacheHeaders(response));
                }
            } else {
                volleyError = new NetworkError();
            }
        } catch (Exception e) {
            e.printStackTrace();
            volleyError = new ParseError();
        }

        return Response.error(volleyError);
    }

    protected T parseResponse(byte[] data, String charset) throws Exception {
        String content = new String(data, charset);
        LogUtil.i("BaseRequest", content);
        return JsonUtil.fromJson(content, getResponseClass());
    }

    /**
     * 发送请求前的处理，比如参数验证
     *
     * @return true if success.
     */
    public boolean prePostOperation() {
        return true;
    }

    protected abstract Type getResponseClass();

}

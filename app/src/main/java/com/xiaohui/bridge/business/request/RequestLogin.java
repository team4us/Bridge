package com.xiaohui.bridge.business.request;

import com.android.volley.AuthFailureError;
import com.xiaohui.bridge.business.response.ResponseLogin;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaohui on 14/11/19.
 */
public class RequestLogin extends Post<ResponseLogin> {

    private static final String PATH = "/PadVisit/Login";

    private String userName;
    private String password;

    public RequestLogin() {
        super(PATH);
    }

    @Override
    protected Type getResponseClass() {
        return ResponseLogin.class;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", userName);
        params.put("password", userName);
        return params;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

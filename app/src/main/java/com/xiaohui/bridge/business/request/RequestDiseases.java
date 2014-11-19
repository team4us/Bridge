package com.xiaohui.bridge.business.request;

import com.android.volley.AuthFailureError;
import com.xiaohui.bridge.business.response.ResponseDiseases;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaohui on 14/11/19.
 */
public class RequestDiseases extends Post<ResponseDiseases> {

    private static final String PATH = "/PadVisit/PostDisease";

    private String year;

    public RequestDiseases() {
        super(PATH);
    }

    @Override
    protected Type getResponseClass() {
        return ResponseDiseases.class;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("year", year);
        return params;
    }

    public void setYear(String year) {
        this.year = year;
    }
}

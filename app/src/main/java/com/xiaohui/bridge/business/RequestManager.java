package com.xiaohui.bridge.business;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.xiaohui.bridge.business.request.AbstractRequest;
import com.xiaohui.bridge.business.response.IResponse;

import java.io.File;

/**
 * Created by xiaohui on 14/11/18.
 */
public class RequestManager {
    private File cacheDir;
    private RequestQueue requestQueue;

    public RequestManager() {
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            if (cacheDir == null) {
                throw new IllegalArgumentException("Cache Directory Should be Init first by BusinessManager.initCacheDir(File)");
            }
            Cache cache = new DiskBasedCache(cacheDir);
            HttpStack stack = new HttpClientStack(null); //TODO
            Network network = new BasicNetwork(stack);
            requestQueue = new RequestQueue(cache, network);
            requestQueue.start();
        }
        return requestQueue;
    }

    public void request(AbstractRequest<? extends IResponse> request) {
        if (request.prePostOperation()) {
            addToRequestQueue(request);
        }
    }

    private <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}

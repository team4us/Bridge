package com.xiaohui.bridge;

import android.app.Application;

import com.xiaohui.bridge.business.store.Cookie;

import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

/**
 * Created by xhChen on 14/9/22.
 */
public class XhApplication extends Application {

    private BinderFactory binderFactory;
    private Cookie cookie;

    @Override
    public void onCreate() {
        super.onCreate();
        binderFactory = new BinderFactoryBuilder().build();
        cookie = new Cookie();
    }

    public BinderFactory getBinderFactory() {
        return binderFactory;
    }

    public Cookie getCookie() {
        return cookie;
    }
}

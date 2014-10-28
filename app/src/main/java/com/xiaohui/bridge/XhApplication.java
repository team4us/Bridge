package com.xiaohui.bridge;

import android.app.Application;

import com.xiaohui.bridge.storage.Cookie;
import com.xiaohui.bridge.storage.DesEncrypt;
import com.xiaohui.bridge.storage.Store;

import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

/**
 * Created by xhChen on 14/9/22.
 */
public class XhApplication extends Application {

    private static final String STORE_NAME = "bridge.storage";
    private static final String KEY = "@WSXCDE#$RV3edc";
    private static final String DEFAULT_USER_NAME = "Default";

    private static Application application;
    private BinderFactory binderFactory;
    private Cookie cookie;
    private Store store;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        binderFactory = new BinderFactoryBuilder().build();
        cookie = new Cookie();
        store = new Store(this, STORE_NAME, new DesEncrypt(KEY));
    }

    public BinderFactory getBinderFactory() {
        return binderFactory;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public static Application getApplication() {
        return application;
    }

    public Store getStore() {
        return store;
    }

    public String getCurrentUserName() {
        return store.getString(Keys.USER, DEFAULT_USER_NAME);
    }

    public void setCurrentUserName(String userName) {
        store.putString(Keys.USER, userName);
    }
}

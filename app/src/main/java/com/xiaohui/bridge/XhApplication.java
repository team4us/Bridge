package com.xiaohui.bridge;

import android.app.Application;

import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

/**
 * Created by xhChen on 14/9/22.
 */
public class XhApplication extends Application {

    private BinderFactory binderFactory;

    @Override
    public void onCreate() {
        super.onCreate();

        binderFactory = new BinderFactoryBuilder().build();
    }

    public BinderFactory getBinderFactory() {
        return binderFactory;
    }
}

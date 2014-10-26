package com.xiaohui.bridge;

import android.app.Application;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;
import com.xiaohui.bridge.business.store.Cookie;
import com.xiaohui.bridge.util.LogUtil;

import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import java.io.IOException;

/**
 * Created by xhChen on 14/9/22.
 */
public class XhApplication extends Application {

    private static final String DATABASE_NAME = "bridge";
    private static Application application;
    private BinderFactory binderFactory;
    private Cookie cookie;
    private Database database;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        binderFactory = new BinderFactoryBuilder().build();
        cookie = new Cookie();
        initDatabase();
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

    private void initDatabase() {
        try {
            if (BuildConfig.DEBUG) {
                Manager.enableLogging(Log.TAG, Log.VERBOSE);
                Manager.enableLogging(Log.TAG_SYNC_ASYNC_TASK, Log.VERBOSE);
                Manager.enableLogging(Log.TAG_SYNC, Log.VERBOSE);
                Manager.enableLogging(Log.TAG_QUERY, Log.VERBOSE);
                Manager.enableLogging(Log.TAG_VIEW, Log.VERBOSE);
                Manager.enableLogging(Log.TAG_DATABASE, Log.VERBOSE);
            }

            Manager manager = new Manager(new AndroidContext(getApplicationContext()), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(DATABASE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public Database getDatabase() {
        return this.database;
    }
}

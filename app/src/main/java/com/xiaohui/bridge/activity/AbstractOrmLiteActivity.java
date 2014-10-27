package com.xiaohui.bridge.activity;

import android.content.Context;
import android.os.Bundle;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Created by xiaohui on 14-10-27.
 */
public abstract class AbstractOrmLiteActivity<T extends OrmLiteSqliteOpenHelper> extends AbstractActivity {
    private volatile T helper;
    private volatile boolean created = false;
    private volatile boolean destroyed = false;

    public T getHelper() {
        if (helper == null) {
            if (!created) {
                throw new IllegalStateException("A call has not been made to onCreate() yet so the helper is null");
            } else if (destroyed) {
                throw new IllegalStateException(
                        "A call to onDestroy has already been made and the helper cannot be used after that point");
            } else {
                throw new IllegalStateException("Helper is null for some unknown reason");
            }
        } else {
            return helper;
        }
    }

    public ConnectionSource getConnectionSource() {
        return getHelper().getConnectionSource();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (helper == null) {
            helper = getHelperInternal(this);
            created = true;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseHelper(helper);
        destroyed = true;
    }

    protected T getHelperInternal(Context context) {
        @SuppressWarnings({ "unchecked", "deprecation" })
        T newHelper = (T) OpenHelperManager.getHelper(context);
        return newHelper;
    }

    protected void releaseHelper(T helper) {
        OpenHelperManager.releaseHelper();
        this.helper = null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(super.hashCode());
    }
}

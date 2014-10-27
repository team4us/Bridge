package com.xiaohui.bridge.storage;

import java.lang.reflect.Type;

/**
 * Created by xiaohui on 13-12-27.
 */
public interface IStore {

    public void putBoolean(String key, boolean value);
    public boolean getBoolean(String key, boolean defValue);
    public void putInt(String key, int value);
    public int getInt(String key, int defValue);
    public void putLong(String key, long value);
    public long getLong(String key, long defValue);
    public void putString(String key, String value);
    public String getString(String key, String defValue);
    public void putObject(String key, Object value);
    public <T> T getObject(String key, Class<T> classOfT);
    public <T> T getObject(String key, Type type);
    public void removeKey(String key);
    public void removeAllKey();

}

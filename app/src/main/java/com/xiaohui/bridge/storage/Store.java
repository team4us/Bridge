package com.xiaohui.bridge.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.xiaohui.bridge.util.JsonUtil;

import java.lang.reflect.Type;

/**
 * Created by xiaohui on 14-6-6.
 */
public class Store implements IStore {

    private IEncrypt encrypt;
    private SharedPreferences preferences;

    public Store(Context context, String fileName) {
        this(context, fileName, new DefaultEncrypt());
    }

    public Store(Context context, String fileName, IEncrypt encrypt) {
        preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        this.encrypt = encrypt;
    }

    @Override
    public void putBoolean(String key, boolean value) {
        putString(key, String.valueOf(value));
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        String value = getString(key, String.valueOf(defValue));
        return Boolean.valueOf(value);
    }

    @Override
    public void putInt(String key, int value) {
        putString(key, String.valueOf(value));
    }

    @Override
    public int getInt(String key, int defValue) {
        String value = getString(key, String.valueOf(defValue));
        return Integer.valueOf(value);
    }

    @Override
    public void putLong(String key, long value) {
        putString(key, String.valueOf(value));
    }

    @Override
    public long getLong(String key, long defValue) {
        String value = getString(key, String.valueOf(defValue));
        return Long.valueOf(value);
    }

    @Override
    public void putString(String key, String value) {
        if (preferences == null)
            return;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(encrypt.encrypt(key), encrypt.encrypt(value));
        editor.commit();
    }

    @Override
    public String getString(String key, String defValue) {
        if (preferences == null)
            return defValue;
        return encrypt.decrypt(preferences.getString(encrypt.encrypt(key), defValue));
    }

    @Override
    public void putObject(String key, Object value) {
        putString(key, JsonUtil.toJson(value, false));
    }

    @Override
    public <T> T getObject(String key, Class<T> classOfT) {
        return JsonUtil.fromJson(getString(key, ""), classOfT, false);
    }

    @Override
    public <T> T getObject(String key, Type type) {
        return JsonUtil.fromJson(getString(key, ""), type, false);
    }

    @Override
    public void removeKey(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(encrypt.encrypt(key));
        editor.commit();
    }

    @Override
    public void removeAllKey() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}

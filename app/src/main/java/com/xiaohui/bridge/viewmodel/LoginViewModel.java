package com.xiaohui.bridge.viewmodel;

import android.text.TextUtils;
import android.util.Log;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.storage.Store;
import com.xiaohui.bridge.view.ILoginView;

import org.robobinding.annotation.PresentationModel;

/**
 * Created by xhChen on 14/9/22.
 */
@PresentationModel
public class LoginViewModel {

    private ILoginView loginView;
    private Store store;
    private String name;
    private String password;

    public LoginViewModel(ILoginView view, Store store) {
        loginView = view;
        this.store = store;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() {
        String name = getName();
        if (TextUtils.isEmpty(name)) {
            name = "default";
        }
        store.putString(Keys.USER_NAME, name);
        loginView.onClickLogin();
    }
}

package com.xiaohui.bridge.viewmodel;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.model.UserModel;
import com.xiaohui.bridge.storage.Cookie;
import com.xiaohui.bridge.storage.Store;
import com.xiaohui.bridge.view.ILoginView;

import org.robobinding.annotation.PresentationModel;

import java.sql.SQLException;

/**
 * Created by xhChen on 14/9/22.
 */
@PresentationModel
public class LoginViewModel {

    private ILoginView loginView;
    private Store store;
    private Cookie cookie;
    private String name;
    private String password;
    private Dao<UserModel, Integer> dao;

    public LoginViewModel(ILoginView view, Store store, Dao<UserModel, Integer> dao, Cookie cookie) {
        loginView = view;
        this.store = store;
        this.dao = dao;
        this.cookie = cookie;
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

    private boolean verify() {
        return !TextUtils.isEmpty(name) && name.equals("test")
                && !TextUtils.isEmpty(password) && password.equals("111111");
    }

    public void login() {
        if (verify()) {
            store.putString(Keys.USER, name);
            UserModel userModel = new UserModel();
            userModel.setUserName(name);
            try {
                dao.create(userModel);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            cookie.put(Keys.USER, userModel);
            loginView.loginSuccess();
        } else {
            loginView.loginFailed();
        }
    }
}

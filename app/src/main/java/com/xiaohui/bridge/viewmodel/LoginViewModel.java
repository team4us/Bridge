package com.xiaohui.bridge.viewmodel;

import android.util.Log;

import com.xiaohui.bridge.view.ILoginView;

import org.robobinding.aspects.PresentationModel;

/**
 * Created by xhChen on 14/9/22.
 */
@PresentationModel
public class LoginViewModel {

    private ILoginView loginView;

    public LoginViewModel(ILoginView view) {
        loginView = view;
    }

    public void login() {
        Log.d("11", "1111");
        loginView.onClickLogin();
    }
}

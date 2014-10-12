package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.os.Bundle;

import com.xiaohui.bridge.R;
import com.xiaohui.bridge.view.ILoginView;
import com.xiaohui.bridge.viewmodel.LoginViewModel;

/**
 * Created by xhChen on 14/9/22.
 */
public class LoginActivity extends AbstractActivity implements ILoginView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login, new LoginViewModel(this));
    }

    @Override
    public void onClickLogin() {
        startActivity(new Intent(this, ProjectsActivity.class));
        finish();
    }
}

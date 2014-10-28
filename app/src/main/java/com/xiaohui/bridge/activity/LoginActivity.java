package com.xiaohui.bridge.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.view.ILoginView;
import com.xiaohui.bridge.viewmodel.LoginViewModel;

/**
 * Created by xhChen on 14/9/22.
 */
public class LoginActivity extends AbstractOrmLiteActivity<DatabaseHelper> implements ILoginView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login, new LoginViewModel(this, getStore(), getHelper().getUserDao(), getCookie()));
    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, ProjectsActivity.class));
        finish();
    }

    @Override
    public void loginFailed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请输入正确的账号和密码！");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}

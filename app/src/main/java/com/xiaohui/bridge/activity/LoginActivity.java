package com.xiaohui.bridge.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.CheckBox;
import android.widget.Toast;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.BusinessManager;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.view.ILoginView;
import com.xiaohui.bridge.viewmodel.LoginViewModel;

import java.io.File;

/**
 * Created by xhChen on 14/9/22.
 */
public class LoginActivity extends AbstractOrmLiteActivity<DatabaseHelper> implements ILoginView {

    private LoginViewModel loginViewModel;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel =  new LoginViewModel(this, getStore(), getHelper().getUserDao(), getCookie());
        setContentView(R.layout.activity_login, loginViewModel);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
    }

    @Override
    public void loginSuccess() {
        if(!checkBox.isChecked()) {
            startActivity(new Intent(this, ProjectsActivity.class));
        } else {
            startActivity(new Intent(this, AllUsersActivity.class));
        }
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

//    private void initFolder(){
//        String PicturePath = BusinessManager.ALL_MEDIA_FILE_PATH + loginViewModel.getName() + File.separator;
//        File saveDir = new File(PicturePath);
//
//        if (!saveDir.exists()) {
//            if (!saveDir.mkdirs()) {
//                Toast.makeText(this, "创建个人文档目录失败", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        BusinessManager.USER_MEDIA_FILE_PATH = PicturePath;
//    }
}

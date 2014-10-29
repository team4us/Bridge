package com.xiaohui.bridge.activity;

import android.os.Bundle;

import com.xiaohui.bridge.model.UserModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.view.IUserView;
import com.xiaohui.bridge.viewmodel.UsersViewModel;

/**
 *
 * Created by Administrator on 2014/10/29.
 */
public class AllUsersActivity extends AbstractOrmLiteActivity<DatabaseHelper> implements IUserView{

    private UsersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel= new UsersViewModel(this, getCookie(), getHelper());
    }

    @Override
    public void onItemSelect(int position, UserModel object) {

    }
}

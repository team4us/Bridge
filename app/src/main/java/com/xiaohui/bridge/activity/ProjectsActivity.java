package com.xiaohui.bridge.activity;

import android.content.Intent;
import android.os.Bundle;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.bean.Project;
import com.xiaohui.bridge.view.IProjectView;
import com.xiaohui.bridge.viewmodel.ProjectsViewModel;

/**
 * Created by xhChen on 14/9/27.
 */
public class ProjectsActivity extends AbstractActivity implements IProjectView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeContentView(R.layout.activity_projects, new ProjectsViewModel(this));
    }

    @Override
    public void onItemSelect(int position, Project project) {
        getCookie().put(Keys.PROJECT, project);
        startActivity(new Intent(this, MainActivity.class));
    }
}

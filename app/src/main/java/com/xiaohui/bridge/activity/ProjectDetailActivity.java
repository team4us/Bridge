package com.xiaohui.bridge.activity;

import android.os.Bundle;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.R;
import com.xiaohui.bridge.business.bean.Project;
import com.xiaohui.bridge.viewmodel.ProjectDetailViewModel;
import com.xiaohui.bridge.viewmodel.ProjectsViewModel;

/**
 * 项目详情界面
 * Created by Administrator on 2014/9/27.
 */
public class ProjectDetailActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectDetailViewModel viewModel = new ProjectDetailViewModel();
        viewModel.setProject((Project) getCookie().get(Keys.PROJECT));
        initializeContentView(R.layout.activity_project_detail, viewModel);
        setTitle("项目详情");
    }
}

package com.xiaohui.bridge.activity;

import android.os.Bundle;

import com.xiaohui.bridge.R;

/**
 * 项目详情界面
 * Created by Administrator on 2014/9/27.
 */
public class ProjectDetailActivity extends AbstractActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        setTitle("项目详情");
    }
}

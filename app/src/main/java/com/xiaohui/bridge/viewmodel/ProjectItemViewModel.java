package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.model.ProjectModel;

import org.robobinding.itempresentationmodel.ItemContext;
import org.robobinding.itempresentationmodel.ItemPresentationModel;

/**
 * Created by xiaohui on 14-9-24.
 */
public class ProjectItemViewModel implements ItemPresentationModel<ProjectModel> {

    private String title;
    private String code;

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return "项目编号：" + code;
    }

    @Override
    public void updateData(ProjectModel project, ItemContext itemContext) {
        title = project.getProject().getName();
        code = project.getProject().getCode();
    }
}

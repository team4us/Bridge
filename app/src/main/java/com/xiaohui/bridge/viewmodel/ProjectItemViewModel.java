package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.business.bean.Project;

import org.robobinding.itempresentationmodel.ItemPresentationModel;

/**
 * Created by xiaohui on 14-9-24.
 */
public class ProjectItemViewModel implements ItemPresentationModel<Project> {

    private String title;

    public String getTitle() {
        return title;
    }

    @Override
    public void updateData(int index, Project project) {
        title = project.getName();
    }
}

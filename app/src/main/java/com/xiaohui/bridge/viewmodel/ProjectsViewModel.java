package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.business.bean.Project;
import com.xiaohui.bridge.business.store.StoreManager;
import com.xiaohui.bridge.model.ProjectsModel;
import com.xiaohui.bridge.view.IProjectView;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.widget.adapterview.ItemClickEvent;

import java.util.List;

/**
 * Created by xiaohui on 14-9-24.
 */
@PresentationModel
public class ProjectsViewModel {

    private IProjectView projectView;
    private ProjectsModel model;

    public ProjectsViewModel(IProjectView view) {
        projectView = view;
        model = new ProjectsModel(StoreManager.Instance.getProjects());
    }

    @ItemPresentationModel(ProjectItemViewModel.class)
    public List<Project> getProjects() {
        return model.getProjects();
    }

    public Project getCurrentProject() {
        return model.getCurrentProject();
    }

    public void onItemClick(ItemClickEvent event) {
        int pos = event.getPosition();
        model.setPosition(event.getPosition());
        if (projectView != null) {
            projectView.onItemSelect(pos, model.getProject(pos));
        }
    }
}

package com.xiaohui.bridge.viewmodel;

import com.j256.ormlite.stmt.QueryBuilder;
import com.xiaohui.bridge.business.BusinessManager;
import com.xiaohui.bridge.model.ProjectModel;
import com.xiaohui.bridge.model.UserModel;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.view.IProjectView;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;
import org.robobinding.widget.adapterview.ItemClickEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohui on 14-9-24.
 */
@PresentationModel
public class ProjectsViewModel implements HasPresentationModelChangeSupport {

    private final PresentationModelChangeSupport changeSupport;
    private IProjectView projectView;
    private DatabaseHelper helper;
    private UserModel user;
    private List<ProjectModel> projects;

    public ProjectsViewModel(IProjectView view, DatabaseHelper helper, UserModel user) {
        projectView = view;
        this.helper = helper;
        this.user = user;
        this.changeSupport = new PresentationModelChangeSupport(this);
    }

    @ItemPresentationModel(ProjectItemViewModel.class)
    public List<ProjectModel> getProjects() {
        if (projects == null) {
            try {
                QueryBuilder<ProjectModel, Integer> builder = helper.getProjectDao().queryBuilder();
                builder.where().eq("userName", user.getUserName());
                builder.orderBy("Id", false);
                projects = builder.query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return projects;
    }

    public void updateData() {
        projects = null;
        changeSupport.firePropertyChange("projects");
    }

    public ProjectModel getProject(int index) {
        return projects.get(index);
    }

    public void onItemClick(ItemClickEvent event) {
        int pos = event.getPosition();
        if (projectView != null) {
            projectView.onItemSelect(pos, projects.get(pos));
        }
    }

    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return changeSupport;
    }

}

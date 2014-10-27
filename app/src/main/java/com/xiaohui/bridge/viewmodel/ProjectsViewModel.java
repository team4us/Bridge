package com.xiaohui.bridge.viewmodel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.xiaohui.bridge.business.BusinessManager;
import com.xiaohui.bridge.business.bean.Project;
import com.xiaohui.bridge.business.store.StoreManager;
import com.xiaohui.bridge.model.ProjectModel;
import com.xiaohui.bridge.model.ProjectsModel;
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
    private Dao<ProjectModel, Integer> dao;
    private String userName;
    private BusinessManager businessManager;
    private List<ProjectModel> projects;


    public ProjectsViewModel(IProjectView view, Dao<ProjectModel, Integer> dao, String userName) {
        projectView = view;
        this.dao = dao;
        this.userName = userName;
        this.changeSupport = new PresentationModelChangeSupport(this);
        this.businessManager = new BusinessManager();
    }

    @ItemPresentationModel(ProjectItemViewModel.class)
    public List<ProjectModel> getProjects() {
        if (projects == null) {
            try {
                QueryBuilder<ProjectModel, Integer> builder = dao.queryBuilder();
                builder.orderBy("Id", false);
                projects = builder.query();
            } catch (SQLException e) {
                e.printStackTrace();
                projects = new ArrayList<ProjectModel>();
            }
        }

        return projects;
    }

    public void onItemClick(ItemClickEvent event) {
        int pos = event.getPosition();
        if (projectView != null) {
            projectView.onItemSelect(pos, projects.get(pos));
        }
    }

    public void download() {
        businessManager.download(dao, userName);
        projects = null;
        changeSupport.firePropertyChange("projects");
    }

    public void upload() {
        businessManager.upload();
    }

    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return changeSupport;
    }

}

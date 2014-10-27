package com.xiaohui.bridge.business;

import com.j256.ormlite.dao.Dao;
import com.xiaohui.bridge.business.bean.Project;
import com.xiaohui.bridge.business.store.StoreManager;
import com.xiaohui.bridge.model.ProjectModel;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by xiaohui on 14-10-27.
 */
public class BusinessManager {

    public void download(Dao<ProjectModel, Integer> dao, String userName) {
        List<Project> projects = StoreManager.Instance.getProjects();
        try {
            for (Project project : projects) {
                ProjectModel projectModel = new ProjectModel();
                projectModel.setProject(project);
                projectModel.setUserName(userName);
                dao.create(projectModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upload() {

    }
}

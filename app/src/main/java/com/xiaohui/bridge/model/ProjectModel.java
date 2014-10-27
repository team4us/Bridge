package com.xiaohui.bridge.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.xiaohui.bridge.business.bean.Project;

/**
 * Created by xiaohui on 14-10-27.
 */
public class ProjectModel {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String userName;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Project project;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getUserName() {
        return userName;
    }

    public Project getProject() {
        return project;
    }
}

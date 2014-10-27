package com.xiaohui.bridge.model;

import com.xiaohui.bridge.business.bean.Project;

import java.util.List;

/**
 * Created by xhChen on 14/9/27.
 */
public class ProjectsModel {

    private List<Project> projects;
    private int position;

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public Project getCurrentProject() {
        return getProject(position);
    }

    public Project getProject(int position) {
        return projects.get(position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

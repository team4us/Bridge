package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.business.bean.Project;

import org.robobinding.aspects.PresentationModel;

/**
 * Created by xiaohui on 14-9-24.
 */
@PresentationModel
public class ProjectDetailViewModel {

    private Project project;

    public ProjectDetailViewModel() {
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getName() {
        return project.getName();
    }

    public String getCode() {
        return project.getCode();
    }

    public String getCompany() {
        return project.getCompany();
    }

    public String getCategory() {
        return project.getCategory();
    }

    public String getManager() {
        return project.getManager();
    }

    public String getCreator() {
        return project.getCreator();
    }

    public String getAuditor() {
        return project.getAuditor();
    }

    public String getJudge() {
        return project.getJudge();
    }

    public String getTechManager() {
        return project.getTechManager();
    }

    public String getDecisionMaker() {
        return project.getDecisionMaker();
    }

    public String getMember() {
        return project.getMember();
    }

    public String getDescription() {
        return project.getDescription();
    }
}

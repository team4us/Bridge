package com.xiaohui.bridge.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.xiaohui.bridge.business.bean.Bridge;

import java.util.List;

/**
 * Created by xiaohui on 14-9-24.
 */
@DatabaseTable(tableName = "Bridge")
public class BridgeModel {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String projectCode;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Bridge bridge;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName="project_id")
    private ProjectModel project;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<ChildBridgeModel> childBridges;

    public void setBridge(Bridge bridge) {
        this.bridge = bridge;
    }

    public Bridge getBridge() {
        return bridge;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public int getId() {
        return id;
    }

    public ProjectModel getProject() {
        return project;
    }

    public void setProject(ProjectModel project) {
        this.project = project;
    }
}

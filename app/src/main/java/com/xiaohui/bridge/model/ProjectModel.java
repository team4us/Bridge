package com.xiaohui.bridge.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.xiaohui.bridge.business.bean.Project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xiaohui on 14-10-27.
 */
@DatabaseTable(tableName = "Project")
public class ProjectModel {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String userName;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Project project;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName="user_id")
    private UserModel user;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<BridgeModel> bridges;

    public int getId() {
        return id;
    }

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

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<BridgeModel> getBridges() {
        List<BridgeModel> list = new ArrayList<BridgeModel>();
        Iterator<BridgeModel> iterator = bridges.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
}

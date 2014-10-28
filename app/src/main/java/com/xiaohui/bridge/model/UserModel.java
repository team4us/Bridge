package com.xiaohui.bridge.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xhChen on 14/10/27.
 */
@DatabaseTable(tableName = "User")
public class UserModel {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String userName;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<ProjectModel> projects;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<ProjectModel> getProjects() {
        List<ProjectModel> list = new ArrayList<ProjectModel>();
        if (projects != null) {
            Iterator<ProjectModel> iterator = projects.iterator();
            while (iterator.hasNext()) {
                ProjectModel projectModel = iterator.next();
                list.add(projectModel);
            }
        }
        return list;
    }
}

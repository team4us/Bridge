package com.xiaohui.bridge.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by xhChen on 14/10/27.
 */
@DatabaseTable(tableName = "User")
public class UserModel {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(uniqueIndex = true)
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

package com.xiaohui.bridge.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by xhChen on 14/10/27.
 */
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

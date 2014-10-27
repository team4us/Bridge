package com.xiaohui.bridge.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.xiaohui.bridge.business.bean.Bridge;

import java.util.List;

/**
 * Created by xiaohui on 14-9-24.
 */
public class BridgeModel {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String projectCode;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Bridge bridge;

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
}

package com.xiaohui.bridge.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.xiaohui.bridge.business.bean.Disease;

/**
 * Created by xiaohui on 14-10-28.
 */
@DatabaseTable(tableName = "Disease")
public class DiseaseModel {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Disease disease;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "component_id")
    private ComponentModel component;
    @DatabaseField
    private long time;

    public int getId() {
        return id;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public ComponentModel getComponent() {
        return component;
    }

    public void setComponent(ComponentModel component) {
        this.component = component;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

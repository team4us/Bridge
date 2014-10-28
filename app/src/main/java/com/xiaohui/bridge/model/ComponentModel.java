package com.xiaohui.bridge.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.xiaohui.bridge.business.bean.Component;

/**
 * Created by xhChen on 14/10/28.
 */
@DatabaseTable(tableName = "Component")
public class ComponentModel {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Component component;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "child_bridge_id")
    private ChildBridgeModel childBridge;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<DiseaseModel> diseases;

    public int getId() {
        return id;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public ChildBridgeModel getChildBridge() {
        return childBridge;
    }

    public void setChildBridge(ChildBridgeModel childBridge) {
        this.childBridge = childBridge;
    }

    public ForeignCollection<DiseaseModel> getDiseases() {
        return diseases;
    }
}

package com.xiaohui.bridge.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.xiaohui.bridge.business.bean.ChildBridge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xhChen on 14/10/28.
 */
@DatabaseTable(tableName = "ChildBridge")
public class ChildBridgeModel {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ChildBridge childBridge;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "bridge_id")
    private BridgeModel bridge;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<BlockModel> blocks;

    public int getId() {
        return id;
    }

    public ChildBridge getChildBridge() {
        return childBridge;
    }

    public void setChildBridge(ChildBridge childBridge) {
        this.childBridge = childBridge;
    }

    public BridgeModel getBridge() {
        return bridge;
    }

    public void setBridge(BridgeModel bridge) {
        this.bridge = bridge;
    }

    public List<BlockModel> getBlocks() {
        List<BlockModel> list = new ArrayList<BlockModel>();
        Iterator<BlockModel> iterator = blocks.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
}

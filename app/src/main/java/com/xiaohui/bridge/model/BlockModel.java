package com.xiaohui.bridge.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.xiaohui.bridge.business.bean.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaohui on 14-10-28.
 */
@DatabaseTable(tableName = "Block")
public class BlockModel {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Block block;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "child_bridge_id")
    private ChildBridgeModel childBridge;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<ComponentModel> components;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private HashMap<String, String> blockProperty;

    public int getId() {
        return id;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public ChildBridgeModel getChildBridge() {
        return childBridge;
    }

    public void setChildBridge(ChildBridgeModel childBridge) {
        this.childBridge = childBridge;
    }

    public List<ComponentModel> getComponents() {
        List<ComponentModel> list = new ArrayList<ComponentModel>();
        Iterator<ComponentModel> iterator = components.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public Map<String, String> getBlockProperty() {
        return blockProperty;
    }

    public void setBlockProperty(HashMap<String, String> blockProperty) {
        this.blockProperty = blockProperty;
    }
}

package com.xiaohui.bridge.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.xiaohui.bridge.business.bean.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xhChen on 14/10/28.
 */
@DatabaseTable(tableName = "Component")
public class ComponentModel {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Component component;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "block_id")
    private BlockModel block;
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

    public BlockModel getBlock() {
        return block;
    }

    public void setBlock(BlockModel block) {
        this.block = block;
    }

    public List<DiseaseModel> getDiseases() {
        List<DiseaseModel> list = new ArrayList<DiseaseModel>();
        Iterator<DiseaseModel> iterator = diseases.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
}

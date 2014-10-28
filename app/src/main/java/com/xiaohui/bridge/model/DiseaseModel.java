package com.xiaohui.bridge.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.xiaohui.bridge.business.bean.Disease;
import com.xiaohui.bridge.business.bean.Project;

/**
 */
@DatabaseTable(tableName = "Disease")
public class DiseaseModel {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Disease disease;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<BridgeModel> bridges;

    public int getId() {
        return id;
    }


    public void setProject(Disease disease) {
        this.disease = disease;
    }

    public Disease getProject() {
        return disease;
    }
}

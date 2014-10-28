package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.model.DiseaseModel;

import org.robobinding.itempresentationmodel.ItemContext;
import org.robobinding.itempresentationmodel.ItemPresentationModel;

/**
 * Created by xiaohui on 14-10-28.
 */
public class DiseaseItemViewModel implements ItemPresentationModel<DiseaseModel> {

    private String title;

    public String getTitle() {
        return title;
    }

    @Override
    public void updateData(DiseaseModel diseaseModel, ItemContext itemContext) {
        title = String.valueOf(itemContext.getPosition() + 1)
                + " " + diseaseModel.getDisease().getDiseaseType();
    }
}

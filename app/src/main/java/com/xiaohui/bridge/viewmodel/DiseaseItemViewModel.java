package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.business.bean.Disease;
import com.xiaohui.bridge.model.DiseaseModel;
import com.xiaohui.bridge.util.ListUtil;

import org.joda.time.DateTime;
import org.robobinding.itempresentationmodel.ItemContext;
import org.robobinding.itempresentationmodel.ItemPresentationModel;

import java.util.Collections;

/**
 * Created by xiaohui on 14-10-28.
 */
public class DiseaseItemViewModel implements ItemPresentationModel<DiseaseModel> {

    private String title;
    private String count;
    private String location;
    private String time;

    public String getTitle() {
        return title;
    }

    public String getCount() {
        return count;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    @Override
    public void updateData(DiseaseModel diseaseModel, ItemContext itemContext) {
        Disease disease = diseaseModel.getDisease();
        title = String.valueOf(itemContext.getPosition() + 1)
                + " " + disease.getType();
        count = "多媒体：" + (ListUtil.sizeOfList(disease.getPictureList())
                + ListUtil.sizeOfList(disease.getVideoList()) + ListUtil.sizeOfList(disease.getVoiceList()));
        location = "部位：" + disease.getLocation();
        time = new DateTime(diseaseModel.getTime()).toString("yyyy-MM-dd HH:mm");
    }
}

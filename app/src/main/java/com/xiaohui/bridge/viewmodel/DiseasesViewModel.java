package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.model.DiseaseModel;
import com.xiaohui.bridge.storage.Cookie;
import com.xiaohui.bridge.view.IDiseaseView;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.widget.adapterview.ItemClickEvent;

import java.util.List;

/**
 * Created by xiaohui on 14-10-28.
 */
@PresentationModel
public class DiseasesViewModel {

    private IDiseaseView view;
    private Cookie cookie;
    private ComponentModel componentModel;

    public DiseasesViewModel(IDiseaseView view, Cookie cookie, ComponentModel componentModel) {
        this.view = view;
        this.cookie = cookie;
        this.componentModel = componentModel;
    }

    @ItemPresentationModel(DiseaseItemViewModel.class)
    public List<DiseaseModel> getDiseases() {
        return componentModel.getDiseases();
    }

    public void onItemClick(ItemClickEvent event) {
        int pos = event.getPosition();
        DiseaseModel diseaseModel = getDiseases().get(pos);
        cookie.put(Keys.DISEASE, diseaseModel);
        view.onItemSelect(pos, diseaseModel);
    }
}

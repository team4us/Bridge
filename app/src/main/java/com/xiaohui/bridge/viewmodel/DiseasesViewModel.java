package com.xiaohui.bridge.viewmodel;

import com.j256.ormlite.dao.Dao;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.model.DiseaseModel;
import com.xiaohui.bridge.storage.Cookie;
import com.xiaohui.bridge.view.IDiseaseView;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;
import org.robobinding.widget.adapterview.ItemClickEvent;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by xiaohui on 14-10-28.
 */
@PresentationModel
public class DiseasesViewModel implements HasPresentationModelChangeSupport {

    private final PresentationModelChangeSupport changeSupport;
    private IDiseaseView view;
    private Cookie cookie;
    private ComponentModel componentModel;
    private Dao<ComponentModel, Integer> dao;

    public DiseasesViewModel(IDiseaseView view, Cookie cookie, ComponentModel componentModel, Dao<ComponentModel, Integer> dao) {
        this.view = view;
        this.cookie = cookie;
        this.componentModel = componentModel;
        changeSupport = new PresentationModelChangeSupport(this);
        this.dao = dao;
    }

    @ItemPresentationModel(DiseaseItemViewModel.class)
    public List<DiseaseModel> getDiseases() {
        return componentModel.getDiseases();
    }

    public void updateData(){
        try {
            componentModel = this.dao.queryForSameId(componentModel);
            cookie.put(Keys.COMPONENT, componentModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        changeSupport.firePropertyChange("diseases");
    }

    public void onItemClick(ItemClickEvent event) {
        int pos = event.getPosition();
        DiseaseModel diseaseModel = getDiseases().get(pos);
        cookie.put(Keys.DISEASE, diseaseModel);
        view.onItemSelect(pos, diseaseModel);
    }

    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return changeSupport;
    }
}

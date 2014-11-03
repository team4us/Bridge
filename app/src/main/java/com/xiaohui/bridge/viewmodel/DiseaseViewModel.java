package com.xiaohui.bridge.viewmodel;

import android.content.res.Resources;

import com.xiaohui.bridge.BuildConfig;
import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.XhApplication;
import com.xiaohui.bridge.business.store.StoreManager;
import com.xiaohui.bridge.model.BlockModel;
import com.xiaohui.bridge.model.ComponentModel;
import com.xiaohui.bridge.storage.Cookie;
import com.xiaohui.bridge.view.IDiseaseView;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xhChen on 14/10/29.
 */
@PresentationModel
public class DiseaseViewModel {

    private IDiseaseView view;
    private String componentName;
    private int blockType;

    public DiseaseViewModel(IDiseaseView view, Cookie cookie) {
        this.view = view;
        ComponentModel componentModel = (ComponentModel) cookie.get(Keys.COMPONENT);
        componentName = componentModel.getComponent().getName();
        BlockModel blockModel = (BlockModel) cookie.get(Keys.BLOCK);
        blockType = blockModel.getBlock().getType();
    }

    public String getComponentName() {
        return componentName;
    }

    @ItemPresentationModel(LocationItemViewModel.class)
    public List<String> getLocations() {
        return Arrays.asList(StoreManager.Instance.generalsTypes);
    }

    @ItemPresentationModel(LocationItemViewModel.class)
    public List<String> getDiseaseTypes() {
        String key = "disease_type_" + String.valueOf(blockType + 1);
        Resources resources = XhApplication.getApplication().getResources();
        int id = resources.getIdentifier(key, "array", BuildConfig.PACKAGE_NAME);
        String[] diseaseTypes = resources.getStringArray(id);
        return Arrays.asList(diseaseTypes);
    }

    public void onClickTakePhoto() {
        view.takePhoto();
    }

    public void onClickSelectPic() {
        view.selectPictures();
    }

    public void onClickVoice() {
        view.takeVoice();
    }

    public void onClickMovie() {
        view.takeMovie();
    }
}

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
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xhChen on 14/10/29.
 */
@PresentationModel
public class DiseaseViewModel {

    private PresentationModelChangeSupport changeSupport;
    private IDiseaseView view;
    private String componentName;
    private List<String> locations;
    private List<String> diseaseTypes;
    private List<Integer> methods;
    private int method;

    public DiseaseViewModel(IDiseaseView view, Cookie cookie) {
        changeSupport = new PresentationModelChangeSupport(this);
        this.view = view;
        ComponentModel componentModel = (ComponentModel) cookie.get(Keys.COMPONENT);
        componentName = componentModel.getComponent().getName();
        initData(cookie);
    }

    private void initData(Cookie cookie) {
        locations = Arrays.asList(StoreManager.Instance.generalsTypes);

        BlockModel blockModel = (BlockModel) cookie.get(Keys.BLOCK);
        int blockType = blockModel.getBlock().getType();
        String key = "disease_type_" + blockType;
        Resources resources = XhApplication.getApplication().getResources();
        int id = resources.getIdentifier(key, "array", BuildConfig.PACKAGE_NAME);
        diseaseTypes = Arrays.asList(resources.getStringArray(id));

        key = "disease_method_" + blockType;
        id = resources.getIdentifier(key, "array", BuildConfig.PACKAGE_NAME);
        methods = new ArrayList<Integer>();
        for (int method : resources.getIntArray(id)) {
            methods.add(method);
        }
    }

    public String getComponentName() {
        return componentName;
    }

    @ItemPresentationModel(LocationItemViewModel.class)
    public List<String> getLocations() {
        return locations;
    }

    @ItemPresentationModel(LocationItemViewModel.class)
    public List<String> getDiseaseTypes() {
        return diseaseTypes;
    }

    public int getMethod() {
        return method;
    }

    public void onItemClickLocation(int position) {

    }

    public void onItemClickDiseaseType(int position) {
        method = methods.get(position);
        view.updateMethodView(method);
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

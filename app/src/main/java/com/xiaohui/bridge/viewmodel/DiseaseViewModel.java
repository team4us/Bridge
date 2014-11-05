package com.xiaohui.bridge.viewmodel;

import android.content.res.Resources;
import android.view.View;

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

    private IDiseaseView view;
    private String componentName;
    private List<String> locations;
    private List<String> types;
    private List<Integer> methods;
    private String location;
    private String type;

    public DiseaseViewModel(IDiseaseView view, Cookie cookie) {
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
        types = Arrays.asList(resources.getStringArray(id));

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
    public List<String> getTypes() {
        return types;
    }

    public int indexWithType(String name) {
        int size = types.size();
        for (int i = 0; i < size; i++) {
            String type = types.get(i);
            if (type.equalsIgnoreCase(name)) {
                return i;
            }
        }
        return size - 1; //如果不存在相同的则取最后一个“其他”
    }

    public int indexWithLocation(String name) {
        for (int i = 0; i < locations.size(); i++) {
            String location = locations.get(i);
            if (location.equalsIgnoreCase(name)) {
                return i;
            }
        }

        return 0;
    }

    public void onItemClickLocation(int position) {
        location = locations.get(position);
    }

    public void onItemClickDiseaseType(int position) {
        type = types.get(position);
        boolean isOther = position == types.size() - 1;
        view.updateMethodView(isOther, methods.get(position));
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

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

}

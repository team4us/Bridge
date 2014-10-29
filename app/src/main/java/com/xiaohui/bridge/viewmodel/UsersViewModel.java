package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.Keys;
import com.xiaohui.bridge.model.UserModel;
import com.xiaohui.bridge.storage.Cookie;
import com.xiaohui.bridge.storage.DatabaseHelper;
import com.xiaohui.bridge.view.IUserView;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;
import org.robobinding.widget.adapterview.ItemClickEvent;

import java.util.List;

/**
 */
@PresentationModel
public class UsersViewModel implements HasPresentationModelChangeSupport {

    private final PresentationModelChangeSupport changeSupport;
    private IUserView view;
    private Cookie cookie;
    private DatabaseHelper helper;

    public UsersViewModel(IUserView view, Cookie cookie, DatabaseHelper helper) {
        this.view = view;
        this.cookie = cookie;
        changeSupport = new PresentationModelChangeSupport(this);
        this.helper = helper;
    }

    @ItemPresentationModel(UserItemViewModel.class)
    public List<UserModel> getDiseases() {
//        return componentModel.getDiseases();
        return null;
    }

    public void onItemClick(ItemClickEvent event) {
        int pos = event.getPosition();
        UserModel userModel = getDiseases().get(pos);
        cookie.put(Keys.USER, userModel);
        view.onItemSelect(pos, userModel);
    }


    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return changeSupport;
    }
}

package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.model.UserModel;

import org.robobinding.itempresentationmodel.ItemContext;
import org.robobinding.itempresentationmodel.ItemPresentationModel;

/**
 *
 * Created by xiaohui on 14-10-28.
 */
public class UserItemViewModel implements ItemPresentationModel<UserModel> {

    private String title;

    public String getTitle() {
        return title;
    }

    @Override
    public void updateData(UserModel userModel, ItemContext itemContext) {
        title = String.valueOf(itemContext.getPosition() + 1)
                + " " + userModel.getUserName();
    }
}

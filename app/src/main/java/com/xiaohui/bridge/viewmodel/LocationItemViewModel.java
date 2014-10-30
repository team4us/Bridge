package com.xiaohui.bridge.viewmodel;

import org.robobinding.itempresentationmodel.ItemContext;
import org.robobinding.itempresentationmodel.ItemPresentationModel;

/**
 * Created by xiaohui on 14-9-24.
 */
public class LocationItemViewModel implements ItemPresentationModel<String> {

    private String text;

    public String getText() {
        return text;
    }


    @Override
    public void updateData(String text, ItemContext itemContext) {
        this.text = text;
    }
}

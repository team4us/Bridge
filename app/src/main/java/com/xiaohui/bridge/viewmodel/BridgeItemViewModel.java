package com.xiaohui.bridge.viewmodel;

import org.robobinding.itempresentationmodel.ItemPresentationModel;

/**
 * Created by xiaohui on 14-9-24.
 */
public class BridgeItemViewModel implements ItemPresentationModel<String> {

    private String title;

    public String getTitle() {
        return title;
    }

    @Override
    public void updateData(int i, String s) {
        title = s;
    }
}

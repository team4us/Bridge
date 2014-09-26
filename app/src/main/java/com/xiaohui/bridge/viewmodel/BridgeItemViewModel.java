package com.xiaohui.bridge.viewmodel;

import com.xiaohui.bridge.business.bean.Bridge;

import org.robobinding.itempresentationmodel.ItemPresentationModel;

/**
 * Created by xiaohui on 14-9-24.
 */
public class BridgeItemViewModel implements ItemPresentationModel<Bridge> {

    private String title;

    public String getTitle() {
        return title;
    }

    @Override
    public void updateData(int index, Bridge bridge) {
        title = bridge.getName();
    }
}
